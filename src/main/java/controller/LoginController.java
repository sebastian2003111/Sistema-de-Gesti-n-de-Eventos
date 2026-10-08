package controller;

import model.Usuario;
import model.UsuarioService;
import view.FrmLogin;
import view.FrmEvento;
import model.EventoService;
import model.IEventoRepository;
import model.EventoRepository;

public class LoginController {

    private final FrmLogin vistaLogin;
    private final UsuarioService servicioUsuario;

    public LoginController(FrmLogin vista, UsuarioService servicio) {
        this.vistaLogin = vista;
        this.servicioUsuario = servicio;

        // Login Actions
        this.vistaLogin.getBtnLogin().addActionListener(e -> autenticar());
        this.vistaLogin.getLblIrARegistro().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { vistaLogin.mostrarPanel("REGISTRO"); }
            public void mouseEntered(java.awt.event.MouseEvent e) { vistaLogin.getLblIrARegistro().setForeground(new java.awt.Color(255, 117, 31)); }
            public void mouseExited(java.awt.event.MouseEvent e) { vistaLogin.getLblIrARegistro().setForeground(new java.awt.Color(150, 150, 150)); }
        });
        
        // Volver al Inicio (Modo Invitado)
        this.vistaLogin.lblVolverInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                vistaLogin.dispose();
                iniciarAppPrincipal(null); // null = Modo Invitado
            }
            public void mouseEntered(java.awt.event.MouseEvent e) { vistaLogin.lblVolverInicio.setForeground(new java.awt.Color(255, 117, 31)); }
            public void mouseExited(java.awt.event.MouseEvent e) { vistaLogin.lblVolverInicio.setForeground(new java.awt.Color(100, 150, 255)); }
        });

        // Register Actions
        this.vistaLogin.getBtnRegistrar().addActionListener(e -> registrarNuevoUsuario());
        this.vistaLogin.getLblIrALogin().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { vistaLogin.mostrarPanel("LOGIN"); }
            public void mouseEntered(java.awt.event.MouseEvent e) { vistaLogin.getLblIrALogin().setForeground(new java.awt.Color(255, 117, 31)); }
            public void mouseExited(java.awt.event.MouseEvent e) { vistaLogin.getLblIrALogin().setForeground(new java.awt.Color(150, 150, 150)); }
        });
        
        // Listener dinámico para los requisitos de contraseña
        this.vistaLogin.getTxtRegPassword().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarRequisitos(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarRequisitos(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarRequisitos(); }
        });
    }
    
    private void actualizarRequisitos() {
        String pass = vistaLogin.getRegPassword();
        boolean length = pass.length() >= 8;
        boolean upper = pass.matches(".*[A-Z].*");
        boolean lower = pass.matches(".*[a-z].*");
        boolean number = pass.matches(".*[0-9].*");
        boolean special = pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        String colorValid = "green";
        String colorInvalid = "gray";
        
        String html = "<html>Requisitos: "
                + "<font color='" + (length ? colorValid : colorInvalid) + "'>8+ caracteres</font>, "
                + "<font color='" + (upper ? colorValid : colorInvalid) + "'>Mayúscula</font>, "
                + "<font color='" + (lower ? colorValid : colorInvalid) + "'>Minúscula</font>, "
                + "<font color='" + (number ? colorValid : colorInvalid) + "'>Número</font>, "
                + "<font color='" + (special ? colorValid : colorInvalid) + "'>Símbolo</font></html>";
        vistaLogin.setRegRequisitosPassText(html);
    }

    public void iniciar() {
        vistaLogin.setVisible(true);
    }

    private void registrarNuevoUsuario() {
        try {
            String username = vistaLogin.getRegUsername();
            String correo = vistaLogin.getRegCorreo();
            String password = vistaLogin.getRegPassword();
            String confirmPass = vistaLogin.getRegConfirmPassword();
            String rol = vistaLogin.getRegRol();

            if (!password.equals(confirmPass)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden. Por favor, verifícalas.");
            }
            
            servicioUsuario.registrarUsuario(username, correo, password, rol);
            vistaLogin.mostrarMensajeExito("Cuenta de " + rol + " creada con éxito. Ahora puedes iniciar sesión.");
            
            // Limpiar y volver al login
            vistaLogin.mostrarPanel("LOGIN");
            
        } catch (IllegalArgumentException ex) {
            vistaLogin.mostrarMensajeError(ex.getMessage());
        }
    }

    private void autenticar() {
        try {
            String correo = vistaLogin.getCorreo();
            String password = vistaLogin.getPassword();

            Usuario usuarioAutenticado = servicioUsuario.login(correo, password);

            javax.swing.JOptionPane.showMessageDialog(vistaLogin,
                    "¡Bienvenido " + usuarioAutenticado.getRol() + "!",
                    "Acceso Concedido",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            vistaLogin.dispose(); // Cierra la ventana de login
            iniciarAppPrincipal(usuarioAutenticado);

        } catch (IllegalArgumentException ex) {
            vistaLogin.mostrarMensajeError(ex.getMessage());
        }
    }

    private void iniciarAppPrincipal(Usuario usuario) {
        view.FrmDashboard vistaDashboard = new view.FrmDashboard();
        DashboardController dashboardController = new DashboardController(vistaDashboard, usuario);
        // DashboardController internamente se hace visible y carga los módulos
    }
}
