package main;

import controller.EventoController;
import model.EventoService;
import view.FrmEvento;

public class SistemaGestionEventos {

    public static void main(String[] args) {

        // Configurar FlatLaf (Tema Oscuro Moderno por defecto) mediante ThemeManager
        try {
            config.ThemeManager.setTema(true, null);
        } catch (Exception ex) {
            System.err.println("Error al inicializar ThemeManager / FlatLaf: " + ex.getMessage());
        }

        // Inicializar Dashboard en modo Invitado
        view.FrmDashboard vistaDashboard = new view.FrmDashboard();
        controller.DashboardController dashboardController = new controller.DashboardController(vistaDashboard, null); // null = Modo Invitado
    }
}