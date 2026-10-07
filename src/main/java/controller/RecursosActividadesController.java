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

    public RecursosActividadesController(PnlRecursosActividades vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;
        this.repo = RecursosActividadesRepository.getInstance();

        vista.getBtnAgregarActividad().addActionListener(e -> agregarActividad());
        vista.getBtnAgregarRecurso().addActionListener(e -> agregarRecurso());
    }

    public void iniciar() {
        actualizarComboboxes();
        actualizarTablas();
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

    private void agregarActividad() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para agregar actividades.");
            return;
        }
        String nombre = vista.getTxtActNombre().getText();
        String horario = vista.getTxtActHorario().getText();
        String resp = vista.getTxtActResponsable().getText();
        String evt = vista.getCbxActEvento().getSelectedItem().toString();

        if (nombre.isEmpty() || horario.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Nombre, horario y evento son obligatorios.");
            return;
        }
        repo.agregarActividad(new Actividad(repo.getNextActividadId(), nombre, horario, resp, evt));
        vista.getTxtActNombre().setText("");
        vista.getTxtActHorario().setText("");
        vista.getTxtActResponsable().setText("");
        actualizarTablas();
        JOptionPane.showMessageDialog(vista, "Actividad agregada.");
    }

    private void agregarRecurso() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para agregar recursos.");
            return;
        }
        String tipo = vista.getTxtRecTipo().getText();
        int cant = (int) vista.getSpRecCantidad().getValue();
        String evt = vista.getCbxRecEvento().getSelectedItem().toString();

        if (tipo.isEmpty() || evt.equals("Seleccione...")) {
            JOptionPane.showMessageDialog(vista, "Tipo y evento son obligatorios.");
            return;
        }
        repo.agregarRecurso(new Recurso(repo.getNextRecursoId(), tipo, cant, evt));
        vista.getTxtRecTipo().setText("");
        actualizarTablas();
        JOptionPane.showMessageDialog(vista, "Recurso agregado.");
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
