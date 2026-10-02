package main;

import controller.EventoController;
import model.EventoService;
import view.FrmEvento;

public class SistemaGestionEventos {

    public static void main(String[] args) {

        // Configurar colores globales para los cuadros de diálogo (JOptionPane)
        javax.swing.UIManager.put("OptionPane.background", new java.awt.Color(51, 51, 51));
        javax.swing.UIManager.put("Panel.background", new java.awt.Color(51, 51, 51));
        javax.swing.UIManager.put("OptionPane.messageForeground", new java.awt.Color(255, 255, 255));
        javax.swing.UIManager.put("Button.background", new java.awt.Color(255, 117, 31));
        javax.swing.UIManager.put("Button.foreground", new java.awt.Color(255, 255, 255));
        javax.swing.UIManager.put("Button.font", new java.awt.Font("Arial Black", 0, 12));

        FrmEvento vista = new FrmEvento();

        model.IEventoRepository repository = new model.EventoRepository();
        EventoService servicio = new EventoService(repository);

        EventoController controlador =
                new EventoController(vista, servicio);

        controlador.iniciar();

        vista.setVisible(true);
    }
}