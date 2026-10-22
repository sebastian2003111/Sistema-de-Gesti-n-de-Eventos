package controller;

import model.Usuario;
import view.PnlConfiguracion;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.awt.Font;

public class ConfiguracionController {
    private final PnlConfiguracion vista;
    private final Usuario usuarioActual;
    private final model.UsuarioService usuarioService;

    public ConfiguracionController(PnlConfiguracion vista, Usuario usuarioActual) {
        this.vista = vista;
        this.usuarioActual = usuarioActual;
        this.usuarioService = new model.UsuarioService(model.UsuarioRepository.getInstance());

        // Cargar datos de Perfil
        if (usuarioActual != null) {
            vista.getLblNombre().setText(usuarioActual.getUsername());
            if (vista.getLblRolBadge() != null) {
                vista.getLblRolBadge().setText(" " + usuarioActual.getRol() + " ");
            }
            vista.getLblCorreo().setText("Correo: " + usuarioActual.getCorreo());
        } else {
            vista.getLblNombre().setText("Invitado");
            if (vista.getLblRolBadge() != null) {
                vista.getLblRolBadge().setText(" Invitado ");
            }
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
        vista.getBtnBackup().addActionListener(e -> crearBackup());
        vista.getBtnRestaurar().addActionListener(e -> restaurarBackup());
        
        vista.getChkActivarNotificaciones().addActionListener(e -> {
            String estado = vista.getChkActivarNotificaciones().isSelected() ? "activadas" : "desactivadas";
            System.out.println("Notificaciones globales " + estado);
        });

        // Cargar y vincular configuración SMTP
        config.EmailConfig emailCfg = config.EmailConfig.getInstance();
        vista.getTxtSmtpRemitente().setText(emailCfg.getRemitente());
        vista.getTxtSmtpPassword().setText(emailCfg.getPassword());
        vista.getChkSmtpHabilitado().setSelected(emailCfg.isHabilitado());
        vista.getBtnGuardarSmtp().addActionListener(e -> guardarConfigSmtp());
    }

    private void guardarConfigSmtp() {
        if (usuarioActual == null || !usuarioActual.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(vista, "Solo los administradores pueden modificar la configuración de correo.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String remitente = vista.getTxtSmtpRemitente().getText().trim();
        String pass = new String(vista.getTxtSmtpPassword().getPassword()).trim();
        boolean habilitado = vista.getChkSmtpHabilitado().isSelected();

        config.EmailConfig.getInstance().guardar("smtp.gmail.com", 587, remitente, pass, habilitado);
        JOptionPane.showMessageDialog(vista, "¡Configuración de correo SMTP guardada con éxito!", "Configuración Actualizada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarPassword() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(vista, "Debes iniciar sesión para cambiar la contraseña.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String actual = new String(vista.getTxtActualPass().getPassword());
        String nueva = new String(vista.getTxtNuevaPass().getPassword());
        String confirmar = new String(vista.getTxtConfirmarPass().getPassword());

        try {
            // Validar y persistir en la base de datos SQLite con verificación de clave actual
            usuarioService.cambiarPasswordConVerificacion(usuarioActual.getCorreo(), actual, nueva, confirmar);
            
            // Actualizar el estado del objeto en memoria y limpiar campos
            usuarioActual.setPassword(nueva);
            vista.getTxtActualPass().setText("");
            vista.getTxtNuevaPass().setText("");
            vista.getTxtConfirmarPass().setText("");
            
            JOptionPane.showMessageDialog(vista, "¡Contraseña actualizada con éxito en la base de datos!", "Seguridad Actualizada", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Validación de Contraseña", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar la contraseña: " + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarTema(boolean oscuro) {
        config.ThemeManager.setTema(oscuro, SwingUtilities.getWindowAncestor(vista));
    }

    private void cambiarTamanoInterfaz() {
        int index = vista.getCbxTamanoInterfaz().getSelectedIndex();
        int baseSize = 14; // Normal
        if (index == 0) baseSize = 12; // Pequeño
        if (index == 2) baseSize = 16; // Grande
        
        UIManager.put("defaultFont", new Font("Segoe UI Emoji", Font.PLAIN, baseSize));
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(vista));
    }

    private void limpiarDatos() {
        if (usuarioActual == null || !usuarioActual.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(vista, "Solo los administradores pueden borrar datos del sistema.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int resp = JOptionPane.showConfirmDialog(vista, 
                "¿Estás absolutamente seguro de limpiar todos los datos del sistema?\nEsta acción no se puede deshacer.", 
                "Peligro", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
                
        if (resp == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(vista, "Para realizar una limpieza segura, puede crear una copia de respaldo y reiniciar la base de datos.");
        }
    }

    private void crearBackup() {
        if (usuarioActual == null || !usuarioActual.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(vista, "Solo los administradores pueden generar copias de seguridad.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.io.File dbOriginal = new java.io.File("gestion_eventos.db");
        if (!dbOriginal.exists()) {
            JOptionPane.showMessageDialog(vista, "No se encontró el archivo de base de datos actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Copia de Seguridad de la Base de Datos");
        String fecha = java.time.LocalDate.now().toString();
        fileChooser.setSelectedFile(new java.io.File("backup_gestion_eventos_" + fecha + ".db"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Base de datos SQLite (*.db)", "db"));

        if (fileChooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            java.io.File destino = fileChooser.getSelectedFile();
            if (!destino.getName().toLowerCase().endsWith(".db")) {
                destino = new java.io.File(destino.getParentFile(), destino.getName() + ".db");
            }
            try {
                java.nio.file.Files.copy(dbOriginal.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(vista, "¡Copia de seguridad creada con éxito!\nGuardada en:\n" + destino.getAbsolutePath(), "Respaldo Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al generar la copia de seguridad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restaurarBackup() {
        if (usuarioActual == null || !usuarioActual.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(vista, "Solo los administradores pueden restaurar copias de seguridad.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int advertencia = JOptionPane.showConfirmDialog(vista,
                "ADVERTENCIA: Restaurar una copia de seguridad reemplazará toda la información actual de la base de datos.\n¿Desea continuar?",
                "Restaurar Base de Datos",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (advertencia != JOptionPane.YES_OPTION) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Copia de Seguridad (.db)");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Base de datos SQLite (*.db)", "db"));

        if (fileChooser.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
            java.io.File respaldo = fileChooser.getSelectedFile();
            java.io.File dbOriginal = new java.io.File("gestion_eventos.db");

            try {
                // Cerrar la conexión abierta para liberar el archivo antes de reemplazar
                java.sql.Connection conn = config.DatabaseConnection.getInstance();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }

                java.nio.file.Files.copy(respaldo.toPath(), dbOriginal.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                JOptionPane.showMessageDialog(vista,
                        "¡Base de datos restaurada con éxito!\nSe recomienda reiniciar el aplicativo para reflejar todos los cambios.",
                        "Restauración Completada",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al restaurar la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
