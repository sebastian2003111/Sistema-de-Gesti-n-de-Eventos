package controller;

import model.Usuario;
import view.PnlConfiguracion;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Font;

public class ConfiguracionController {
    private PnlConfiguracion vista;
    private Usuario usuarioActual;

    public ConfiguracionController(PnlConfiguracion vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;

        // Cargar datos de Perfil
        if (usuarioActual != null) {
            vista.getLblNombre().setText("Nombre/Usuario: " + usuarioActual.getUsername() + " (Rol: " + usuarioActual.getRol() + ")");
            vista.getLblCorreo().setText("Correo: " + usuarioActual.getCorreo());
        } else {
            vista.getLblNombre().setText("Nombre/Usuario: Invitado");
            vista.getLblCorreo().setText("Correo: No registrado");
        }

        // Listeners
        vista.getBtnActualizarPass().addActionListener(e -> actualizarPassword());
        vista.getRbDark().addActionListener(e -> cambiarTema(true));
        vista.getRbLight().addActionListener(e -> cambiarTema(false));
        vista.getCbxTamanoInterfaz().addActionListener(e -> cambiarTamanoInterfaz());
        
        vista.getBtnRecargarDatos().addActionListener(e -> {
            JOptionPane.showMessageDialog(vista, "Vistas recargadas correctamente.");
        });
        
        vista.getBtnLimpiarDatos().addActionListener(e -> limpiarDatos());
        
        vista.getChkActivarNotificaciones().addActionListener(e -> {
            String estado = vista.getChkActivarNotificaciones().isSelected() ? "activadas" : "desactivadas";
            System.out.println("Notificaciones globales " + estado);
        });
    }

    private void actualizarPassword() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para cambiar la contraseña.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nueva = new String(vista.getTxtNuevaPass().getPassword());
        if (nueva.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "La contraseña no puede estar vacía.");
            return;
        }
        
        if (nueva.length() < 8) {
            JOptionPane.showMessageDialog(vista, "La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        usuarioActual.setPassword(nueva);
        vista.getTxtNuevaPass().setText("");
        JOptionPane.showMessageDialog(vista, "¡Contraseña actualizada con éxito!");
    }

    private void cambiarTema(boolean oscuro) {
        try {
            if (oscuro) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(vista));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cambiarTamanoInterfaz() {
        int index = vista.getCbxTamanoInterfaz().getSelectedIndex();
        int baseSize = 14; // Normal
        if (index == 0) baseSize = 12; // Pequeño
        if (index == 2) baseSize = 16; // Grande
        
        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, baseSize));
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(vista));
    }

    private void limpiarDatos() {
        if (usuarioActual == null || !usuarioActual.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(vista, "Solo los administradores pueden borrar los datos en memoria.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int resp = JOptionPane.showConfirmDialog(vista, "¿Estás absolutamente seguro de limpiar todos los datos del sistema?", "Peligro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            // En este incremento solo simulamos la limpieza de la DB que vendrá en el inc. 7
            JOptionPane.showMessageDialog(vista, "Base de datos en memoria limpiada (Simulación).");
        }
    }
}
