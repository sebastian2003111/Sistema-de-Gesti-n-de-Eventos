package main;

import controller.EventoController;
import model.EventoService;
import view.FrmEvento;

public class SistemaGestionEventos {

    public static void main(String[] args) {

        FrmEvento vista = new FrmEvento();

        model.IEventoRepository repository = new model.EventoRepository();
        EventoService servicio = new EventoService(repository);

        EventoController controlador =
                new EventoController(vista, servicio);

        controlador.iniciar();

        vista.setVisible(true);
    }
}