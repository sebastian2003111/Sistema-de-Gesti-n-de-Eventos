package controller;

import model.Actividad;
import model.Recurso;
import model.RecursosActividadesRepository;
import model.EventoRepository;
import model.Evento;
import model.Usuario;
import view.PnlRecursosActividades;
import javax.swing.JOptionPane;

public class RecursosActividadesController {
    private PnlRecursosActividades vista;
    private RecursosActividadesRepository repo;
    private Usuario usuarioActual;

    private Integer idActividadSeleccionada = null;
    private Integer idRecursoSeleccionado = null;

    public RecursosActividadesController(PnlRecursosActividades vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;
        this.repo = RecursosActividadesRepository.getInstance();

        // Listeners Actividades
        vista.getBtnAgregarActividad().addActionListener(e -> agregarActividad());
        vista.getBtnActualizarActividad().addActionListener(e -> actualizarActividad());
        vista.getBtnEliminarActividad().addActionListener(e -> eliminarActividad());
        vista.getBtnLimpiarActividad().addActionListener(e -> limpiarFormActividad());

        vista.getTblActividades().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarActividadSeleccionada();
            }
        });

        // Listeners Recursos
        vista.getBtnAgregarRecurso().addActionListener(e -> agregarRecurso());
        vista.getBtnActualizarRecurso().addActionListener(e -> actualizarRecurso());
        vista.getBtnEliminarRecurso().addActionListener(e -> eliminarRecurso());
        vista.getBtnLimpiarRecurso().addActionListener(e -> limpiarFormRecurso());

        vista.getTblRecursos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarRecursoSeleccionado();
            }
        });
    }

    public void iniciar() {
        actualizarComboboxes();
        actualizarTablas();
        limpiarFormActividad();
        limpiarFormRecurso();
    }

    private void actualizarComboboxes() {
        vista.getCbxActEvento().removeAllItems();
        vista.getCbxRecEvento().removeAllItems();
        vista.getCbxActEvento().addItem("Seleccione...");
        vista.getCbxRecEvento().addItem("Seleccione...");
        
        for (Evento ev : EventoRepository.getInstance().obtenerTodos()) {
            vista.getCbxActEvento().addItem(ev.getNombre());
            vista.getCbxRecEvento().addItem(ev.getNombre());
        }
    }

    // --- MÉTODOS ACTIVIDADES ---

    private void cargarActividadSeleccionada() {
        int fila = vista.getTblActividades().getSelectedRow();
        if (fila >= 0) {
            idActividadSeleccionada = (Integer) vista.getModeloActividades().getValueAt(fila, 0);
            String nombre = (String) vista.getModeloActividades().getValueAt(fila, 1);
            String horario = (String) vista.getModeloActividades().getValueAt(fila, 2);
            String resp = (String) vista.getModeloActividades().getValueAt(fila, 3);
            String evento = (String) vista.getModeloActividades().getValueAt(fila, 4);

            vista.getTxtActNombre().setText(nombre);
            vista.getTxtActHorario().setText(horario);
            vista.getTxtActResponsable().setText(resp);
            vista.getCbxActEvento().setSelectedItem(evento);

            vista.getBtnAgregarActividad().setEnabled(false);
            vista.getBtnActualizarActividad().setEnabled(true);
            vista.getBtnEliminarActividad().setEnabled(true);
        }
    }

    private void limpiarFormActividad() {
        idActividadSeleccionada = null;
        vista.getTxtActNombre().setText("");
        vista.getTxtActHorario().setText("");
        vista.getTxtActResponsable().setText("");
        if (vista.getCbxActEvento().getItemCount() > 0) {
            vista.getCbxActEvento().setSelectedIndex(0);
        }
        vista.getTblActividades().clearSelection();
        vista.getBtnAgregarActividad().setEnabled(true);
        vista.getBtnActualizarActividad().setEnabled(false);
        vista.getBtnEliminarActividad().setEnabled(false);
    }

    private void agregarActividad() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para agregar actividades.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = vista.getTxtActNombre().getText().trim();
        String horario = vista.getTxtActHorario().getText().trim();
        String resp = vista.getTxtActResponsable().getText().trim();
        String evt = vista.getCbxActEvento().getSelectedItem().toString();

        if (nombre.isEmpty() || horario.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Nombre, horario y evento son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            repo.agregarActividad(new Actividad(repo.getNextActividadId(), nombre, horario, resp, evt));
            limpiarFormActividad();
            actualizarTablas();
            JOptionPane.showMessageDialog(vista, "¡Actividad agregada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al agregar la actividad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarActividad() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para actualizar actividades.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona una actividad de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = vista.getTxtActNombre().getText().trim();
        String horario = vista.getTxtActHorario().getText().trim();
        String resp = vista.getTxtActResponsable().getText().trim();
        String evt = vista.getCbxActEvento().getSelectedItem().toString();

        if (nombre.isEmpty() || horario.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Nombre, horario y evento son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            repo.actualizarActividad(new Actividad(idActividadSeleccionada, nombre, horario, resp, evt));
            limpiarFormActividad();
            actualizarTablas();
            JOptionPane.showMessageDialog(vista, "¡Actividad actualizada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al actualizar la actividad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarActividad() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para eliminar actividades.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona una actividad de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(vista, "¿Seguro que deseas eliminar esta actividad?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                repo.eliminarActividad(idActividadSeleccionada);
                limpiarFormActividad();
                actualizarTablas();
                JOptionPane.showMessageDialog(vista, "Actividad eliminada.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Ocurrió un error al eliminar la actividad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- MÉTODOS RECURSOS ---

    private void cargarRecursoSeleccionado() {
        int fila = vista.getTblRecursos().getSelectedRow();
        if (fila >= 0) {
            idRecursoSeleccionado = (Integer) vista.getModeloRecursos().getValueAt(fila, 0);
            String tipo = (String) vista.getModeloRecursos().getValueAt(fila, 1);
            int cant = (Integer) vista.getModeloRecursos().getValueAt(fila, 2);
            String evento = (String) vista.getModeloRecursos().getValueAt(fila, 3);

            vista.getTxtRecTipo().setText(tipo);
            vista.getSpRecCantidad().setValue(cant);
            vista.getCbxRecEvento().setSelectedItem(evento);

            vista.getBtnAgregarRecurso().setEnabled(false);
            vista.getBtnActualizarRecurso().setEnabled(true);
            vista.getBtnEliminarRecurso().setEnabled(true);
        }
    }

    private void limpiarFormRecurso() {
        idRecursoSeleccionado = null;
        vista.getTxtRecTipo().setText("");
        vista.getSpRecCantidad().setValue(1);
        if (vista.getCbxRecEvento().getItemCount() > 0) {
            vista.getCbxRecEvento().setSelectedIndex(0);
        }
        vista.getTblRecursos().clearSelection();
        vista.getBtnAgregarRecurso().setEnabled(true);
        vista.getBtnActualizarRecurso().setEnabled(false);
        vista.getBtnEliminarRecurso().setEnabled(false);
    }

    private void agregarRecurso() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para agregar recursos.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = vista.getTxtRecTipo().getText().trim();
        int cant = (int) vista.getSpRecCantidad().getValue();
        String evt = vista.getCbxRecEvento().getSelectedItem().toString();

        if (tipo.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Tipo y evento son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            repo.agregarRecurso(new Recurso(repo.getNextRecursoId(), tipo, cant, evt));
            limpiarFormRecurso();
            actualizarTablas();
            JOptionPane.showMessageDialog(vista, "¡Recurso agregado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al agregar el recurso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarRecurso() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para actualizar recursos.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idRecursoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona un recurso de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = vista.getTxtRecTipo().getText().trim();
        int cant = (int) vista.getSpRecCantidad().getValue();
        String evt = vista.getCbxRecEvento().getSelectedItem().toString();

        if (tipo.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Tipo y evento son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            repo.actualizarRecurso(new Recurso(idRecursoSeleccionado, tipo, cant, evt));
            limpiarFormRecurso();
            actualizarTablas();
            JOptionPane.showMessageDialog(vista, "¡Recurso actualizado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al actualizar el recurso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRecurso() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para eliminar recursos.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idRecursoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona un recurso de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(vista, "¿Seguro que deseas eliminar este recurso?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                repo.eliminarRecurso(idRecursoSeleccionado);
                limpiarFormRecurso();
                actualizarTablas();
                JOptionPane.showMessageDialog(vista, "Recurso eliminado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Ocurrió un error al eliminar el recurso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTablas() {
        vista.getModeloActividades().setRowCount(0);
        for (Actividad a : repo.obtenerActividades()) {
            vista.getModeloActividades().addRow(new Object[]{a.getId(), a.getNombre(), a.getHorario(), a.getResponsable(), a.getEventoAsignado()});
        }
        vista.getModeloRecursos().setRowCount(0);
        for (Recurso r : repo.obtenerRecursos()) {
            vista.getModeloRecursos().addRow(new Object[]{r.getId(), r.getTipo(), r.getCantidad(), r.getEventoAsignado()});
        }
    }
}
