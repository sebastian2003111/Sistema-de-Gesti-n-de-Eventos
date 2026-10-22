package controller;

import model.NotificacionRepository;
import view.PnlNotificaciones;

public class NotificacionesController {
    private PnlNotificaciones vista;
    private NotificacionRepository repo;

    public NotificacionesController(PnlNotificaciones vista) {
        this.vista = vista;
        this.repo = NotificacionRepository.getInstance();

        vista.getBtnMarcarLeidas().addActionListener(e -> marcarLeidas());
    }

    public void iniciar() {
        actualizarVista();
    }

    private void marcarLeidas() {
        repo.marcarTodasComoLeidas();
        actualizarVista();
    }

    private void actualizarVista() {
        vista.renderNotificaciones(repo.obtenerTodas());
    }
}
