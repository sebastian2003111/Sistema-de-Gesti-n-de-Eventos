package view;

import config.ThemeManager;
import controller.ConfiguracionController;
import controller.DashboardController;
import model.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class VisualThemeVerificationTest {

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
        String artifactDir = "C:/Users/SEBASTIAN BOHORQUEZ/.gemini/antigravity/brain/337346e2-9ca9-417c-b8c9-7db2531b1b41";

        Usuario admin = new Usuario("admin", "admin@admin.com", "Admin123*", "Administrador");

        // 1. CAPTURA: PnlConfiguracion en Modo Oscuro (Verificando Perfil Rediseñado)
        ThemeManager.setTema(true, null);
        FrmDashboard dashDark = new FrmDashboard();
        DashboardController ctrlDark = new DashboardController(dashDark, admin);
        dashDark.setSize(1200, 800);
        dashDark.doLayout();
        dashDark.mostrarPanel("CONFIGURACION");
        dashDark.marcarBotonActivo(dashDark.getBtnConfiguracion());

        // Esperar layout
        Thread.sleep(800);

        BufferedImage imgDark = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gDark = imgDark.createGraphics();
        dashDark.paint(gDark);
        gDark.dispose();
        ImageIO.write(imgDark, "png", new File(artifactDir, "config_perfil_dark.png"));
        System.out.println("Guardado config_perfil_dark.png");
        dashDark.dispose();

        // 2. CAPTURA: FrmDashboard en Modo Claro con PnlConfiguracion
        ThemeManager.setTema(false, null);
        FrmDashboard dashLightConfig = new FrmDashboard();
        DashboardController ctrlLightConfig = new DashboardController(dashLightConfig, admin);
        dashLightConfig.setSize(1200, 800);
        dashLightConfig.doLayout();
        dashLightConfig.mostrarPanel("CONFIGURACION");
        dashLightConfig.marcarBotonActivo(dashLightConfig.getBtnConfiguracion());

        Thread.sleep(800);

        BufferedImage imgLightConfig = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gLightConfig = imgLightConfig.createGraphics();
        dashLightConfig.paint(gLightConfig);
        gLightConfig.dispose();
        ImageIO.write(imgLightConfig, "png", new File(artifactDir, "config_perfil_light.png"));
        System.out.println("Guardado config_perfil_light.png");

        // 3. CAPTURA: FrmDashboard en Modo Claro con Gestión de Eventos
        dashLightConfig.mostrarPanel("EVENTOS");
        dashLightConfig.marcarBotonActivo(dashLightConfig.getBtnEventos());
        Thread.sleep(800);

        BufferedImage imgLightEventos = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gLightEventos = imgLightEventos.createGraphics();
        dashLightConfig.paint(gLightEventos);
        gLightEventos.dispose();
        ImageIO.write(imgLightEventos, "png", new File(artifactDir, "dashboard_eventos_light.png"));
        System.out.println("Guardado dashboard_eventos_light.png");

        // 4. CAPTURA: FrmDashboard en Modo Claro con Inicio (Dashboard)
        dashLightConfig.mostrarPanel("DASHBOARD");
        dashLightConfig.marcarBotonActivo(dashLightConfig.getBtnDashboard());
        Thread.sleep(800);

        BufferedImage imgLightInicio = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gLightInicio = imgLightInicio.createGraphics();
        dashLightConfig.paint(gLightInicio);
        gLightInicio.dispose();
        ImageIO.write(imgLightInicio, "png", new File(artifactDir, "dashboard_inicio_light.png"));
        System.out.println("Guardado dashboard_inicio_light.png");

        dashLightConfig.dispose();

        // Restaurar modo oscuro por defecto
        ThemeManager.setTema(true, null);

        System.out.println("TODAS LAS CAPTURAS COMPLETADAS CON ÉXITO.");
        System.exit(0);
    }
}

