package controller;

import model.Participante;
import model.ParticipanteRepository;
import model.IEventoRepository;
import model.EventoRepository;
import model.Evento;
import model.Usuario;
import view.PnlParticipantes;
import javax.swing.JOptionPane;
import java.util.List;

public class ParticipantesController {
    private PnlParticipantes vista;
    private ParticipanteRepository repo;
    private IEventoRepository eventoRepo;

    private Usuario usuarioActual;

    public ParticipantesController(PnlParticipantes vista, Usuario usuarioActual) {
        this.vista = vista;
        this.repo = ParticipanteRepository.getInstance();
        this.eventoRepo = EventoRepository.getInstance();
        this.usuarioActual = usuarioActual;
        
        vista.getBtnAgregar().addActionListener(e -> agregarParticipante());
    }

    public void iniciar() {
        actualizarComboboxEventos();
        actualizarTabla();
    }

    public void actualizarComboboxEventos() {
        vista.getCbxEventos().removeAllItems();
        vista.getCbxEventos().addItem("Seleccione un evento...");
        List<Evento> eventos = eventoRepo.obtenerTodos();
        for (Evento ev : eventos) {
            vista.getCbxEventos().addItem(ev.getNombre());
        }
    }

    private void agregarParticipante() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para registrar participantes.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = vista.getTxtNombre().getText();
        String correo = vista.getTxtCorreo().getText();
        String evento = vista.getCbxEventos().getSelectedItem().toString();

        if (nombre.isEmpty() || correo.isEmpty() || evento.equals("Seleccione un evento...")) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Participante p = new Participante(repo.getNextId(), nombre, correo, evento);
        repo.agregar(p);
        
        vista.getTxtNombre().setText("");
        vista.getTxtCorreo().setText("");
        vista.getCbxEventos().setSelectedIndex(0);
        
        actualizarTabla();
        JOptionPane.showMessageDialog(vista, "Participante agregado con éxito.");
    }

    private void actualizarTabla() {
        vista.getModeloTabla().setRowCount(0);
        for (Participante p : repo.obtenerTodos()) {
            vista.getModeloTabla().addRow(new Object[]{p.getId(), p.getNombre(), p.getCorreo(), p.getEventoAsignado()});
        }
    }
}
