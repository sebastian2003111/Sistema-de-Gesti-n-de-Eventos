package main;

import controller.EventoController;
import model.EventoService;
import view.FrmEvento;

public class SistemaGestionEventos {

    public static void main(String[] args) {

        // Configurar FlatLaf (Tema Oscuro Moderno)
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            
            // Soporte para Emojis nativo de Windows (Evita los cuadritos)
            javax.swing.UIManager.put("defaultFont", new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 14));

            // Personalizar algunos colores de FlatLaf para que coincidan con tu marca
            javax.swing.UIManager.put("Button.arc", 10); // Botones redondeados
            javax.swing.UIManager.put("Component.arc", 10); // Campos de texto redondeados
            javax.swing.UIManager.put("Button.background", new java.awt.Color(255, 117, 31)); // Naranja
            javax.swing.UIManager.put("Button.foreground", java.awt.Color.WHITE);
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf");
        }

        // Inicializar Dashboard en modo Invitado
        view.FrmDashboard vistaDashboard = new view.FrmDashboard();
        controller.DashboardController dashboardController = new controller.DashboardController(vistaDashboard, null); // null = Modo Invitado
    }
}