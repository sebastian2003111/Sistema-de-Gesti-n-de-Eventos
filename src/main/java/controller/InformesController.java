package controller;

import model.Evento;
import model.EventoRepository;
import view.PnlInformes;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InformesController {
    private PnlInformes vista;

    public InformesController(PnlInformes vista) {
        this.vista = vista;
        this.vista.getBtnConsultar().addActionListener(e -> consultar());
        this.vista.getBtnLimpiar().addActionListener(e -> limpiarFiltros());
        this.vista.getBtnGenerarInforme().addActionListener(e -> generarInforme());
    }

    public void iniciar() {
        limpiarFiltros(); // Esto ya llama a consultar() internamente y deja todo limpio
    }

    private void limpiarFiltros() {
        vista.getDpDesde().clear();
        vista.getDpHasta().clear();
        vista.getCbxTipo().setSelectedIndex(0);
        vista.getCbxEstado().setSelectedIndex(0);
        consultar();
    }

    private void consultar() {
        List<Evento> todos = EventoRepository.getInstance().obtenerTodos();
        
        // Obtener filtros
        LocalDate desde = vista.getDpDesde().getDate();
        LocalDate hasta = vista.getDpHasta().getDate();
        String tipoFiltro = vista.getCbxTipo().getSelectedItem().toString();
        String estadoFiltro = vista.getCbxEstado().getSelectedItem().toString();

        // Filtrar
        List<Evento> filtrados = todos.stream().filter(e -> {
            boolean cumpleDesde = (desde == null) || (e.getFecha() != null && !e.getFecha().isBefore(desde));
            boolean cumpleHasta = (hasta == null) || (e.getFecha() != null && !e.getFecha().isAfter(hasta));
            boolean cumpleTipo = tipoFiltro.equals("Todos") || (e.getTipo() != null && e.getTipo().equals(tipoFiltro));
            boolean cumpleEstado = estadoFiltro.equals("Todos") || (e.getEstado() != null && e.getEstado().equalsIgnoreCase(estadoFiltro));
            return cumpleDesde && cumpleHasta && cumpleTipo && cumpleEstado;
        }).collect(Collectors.toList());

        actualizarTarjetas(filtrados);
        actualizarGraficos(filtrados);
        actualizarTabla(filtrados);
    }

    private void actualizarTarjetas(List<Evento> eventos) {
        int total = eventos.size();
        long programados = eventos.stream().filter(e -> "Programado".equalsIgnoreCase(e.getEstado())).count();
        long enProceso = eventos.stream().filter(e -> "En proceso".equalsIgnoreCase(e.getEstado())).count();
        long finalizados = eventos.stream().filter(e -> "Finalizado".equalsIgnoreCase(e.getEstado())).count();
        long cancelados = eventos.stream().filter(e -> "Cancelado".equalsIgnoreCase(e.getEstado())).count();

        vista.getLblTotal().setText(String.valueOf(total));
        vista.getLblProgramados().setText(String.valueOf(programados));
        vista.getLblEnProceso().setText(String.valueOf(enProceso));
        vista.getLblFinalizados().setText(String.valueOf(finalizados));
        vista.getLblCancelados().setText(String.valueOf(cancelados));
    }

    private void actualizarGraficos(List<Evento> eventos) {
        // Grafico de Tipo
        JPanel pnlTipo = vista.getPnlGraficoTipo();
        pnlTipo.removeAll();
        Map<String, Long> porTipo = eventos.stream().collect(Collectors.groupingBy(
                e -> e.getTipo() == null || e.getTipo().isEmpty() ? "Desconocido" : e.getTipo(), 
                Collectors.counting()
        ));
        crearBarras(pnlTipo, porTipo, eventos.size(), new Color(52, 152, 219));

        // Grafico de Estado
        JPanel pnlEstado = vista.getPnlGraficoEstado();
        pnlEstado.removeAll();
        Map<String, Long> porEstado = eventos.stream().collect(Collectors.groupingBy(
                e -> e.getEstado() == null || e.getEstado().isEmpty() ? "Desconocido" : e.getEstado(), 
                Collectors.counting()
        ));
        crearBarras(pnlEstado, porEstado, eventos.size(), new Color(46, 204, 113));

        pnlTipo.revalidate();
        pnlTipo.repaint();
        pnlEstado.revalidate();
        pnlEstado.repaint();
    }

    private void crearBarras(JPanel pnl, Map<String, Long> datos, int total, Color colorBarra) {
        if (total == 0) {
            JLabel lbl = new JLabel("No hay datos para graficar.");
            lbl.setForeground(Color.GRAY);
            pnl.add(lbl);
            return;
        }

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            JPanel fila = new JPanel(new BorderLayout(5, 5));
            fila.setBackground(new Color(35, 35, 35));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JLabel lblKey = new JLabel(entry.getKey() + " (" + entry.getValue() + ")");
            lblKey.setForeground(Color.LIGHT_GRAY);
            lblKey.setPreferredSize(new Dimension(120, 20));
            
            JProgressBar bar = new JProgressBar(0, total);
            bar.setValue(entry.getValue().intValue());
            bar.setForeground(colorBarra);
            bar.setBackground(new Color(50, 50, 50));
            bar.setBorderPainted(false);

            fila.add(lblKey, BorderLayout.WEST);
            fila.add(bar, BorderLayout.CENTER);
            
            pnl.add(fila);
        }
    }

    private void actualizarTabla(List<Evento> eventos) {
        vista.getModeloProximosEventos().setRowCount(0);
        // Filtrar solo los que sean 'Programado' o 'En proceso' y ordenarlos por fecha si es posible
        List<Evento> proximos = eventos.stream()
                .filter(e -> "Programado".equalsIgnoreCase(e.getEstado()) || "En proceso".equalsIgnoreCase(e.getEstado()))
                .sorted((e1, e2) -> {
                    if (e1.getFecha() == null) return 1;
                    if (e2.getFecha() == null) return -1;
                    return e1.getFecha().compareTo(e2.getFecha());
                })
                .collect(Collectors.toList());

        for (Evento ev : proximos) {
            String fechaStr = ev.getFecha() != null ? ev.getFecha().toString() : "Sin fecha";
            vista.getModeloProximosEventos().addRow(new Object[]{
                    fechaStr,
                    ev.getNombre(),
                    ev.getTipo(),
                    ev.getLugar(),
                    ev.getEstado()
            });
        }
    }

    private void generarInforme() {
        String total = vista.getLblTotal().getText();
        String programados = vista.getLblProgramados().getText();
        String finalizados = vista.getLblFinalizados().getText();
        
        String msj = "RESUMEN EJECUTIVO DE EVENTOS\n" +
                     "------------------------------------\n" +
                     "Total consultados: " + total + "\n" +
                     "Programados: " + programados + "\n" +
                     "Finalizados: " + finalizados + "\n\n" +
                     "Los filtros aplicados en pantalla han sido registrados en este informe.\n" +
                     "(En futuros incrementos esto generará un PDF real con diseño profesional).";
                     
        JOptionPane.showMessageDialog(vista, msj, "Informe Generado", JOptionPane.INFORMATION_MESSAGE);
    }
}
