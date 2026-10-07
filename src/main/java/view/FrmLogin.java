package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class FrmLogin extends JFrame {

    // Componentes Login
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblIrARegistro;
    private JToggleButton btnMostrarPass;

    // Componentes Registro
    private JTextField txtRegUsername;
    private JTextField txtRegCorreo;
    private JComboBox<String> cbxRegRol;
    private JPasswordField txtRegPassword;
    private JPasswordField txtRegConfirmPassword;
    private JButton btnRegistrar;
    private JLabel lblIrALogin;
    private JToggleButton btnRegMostrarPass;
    private JToggleButton btnRegMostrarConfirmPass;
    private JLabel lblRegRequisitosPass;

    // Layout
    private JPanel panelDerecho;
    private CardLayout cardLayout;

    // Colores base
    private final Color colorFondoDerecho = new Color(15, 15, 15);
    private final Color colorFondoIzquierdo = new Color(30, 30, 30);
    private final Color colorNaranja = new Color(255, 117, 31);
    private final Color colorTextoGris = new Color(150, 150, 150);
    private final Border borderInput = new CompoundBorder(
            new LineBorder(colorNaranja, 1, true),
            new EmptyBorder(8, 10, 8, 10)
    );

    public FrmLogin() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Eventos");
        setSize(750, 600); // Tamaño base
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizado por defecto
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cambiar el icono de la ventana
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/Logo_negro2.png"));
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }

        JPanel panelIzquierdo = crearPanelIzquierdo();
        add(panelIzquierdo, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelDerecho = new JPanel(cardLayout);
        
        JPanel innerLogin = crearPanelLogin();
        JPanel outerLogin = new JPanel(new GridBagLayout());
        outerLogin.setBackground(colorFondoDerecho);
        innerLogin.setPreferredSize(new Dimension(450, 450));
        outerLogin.add(innerLogin);

        JPanel innerRegistro = crearPanelRegistro();
        JPanel outerRegistro = new JPanel(new GridBagLayout());
        outerRegistro.setBackground(colorFondoDerecho);
        innerRegistro.setPreferredSize(new Dimension(450, 550));
        outerRegistro.add(innerRegistro);
        
        panelDerecho.add(outerLogin, "LOGIN");
        panelDerecho.add(outerRegistro, "REGISTRO");
        
        add(panelDerecho, BorderLayout.CENTER);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorFondoIzquierdo);
        panel.setPreferredSize(new Dimension(250, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);

        try {
            java.net.URL logoUrl = getClass().getResource("/img/Logo_negro2.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image img = logoIcon.getImage();
                int width = 200;
                int height = (int) (((double) width / img.getWidth(null)) * img.getHeight(null));
                Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); 
                JLabel lblLogo = new JLabel(new ImageIcon(newimg));
                panel.add(lblLogo, gbc);
            }
        } catch (Exception e) {}

        JLabel lblBienvenido = new JLabel("BIENVENIDO");
        lblBienvenido.setForeground(Color.WHITE);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridy = 1;
        panel.add(lblBienvenido, gbc);
        return panel;
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorFondoDerecho);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(20, 40, 20, 40);
        panel.add(lblTitulo, gbc);

        // Correo
        gbc.insets = new Insets(5, 60, 0, 60); gbc.gridy = 1;
        JLabel lblUserLabel = new JLabel("Correo Electrónico");
        lblUserLabel.setForeground(colorTextoGris);
        panel.add(lblUserLabel, gbc);

        gbc.insets = new Insets(2, 60, 10, 60); gbc.gridy = 2;
        txtCorreo = new JTextField(20);
        txtCorreo.setBackground(colorFondoDerecho);
        txtCorreo.setForeground(Color.WHITE);
        txtCorreo.setCaretColor(Color.WHITE);
        txtCorreo.setBorder(borderInput);
        panel.add(txtCorreo, gbc);

        // Contraseña
        gbc.insets = new Insets(5, 60, 0, 60); gbc.gridy = 3;
        JLabel lblPassLabel = new JLabel("Contraseña");
        lblPassLabel.setForeground(colorTextoGris);
        panel.add(lblPassLabel, gbc);

        JPanel panelPass = new JPanel(new BorderLayout());
        panelPass.setBackground(colorFondoDerecho);
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(colorFondoDerecho);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(borderInput);
        panelPass.add(txtPassword, BorderLayout.CENTER);

        btnMostrarPass = new JToggleButton("👁");
        btnMostrarPass.setBackground(colorFondoIzquierdo);
        btnMostrarPass.setForeground(Color.WHITE);
        btnMostrarPass.setFocusPainted(false);
        btnMostrarPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMostrarPass.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) txtPassword.setEchoChar((char) 0);
            else txtPassword.setEchoChar('•');
        });
        panelPass.add(btnMostrarPass, BorderLayout.EAST);

        gbc.insets = new Insets(2, 60, 20, 60); gbc.gridy = 4;
        panel.add(panelPass, gbc);

        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setBackground(colorNaranja);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(10, 10, 10, 10));
        gbc.gridy = 5; gbc.insets = new Insets(10, 60, 10, 60);
        panel.add(btnLogin, gbc);

        lblIrARegistro = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblIrARegistro.setForeground(colorTextoGris);
        lblIrARegistro.setHorizontalAlignment(SwingConstants.CENTER);
        lblIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6; gbc.insets = new Insets(0, 60, 20, 60);
        panel.add(lblIrARegistro, gbc);

        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorFondoDerecho);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        JLabel lblTitulo = new JLabel("Crear Cuenta");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 40, 10, 40);
        panel.add(lblTitulo, gbc);

        // Usuario
        gbc.insets = new Insets(0, 60, 0, 60); gbc.gridy = 1;
        JLabel lblUserLabel = new JLabel("Nombre de Usuario");
        lblUserLabel.setForeground(colorTextoGris);
        panel.add(lblUserLabel, gbc);

        gbc.insets = new Insets(2, 60, 5, 60); gbc.gridy = 2;
        txtRegUsername = new JTextField(20);
        txtRegUsername.setBackground(colorFondoDerecho);
        txtRegUsername.setForeground(Color.WHITE);
        txtRegUsername.setCaretColor(Color.WHITE);
        txtRegUsername.setBorder(borderInput);
        panel.add(txtRegUsername, gbc);
        
        // Correo
        gbc.insets = new Insets(0, 60, 0, 60); gbc.gridy = 3;
        JLabel lblCorreoLabel = new JLabel("Correo Electrónico (ej. @gmail.com / @admin.com)");
        lblCorreoLabel.setForeground(colorTextoGris);
        panel.add(lblCorreoLabel, gbc);

        gbc.insets = new Insets(2, 60, 5, 60); gbc.gridy = 4;
        txtRegCorreo = new JTextField(20);
        txtRegCorreo.setBackground(colorFondoDerecho);
        txtRegCorreo.setForeground(Color.WHITE);
        txtRegCorreo.setCaretColor(Color.WHITE);
        txtRegCorreo.setBorder(borderInput);
        panel.add(txtRegCorreo, gbc);
        
        // Rol
        gbc.insets = new Insets(0, 60, 0, 60); gbc.gridy = 5;
        JLabel lblRolLabel = new JLabel("Tipo de Cuenta");
        lblRolLabel.setForeground(colorTextoGris);
        panel.add(lblRolLabel, gbc);

        gbc.insets = new Insets(2, 60, 5, 60); gbc.gridy = 6;
        cbxRegRol = new JComboBox<>(new String[]{"Usuario", "Administrador"});
        panel.add(cbxRegRol, gbc);

        // Contraseña
        gbc.insets = new Insets(0, 60, 0, 60); gbc.gridy = 7;
        JLabel lblPassLabel = new JLabel("Contraseña");
        lblPassLabel.setForeground(colorTextoGris);
        panel.add(lblPassLabel, gbc);

        JPanel panelPass = new JPanel(new BorderLayout());
        panelPass.setBackground(colorFondoDerecho);
        txtRegPassword = new JPasswordField(20);
        txtRegPassword.setBackground(colorFondoDerecho);
        txtRegPassword.setForeground(Color.WHITE);
        txtRegPassword.setCaretColor(Color.WHITE);
        txtRegPassword.setBorder(borderInput);
        panelPass.add(txtRegPassword, BorderLayout.CENTER);

        btnRegMostrarPass = new JToggleButton("👁");
        btnRegMostrarPass.setBackground(colorFondoIzquierdo);
        btnRegMostrarPass.setForeground(Color.WHITE);
        btnRegMostrarPass.setFocusPainted(false);
        btnRegMostrarPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegMostrarPass.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) txtRegPassword.setEchoChar((char) 0);
            else txtRegPassword.setEchoChar('•');
        });
        panelPass.add(btnRegMostrarPass, BorderLayout.EAST);

        gbc.insets = new Insets(2, 60, 5, 60); gbc.gridy = 8;
        panel.add(panelPass, gbc);

        lblRegRequisitosPass = new JLabel("");
        lblRegRequisitosPass.setForeground(colorTextoGris);
        lblRegRequisitosPass.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        gbc.insets = new Insets(0, 60, 5, 60); gbc.gridy = 9;
        panel.add(lblRegRequisitosPass, gbc);

        // Confirmar Contraseña
        gbc.insets = new Insets(0, 60, 0, 60); gbc.gridy = 10;
        JLabel lblConfirmPassLabel = new JLabel("Confirmar Contraseña");
        lblConfirmPassLabel.setForeground(colorTextoGris);
        panel.add(lblConfirmPassLabel, gbc);

        JPanel panelConfirmPass = new JPanel(new BorderLayout());
        panelConfirmPass.setBackground(colorFondoDerecho);
        txtRegConfirmPassword = new JPasswordField(20);
        txtRegConfirmPassword.setBackground(colorFondoDerecho);
        txtRegConfirmPassword.setForeground(Color.WHITE);
        txtRegConfirmPassword.setCaretColor(Color.WHITE);
        txtRegConfirmPassword.setBorder(borderInput);
        panelConfirmPass.add(txtRegConfirmPassword, BorderLayout.CENTER);

        btnRegMostrarConfirmPass = new JToggleButton("👁");
        btnRegMostrarConfirmPass.setBackground(colorFondoIzquierdo);
        btnRegMostrarConfirmPass.setForeground(Color.WHITE);
        btnRegMostrarConfirmPass.setFocusPainted(false);
        btnRegMostrarConfirmPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegMostrarConfirmPass.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) txtRegConfirmPassword.setEchoChar((char) 0);
            else txtRegConfirmPassword.setEchoChar('•');
        });
        panelConfirmPass.add(btnRegMostrarConfirmPass, BorderLayout.EAST);

        gbc.insets = new Insets(2, 60, 10, 60); gbc.gridy = 11;
        panel.add(panelConfirmPass, gbc);

        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(colorNaranja);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(new EmptyBorder(10, 10, 10, 10));
        gbc.gridy = 12; gbc.insets = new Insets(5, 60, 5, 60);
        panel.add(btnRegistrar, gbc);

        lblIrALogin = new JLabel("¿Ya tienes cuenta? Inicia sesión");
        lblIrALogin.setForeground(colorTextoGris);
        lblIrALogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 13; gbc.insets = new Insets(0, 60, 10, 60);
        panel.add(lblIrALogin, gbc);

        return panel;
    }

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(panelDerecho, nombrePanel);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters Login
    public String getCorreo() { return txtCorreo.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public JLabel getLblIrARegistro() { return lblIrARegistro; }

    // Getters Registro
    public String getRegUsername() { return txtRegUsername.getText(); }
    public String getRegCorreo() { return txtRegCorreo.getText(); }
    public String getRegRol() { return cbxRegRol.getSelectedItem().toString(); }
    public String getRegPassword() { return new String(txtRegPassword.getPassword()); }
    public String getRegConfirmPassword() { return new String(txtRegConfirmPassword.getPassword()); }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JLabel getLblIrALogin() { return lblIrALogin; }
    public JLabel getLblRegRequisitosPass() { return lblRegRequisitosPass; }
    
    public void setRegRequisitosPassText(String text) {
        lblRegRequisitosPass.setText(text);
    }
    
    public JPasswordField getTxtRegPassword() {
        return txtRegPassword;
    }
}
