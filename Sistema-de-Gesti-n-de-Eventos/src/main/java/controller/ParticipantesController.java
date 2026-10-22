package controller;

import model.Participante;
import model.ParticipanteRepository;
import model.IEventoRepository;
import model.EventoRepository;
import model.Evento;
import model.Usuario;
import model.EmailService;
import view.PnlParticipantes;
import javax.swing.JOptionPane;
import java.util.List;

public class ParticipantesController {
    private PnlParticipantes vista;
    private ParticipanteRepository repo;
    private IEventoRepository eventoRepo;

    private Usuario usuarioActual;

    private Integer idSeleccionado = null;

    public ParticipantesController(PnlParticipantes vista, Usuario usuarioActual) {
        this.vista = vista;
        this.repo = ParticipanteRepository.getInstance();
        this.eventoRepo = EventoRepository.getInstance();
        this.usuarioActual = usuarioActual;
        
        // Listeners de botones
        vista.getBtnAgregar().addActionListener(e -> agregarParticipante());
        vista.getBtnActualizar().addActionListener(e -> actualizarParticipante());
        vista.getBtnEliminar().addActionListener(e -> eliminarParticipante());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        vista.getBtnExportar().addActionListener(e -> exportarArchivo());

        // Listener de selección en la tabla
        vista.getTblParticipantes().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarParticipanteSeleccionado();
            }
        });
    }

    public void iniciar() {
        actualizarComboboxEventos();
        actualizarTabla();
        limpiarFormulario();
    }

    public void actualizarComboboxEventos() {
        vista.getCbxEventos().removeAllItems();
        vista.getCbxEventos().addItem("Seleccione un evento...");
        List<Evento> eventos = eventoRepo.obtenerTodos();
        for (Evento ev : eventos) {
            vista.getCbxEventos().addItem(ev.getNombre());
        }
    }

    private void cargarParticipanteSeleccionado() {
        int fila = vista.getTblParticipantes().getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (Integer) vista.getModeloTabla().getValueAt(fila, 0);
            String nombre = (String) vista.getModeloTabla().getValueAt(fila, 1);
            String correo = (String) vista.getModeloTabla().getValueAt(fila, 2);
            String evento = (String) vista.getModeloTabla().getValueAt(fila, 3);

            vista.getTxtNombre().setText(nombre);
            vista.getTxtCorreo().setText(correo);
            vista.getCbxEventos().setSelectedItem(evento);

            vista.getBtnAgregar().setEnabled(false);
            vista.getBtnActualizar().setEnabled(true);
            vista.getBtnEliminar().setEnabled(true);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        vista.getTxtNombre().setText("");
        vista.getTxtCorreo().setText("");
        if (vista.getCbxEventos().getItemCount() > 0) {
            vista.getCbxEventos().setSelectedIndex(0);
        }
        vista.getTblParticipantes().clearSelection();
        vista.getBtnAgregar().setEnabled(true);
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnEliminar().setEnabled(false);
    }

    private void agregarParticipante() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para registrar participantes.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = vista.getTxtNombre().getText().trim();
        String correo = vista.getTxtCorreo().getText().trim();
        String evento = vista.getCbxEventos().getSelectedItem().toString();

        if (nombre.isEmpty() || correo.isEmpty() || evento.equals("Seleccione un evento...")) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Participante p = new Participante(repo.getNextId(), nombre, correo, evento);
            repo.agregar(p);

            // Buscar información completa del evento para incluirla en el correo
            Evento eventoObj = eventoRepo.obtenerTodos().stream()
                    .filter(ev -> ev.getNombre() != null && ev.getNombre().equalsIgnoreCase(evento))
                    .findFirst()
                    .orElse(null);

            // Disparar envío de correo en segundo plano (asíncrono, no bloquea la interfaz)
            EmailService.enviarInvitacionAsync(p, eventoObj);
            
            limpiarFormulario();
            actualizarTabla();
            JOptionPane.showMessageDialog(vista, 
                    "¡Participante agregado con éxito!\nSe ha generado y despachado su invitación por correo electrónico.", 
                    "Registro Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al registrar el participante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarParticipante() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para actualizar participantes.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione un participante de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = vista.getTxtNombre().getText().trim();
        String correo = vista.getTxtCorreo().getText().trim();
        String evento = vista.getCbxEventos().getSelectedItem().toString();

        if (nombre.isEmpty() || correo.isEmpty() || evento.equals("Seleccione un evento...")) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Participante p = new Participante(idSeleccionado, nombre, correo, evento);
            repo.actualizar(p);

            limpiarFormulario();
            actualizarTabla();
            JOptionPane.showMessageDialog(vista, "¡Participante actualizado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al actualizar el participante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarParticipante() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para eliminar participantes.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione un participante de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro de eliminar al participante seleccionado?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
        );

        if (resp == JOptionPane.YES_OPTION) {
            try {
                repo.eliminar(idSeleccionado);
                limpiarFormulario();
                actualizarTabla();
                JOptionPane.showMessageDialog(vista, "Participante eliminado correctamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Ocurrió un error al eliminar el participante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportarArchivo() {
        List<Participante> lista = repo.obtenerTodos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay participantes registrados para exportar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Exportar Lista de Participantes a Excel");
        fileChooser.setSelectedFile(new java.io.File("participantes_eventos.xls"));
        
        javax.swing.filechooser.FileNameExtensionFilter filtroXls = 
                new javax.swing.filechooser.FileNameExtensionFilter("Libro de Excel (*.xls)", "xls");
        javax.swing.filechooser.FileNameExtensionFilter filtroCsv = 
                new javax.swing.filechooser.FileNameExtensionFilter("Valores separados por comas (*.csv)", "csv");
        
        fileChooser.addChoosableFileFilter(filtroXls);
        fileChooser.addChoosableFileFilter(filtroCsv);
        fileChooser.setFileFilter(filtroXls);

        int userSelection = fileChooser.showSaveDialog(vista);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String nombreArchivo = fileToSave.getName().toLowerCase();
            
            if (!nombreArchivo.endsWith(".xls") && !nombreArchivo.endsWith(".csv")) {
                if (fileChooser.getFileFilter() == filtroCsv) {
                    fileToSave = new java.io.File(fileToSave.getParentFile(), fileToSave.getName() + ".csv");
                } else {
                    fileToSave = new java.io.File(fileToSave.getParentFile(), fileToSave.getName() + ".xls");
                }
            }

            try {
                if (fileToSave.getName().toLowerCase().endsWith(".xls")) {
                    exportarXls(fileToSave, lista);
                } else {
                    exportarCsv(fileToSave, lista);
                }

                JOptionPane.showMessageDialog(
                        vista, 
                        "¡Lista de participantes exportada exitosamente a:\n" + fileToSave.getAbsolutePath(), 
                        "Exportación Completada", 
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportarXls(java.io.File file, List<Participante> lista) throws Exception {
        try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
            writer.write("<?mso-application progid=\"Excel.Sheet\"?>\r\n");
            writer.write("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\r\n");
            writer.write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\r\n");
            writer.write(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\r\n");
            writer.write(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\r\n");
            writer.write(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">\r\n");
            writer.write(" <Styles>\r\n");
            writer.write("  <Style ss:ID=\"Header\">\r\n");
            writer.write("   <Font ss:Bold=\"1\" ss:Color=\"#FFFFFF\" ss:Size=\"11\"/>\r\n");
            writer.write("   <Interior ss:Color=\"#1E293B\" ss:Pattern=\"Solid\"/>\r\n");
            writer.write("   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\"/>\r\n");
            writer.write("  </Style>\r\n");
            writer.write("  <Style ss:ID=\"Data\">\r\n");
            writer.write("   <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Center\"/>\r\n");
            writer.write("  </Style>\r\n");
            writer.write("  <Style ss:ID=\"DataCenter\">\r\n");
            writer.write("   <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\"/>\r\n");
            writer.write("  </Style>\r\n");
            writer.write(" </Styles>\r\n");
            writer.write(" <Worksheet ss:Name=\"Participantes\">\r\n");
            writer.write("  <Table>\r\n");
            writer.write("   <Column ss:Width=\"50\"/>\r\n");
            writer.write("   <Column ss:Width=\"180\"/>\r\n");
            writer.write("   <Column ss:Width=\"240\"/>\r\n");
            writer.write("   <Column ss:Width=\"200\"/>\r\n");
            writer.write("   <Row ss:StyleID=\"Header\" ss:Height=\"24\">\r\n");
            writer.write("    <Cell><Data ss:Type=\"String\">ID</Data></Cell>\r\n");
            writer.write("    <Cell><Data ss:Type=\"String\">Nombre Completo</Data></Cell>\r\n");
            writer.write("    <Cell><Data ss:Type=\"String\">Correo Electrónico</Data></Cell>\r\n");
            writer.write("    <Cell><Data ss:Type=\"String\">Evento Asignado</Data></Cell>\r\n");
            writer.write("   </Row>\r\n");

            for (Participante p : lista) {
                writer.write("   <Row ss:Height=\"20\">\r\n");
                writer.write(String.format("    <Cell ss:StyleID=\"DataCenter\"><Data ss:Type=\"Number\">%d</Data></Cell>\r\n", p.getId()));
                writer.write(String.format("    <Cell ss:StyleID=\"Data\"><Data ss:Type=\"String\">%s</Data></Cell>\r\n", escaparXml(p.getNombre())));
                writer.write(String.format("    <Cell ss:StyleID=\"Data\"><Data ss:Type=\"String\">%s</Data></Cell>\r\n", escaparXml(p.getCorreo())));
                writer.write(String.format("    <Cell ss:StyleID=\"Data\"><Data ss:Type=\"String\">%s</Data></Cell>\r\n", escaparXml(p.getEventoAsignado())));
                writer.write("   </Row>\r\n");
            }

            writer.write("  </Table>\r\n");
            writer.write(" </Worksheet>\r\n");
            writer.write("</Workbook>\r\n");
        }
    }

    private void exportarCsv(java.io.File file, List<Participante> lista) throws Exception {
        try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writer.write("ID;Nombre;Correo Electrónico;Evento Asignado\r\n");

            for (Participante p : lista) {
                writer.write(String.format("%d;\"%s\";\"%s\";\"%s\"\r\n",
                        p.getId(),
                        p.getNombre().replace("\"", "\"\""),
                        p.getCorreo().replace("\"", "\"\""),
                        p.getEventoAsignado().replace("\"", "\"\"")));
            }
        }
    }

    private String escaparXml(String str) {
        if (str == null) return "";
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }

    private void actualizarTabla() {
        vista.getModeloTabla().setRowCount(0);
        for (Participante p : repo.obtenerTodos()) {
            vista.getModeloTabla().addRow(new Object[]{p.getId(), p.getNombre(), p.getCorreo(), p.getEventoAsignado()});
        }
    }
}
