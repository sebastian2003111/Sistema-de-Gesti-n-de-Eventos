package config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * ThemeManager - Gestor Centralizado de Temas Visuales (Oscuro Moderno y Claro Premium)
 * Permite alternar dinámicamente el Look & Feel de toda la aplicación sin reiniciar,
 * notificando a todas las vistas registradas para que adapten sus paletas de color,
 * tarjetas, bordes, tipografías y contrastes de manera impecable.
 */
public class ThemeManager {

    public interface ThemeListener {
        void aplicarTema(boolean isDark);
    }

    private static boolean darkTheme = true;
    private static final List<ThemeListener> listeners = new ArrayList<>();

    public static boolean isDark() {
        return darkTheme;
    }

    public static void registrarListener(ThemeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void desregistrarListener(ThemeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Aplica el cambio de tema en todo el ecosistema de la aplicación.
     */
    public static void setTema(boolean isDark, Window window) {
        darkTheme = isDark;

        try {
            if (isDark) {
                com.formdev.flatlaf.FlatDarkLaf.setup();
            } else {
                com.formdev.flatlaf.FlatLightLaf.setup();
            }

            // Propiedades base del Look and Feel
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.background", getColorNaranja());
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("defaultFont", new Font("Segoe UI Emoji", Font.PLAIN, 13));

        } catch (Exception ex) {
            System.err.println("Error al aplicar LookAndFeel de FlatLaf: " + ex.getMessage());
        }

        // Notificar a todos los módulos y componentes suscritos
        List<ThemeListener> copiaListeners = new ArrayList<>(listeners);
        for (ThemeListener l : copiaListeners) {
            try {
                l.aplicarTema(isDark);
            } catch (Exception ex) {
                System.err.println("Error notificando cambio de tema a listener: " + ex.getMessage());
            }
        }

        if (window != null) {
            SwingUtilities.updateComponentTreeUI(window);
            window.repaint();
        }
    }

    // =========================================================================
    // PALETA DE COLORES DINÁMICA
    // =========================================================================

    public static Color getColorNaranja() {
        return new Color(255, 117, 31);
    }

    public static Color getColorNaranjaHover() {
        return darkTheme ? new Color(255, 142, 60) : new Color(230, 95, 15);
    }

    public static Color getColorBg() {
        return darkTheme ? new Color(24, 24, 30) : new Color(245, 247, 250);
    }

    public static Color getColorSidebarBg() {
        return darkTheme ? new Color(18, 18, 22) : new Color(255, 255, 255);
    }

    public static Color getColorHeaderBg() {
        return darkTheme ? new Color(22, 22, 28) : new Color(255, 255, 255);
    }

    public static Color getColorCardBg() {
        return darkTheme ? new Color(30, 30, 38) : new Color(255, 255, 255);
    }

    public static Color getColorCardAlt() {
        return darkTheme ? new Color(36, 36, 46) : new Color(248, 250, 252);
    }

    public static Color getColorBorde() {
        return darkTheme ? new Color(45, 45, 56) : new Color(226, 232, 240);
    }

    public static Color getColorBordeSuave() {
        return darkTheme ? new Color(38, 38, 48) : new Color(235, 239, 245);
    }

    public static Color getColorInputBg() {
        return darkTheme ? new Color(22, 22, 28) : new Color(255, 255, 255);
    }

    public static Color getColorInputBorder() {
        return darkTheme ? new Color(55, 56, 68) : new Color(203, 213, 225);
    }

    public static Color getColorTextoTitulo() {
        return darkTheme ? new Color(245, 245, 250) : new Color(15, 23, 42);
    }

    public static Color getColorTextoNormal() {
        return darkTheme ? new Color(220, 224, 235) : new Color(30, 41, 59);
    }

    public static Color getColorTextoMuted() {
        return darkTheme ? new Color(155, 160, 175) : new Color(100, 116, 139);
    }

    public static Color getColorChipBg() {
        return darkTheme ? new Color(30, 30, 38) : new Color(241, 245, 249);
    }

    public static Color getColorTablaHeaderBg() {
        return darkTheme ? new Color(32, 32, 42) : new Color(241, 245, 249);
    }

    public static Color getColorTablaGrid() {
        return darkTheme ? new Color(40, 40, 52) : new Color(230, 235, 242);
    }
}

