package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * FrmLogin - Ventana de Autenticación y Registro con estética moderna,
 * fondo de cristal facetado (low-poly), tarjeta flotante con efecto glassmorphic,
 * campos con borde de foco naranja interactivo y logo retroiluminado.
 */
public class FrmLogin extends JFrame {

    // Componentes Login
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblIrARegistro;
    private JToggleButton btnMostrarPass;
    public JLabel lblVolverInicio;

    // Componentes Registro
    private JTextField txtRegUsername;
    private JTextField txtRegCorreo;
    private JComboBox<String> cbxRegRol;
    private JLabel lblRegCodigoAdmin;
    private JPasswordField txtRegCodigoAdmin;
    private JToggleButton btnRegMostrarCodigoAdmin;
    private JPasswordField txtRegPassword;
    private JPasswordField txtRegConfirmPassword;
    private JButton btnRegistrar;
    private JLabel lblIrALogin;
    public JLabel lblRegVolverInicio;
    private JToggleButton btnRegMostrarPass;
    private JToggleButton btnRegMostrarConfirmPass;
    private JLabel lblRegRequisitosPass;

    // Layout y Navegación
    private JPanel panelDerecho;
    private CardLayout cardLayout;

    // Paleta de Colores
    private static final Color COLOR_SIDEBAR = new Color(20, 20, 24);
    private static final Color COLOR_NARANJA = new Color(255, 117, 31);
    private static final Color COLOR_NARANJA_HOVER = new Color(255, 142, 60);
    private static final Color COLOR_NARANJA_PRESS = new Color(230, 85, 15);
    private static final Color COLOR_TEXTO_MUTED = new Color(160, 160, 175);
    private static final Color COLOR_TEXTO_TITULO = Color.WHITE;
    private static final Color COLOR_ENLACE_AZUL = new Color(114, 150, 212);
    private static final Color COLOR_ENLACE_AZUL_HOVER = new Color(162, 192, 248);

    // Imágenes
    private Image imgLogo;
    private BufferedImage imgFondoPoligonal;

    public FrmLogin() {
        cargarRecursos();
        initComponents();
    }

    private void cargarRecursos() {
        try {
            java.net.URL logoUrl = getClass().getResource("/img/LOGO_SGE.png");
            if (logoUrl != null) {
                imgLogo = new ImageIcon(logoUrl).getImage();
                setIconImage(imgLogo);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
        }

        try {
            java.net.URL bgUrl = getClass().getResource("/img/login_bg.png");
            if (bgUrl != null) {
                imgFondoPoligonal = ImageIO.read(bgUrl);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el fondo low-poly: " + e.getMessage());
        }
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Eventos");
        setSize(1024, 620);
        setMinimumSize(new Dimension(860, 560));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Izquierdo: Sidebar de Bienvenida
        JPanel panelIzquierdo = crearPanelIzquierdo();
        add(panelIzquierdo, BorderLayout.WEST);

        // Panel Derecho: Contenedor con Fondo Poligonal y Tarjeta Flotante
        cardLayout = new CardLayout();
        panelDerecho = new FondoPoligonalPanel(cardLayout);

        // Card 1: LOGIN
        JPanel wrapperLogin = new JPanel(new GridBagLayout());
        wrapperLogin.setOpaque(false);
        JPanel cardLogin = crearTarjetaLogin();
        wrapperLogin.add(cardLogin);

        // Card 2: REGISTRO
        JPanel wrapperRegistro = new JPanel(new GridBagLayout());
        wrapperRegistro.setOpaque(false);
        JPanel cardRegistro = crearTarjetaRegistro();
        wrapperRegistro.add(cardRegistro);

        panelDerecho.add(wrapperLogin, "LOGIN");
        panelDerecho.add(wrapperRegistro, "REGISTRO");

        add(panelDerecho, BorderLayout.CENTER);
    }

    // =========================================================================
    // PANEL IZQUIERDO (SIDEBAR DE BIENVENIDA CON GLOW Y LOGO)
    // =========================================================================
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Fondo sólido mate
                g2.setColor(COLOR_SIDEBAR);
                g2.fillRect(0, 0, w, h);

                // Sutil borde vertical derecho
                g2.setColor(new Color(36, 36, 44));
                g2.drawLine(w - 1, 0, w - 1, h);

                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(270, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 0, 20);

        // Contenedor central vertical
        JPanel contenidoCentro = new JPanel();
        contenidoCentro.setLayout(new BoxLayout(contenidoCentro, BoxLayout.Y_AXIS));
        contenidoCentro.setOpaque(false);

        // Logo con Halo / Resplandor Naranja
        LogoGlowPanel logoPanel = new LogoGlowPanel(imgLogo, 160, 160);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoCentro.add(logoPanel);

        contenidoCentro.add(Box.createVerticalStrut(18));

        // Título "BIENVENIDO"
        JLabel lblBienvenido = new JLabel("BIENVENIDO");
        lblBienvenido.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblBienvenido.setForeground(COLOR_TEXTO_TITULO);
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoCentro.add(lblBienvenido);

        contenidoCentro.add(Box.createVerticalStrut(8));

        // Subtítulo elegante
        JLabel lblSubtitulo = new JLabel("<html><div style='text-align: center; color: #9A9AA6;'>Gestiona tus eventos de forma<br>sencilla</div></html>");
        lblSubtitulo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoCentro.add(lblSubtitulo);

        panel.add(contenidoCentro, gbc);
        return panel;
    }

    // =========================================================================
    // TARJETA DE LOGIN FLOTANTE (GLASSMORPHIC)
    // =========================================================================
    private JPanel crearTarjetaLogin() {
        GlassmorphicCard card = new GlassmorphicCard(380, 430);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Título "Iniciar Sesión"
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 25, 20, 25);
        card.add(lblTitulo, gbc);

        // Campo 1: Correo Electrónico
        JLabel lblUserLabel = new JLabel("Correo Electrónico");
        lblUserLabel.setForeground(COLOR_TEXTO_MUTED);
        lblUserLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 25, 4, 25);
        card.add(lblUserLabel, gbc);

        txtCorreo = new JTextField();
        InputContainer panelCorreo = new InputContainer(txtCorreo, null);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 25, 14, 25);
        card.add(panelCorreo, gbc);

        // Campo 2: Contraseña
        JLabel lblPassLabel = new JLabel("Contraseña");
        lblPassLabel.setForeground(COLOR_TEXTO_MUTED);
        lblPassLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 25, 4, 25);
        card.add(lblPassLabel, gbc);

        txtPassword = new JPasswordField();
        btnMostrarPass = crearBotonOjo(txtPassword);
        InputContainer panelPass = new InputContainer(txtPassword, btnMostrarPass);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 25, 22, 25);
        card.add(panelPass, gbc);

        // Botón INICIAR SESIÓN
        btnLogin = new ModernOrangeButton("INICIAR SESIÓN");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 25, 16, 25);
        card.add(btnLogin, gbc);

        // Enlace "¿No tienes cuenta? Regístrate aquí"
        lblIrARegistro = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblIrARegistro.setForeground(COLOR_TEXTO_MUTED);
        lblIrARegistro.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblIrARegistro.setHorizontalAlignment(SwingConstants.CENTER);
        lblIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(lblIrARegistro, gbc);

        // Enlace "← Volver al Inicio (Modo Invitado)"
        lblVolverInicio = new JLabel("← Volver al Inicio (Modo Invitado)");
        lblVolverInicio.setForeground(COLOR_ENLACE_AZUL);
        lblVolverInicio.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblVolverInicio.setHorizontalAlignment(SwingConstants.CENTER);
        lblVolverInicio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 25, 12, 25);
        card.add(lblVolverInicio, gbc);

        return card;
    }

    // =========================================================================
    // TARJETA DE REGISTRO FLOTANTE (GLASSMORPHIC)
    // =========================================================================
    private JPanel crearTarjetaRegistro() {
        GlassmorphicCard card = new GlassmorphicCard(440, 560);
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Título "Crear Cuenta"
        JLabel lblTitulo = new JLabel("Crear Cuenta");
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(6, 25, 12, 25);
        card.add(lblTitulo, gbc);

        // 1. Nombre de Usuario
        JLabel lblUserLabel = new JLabel("Nombre de Usuario");
        lblUserLabel.setForeground(COLOR_TEXTO_MUTED);
        lblUserLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblUserLabel, gbc);

        txtRegUsername = new JTextField();
        InputContainer panelUser = new InputContainer(txtRegUsername, null);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(panelUser, gbc);

        // 2. Correo Electrónico
        JLabel lblCorreoLabel = new JLabel("Correo Electrónico (ej. @gmail.com / @admin.com)");
        lblCorreoLabel.setForeground(COLOR_TEXTO_MUTED);
        lblCorreoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblCorreoLabel, gbc);

        txtRegCorreo = new JTextField();
        InputContainer panelRegCorreo = new InputContainer(txtRegCorreo, null);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(panelRegCorreo, gbc);

        // 3. Rol / Tipo de Cuenta
        JLabel lblRolLabel = new JLabel("Tipo de Cuenta");
        lblRolLabel.setForeground(COLOR_TEXTO_MUTED);
        lblRolLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblRolLabel, gbc);

        cbxRegRol = new JComboBox<>(new String[]{"Operador", "Administrador"});
        cbxRegRol.setBackground(new Color(28, 29, 36));
        cbxRegRol.setForeground(Color.WHITE);
        cbxRegRol.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        cbxRegRol.setPreferredSize(new Dimension(300, 36));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(cbxRegRol, gbc);

        // 4. Código de Autorización (solo Administrador)
        lblRegCodigoAdmin = new JLabel("Código de Autorización (Solo Administrador)");
        lblRegCodigoAdmin.setForeground(COLOR_TEXTO_MUTED);
        lblRegCodigoAdmin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblRegCodigoAdmin, gbc);

        txtRegCodigoAdmin = new JPasswordField();
        txtRegCodigoAdmin.setEnabled(false);
        btnRegMostrarCodigoAdmin = crearBotonOjo(txtRegCodigoAdmin);
        InputContainer panelCodigoAdmin = new InputContainer(txtRegCodigoAdmin, btnRegMostrarCodigoAdmin);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(panelCodigoAdmin, gbc);

        cbxRegRol.addActionListener(e -> {
            boolean esAdmin = "Administrador".equals(cbxRegRol.getSelectedItem());
            txtRegCodigoAdmin.setEnabled(esAdmin);
            if (esAdmin) {
                lblRegCodigoAdmin.setText("Código de Autorización de Administrador (*Requerido)");
                lblRegCodigoAdmin.setForeground(COLOR_NARANJA);
            } else {
                txtRegCodigoAdmin.setText("");
                lblRegCodigoAdmin.setText("Código de Autorización (Solo Administrador)");
                lblRegCodigoAdmin.setForeground(COLOR_TEXTO_MUTED);
            }
        });

        // 5. Contraseña
        JLabel lblPassLabel = new JLabel("Contraseña");
        lblPassLabel.setForeground(COLOR_TEXTO_MUTED);
        lblPassLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblPassLabel, gbc);

        txtRegPassword = new JPasswordField();
        btnRegMostrarPass = crearBotonOjo(txtRegPassword);
        InputContainer panelRegPass = new InputContainer(txtRegPassword, btnRegMostrarPass);
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 25, 3, 25);
        card.add(panelRegPass, gbc);

        // Requisitos de contraseña dinámicos
        lblRegRequisitosPass = new JLabel("");
        lblRegRequisitosPass.setForeground(COLOR_TEXTO_MUTED);
        lblRegRequisitosPass.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 25, 6, 25);
        card.add(lblRegRequisitosPass, gbc);

        // 6. Confirmar Contraseña
        JLabel lblConfirmPassLabel = new JLabel("Confirmar Contraseña");
        lblConfirmPassLabel.setForeground(COLOR_TEXTO_MUTED);
        lblConfirmPassLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 25, 2, 25);
        card.add(lblConfirmPassLabel, gbc);

        txtRegConfirmPassword = new JPasswordField();
        btnRegMostrarConfirmPass = crearBotonOjo(txtRegConfirmPassword);
        InputContainer panelRegConfirmPass = new InputContainer(txtRegConfirmPassword, btnRegMostrarConfirmPass);
        gbc.gridy = 13;
        gbc.insets = new Insets(0, 25, 14, 25);
        card.add(panelRegConfirmPass, gbc);

        // Botón REGISTRARSE
        btnRegistrar = new ModernOrangeButton("REGISTRARSE");
        gbc.gridy = 14;
        gbc.insets = new Insets(0, 25, 12, 25);
        card.add(btnRegistrar, gbc);

        // Enlace "¿Ya tienes cuenta? Inicia sesión"
        lblIrALogin = new JLabel("¿Ya tienes cuenta? Inicia sesión");
        lblIrALogin.setForeground(COLOR_TEXTO_MUTED);
        lblIrALogin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblIrALogin.setHorizontalAlignment(SwingConstants.CENTER);
        lblIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 15;
        gbc.insets = new Insets(0, 25, 8, 25);
        card.add(lblIrALogin, gbc);

        return card;
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE COMPONENTES INTERACTIVOS
    // =========================================================================
    private JToggleButton crearBotonOjo(JPasswordField passField) {
        JToggleButton btn = new JToggleButton("👁");
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setForeground(new Color(170, 170, 185));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Mostrar/Ocultar contraseña");

        btn.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passField.setEchoChar((char) 0);
                btn.setText("🔒");
                btn.setForeground(COLOR_NARANJA);
            } else {
                passField.setEchoChar('•');
                btn.setText("👁");
                btn.setForeground(new Color(170, 170, 185));
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.isSelected()) btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.isSelected()) btn.setForeground(new Color(170, 170, 185));
            }
        });

        return btn;
    }

    // =========================================================================
    // CLASES INTERNAS PERSONALIZADAS: ESTÉTICA GLASSMORPHIC Y ANIMACIONES
    // =========================================================================

    /**
     * Panel contenedor que dibuja el fondo poligonal facetado (low-poly wallpaper)
     * adaptándolo de forma responsiva al tamaño disponible del panel derecho.
     */
    private class FondoPoligonalPanel extends JPanel {
        public FondoPoligonalPanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int pw = getWidth();
            int ph = getHeight();

            if (imgFondoPoligonal != null) {
                g2.drawImage(imgFondoPoligonal, 0, 0, pw, ph, null);
            } else {
                // Fallback a gradiente slate oscuro si la imagen no está disponible
                GradientPaint gp = new GradientPaint(0, 0, new Color(34, 35, 42), pw, ph, new Color(18, 18, 24));
                g2.setPaint(gp);
                g2.fillRect(0, 0, pw, ph);
            }

            g2.dispose();
        }
    }

    /**
     * Tarjeta flotante con esquinas redondeadas, sombra ambiental multicapa,
     * fondo slate acrílico y borde sutil iluminado.
     */
    private static class GlassmorphicCard extends JPanel {
        private final int prefW;
        private final int prefH;

        public GlassmorphicCard(int w, int h) {
            this.prefW = w;
            this.prefH = h;
            setOpaque(false);
            setPreferredSize(new Dimension(prefW, prefH));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = 20;

            // 1. Sombra suave multicapa (efecto elevación)
            for (int i = 1; i <= 6; i++) {
                int alpha = Math.max(0, 16 - (i * 2));
                g2.setColor(new Color(0, 0, 0, alpha));
                g2.drawRoundRect(6 - i, 6 - i + 2, w - (6 - i) * 2, h - (6 - i) * 2, arc + i * 2, arc + i * 2);
            }

            // 2. Fondo de cristal oscuro acrílico con degradado suave
            GradientPaint cardGrad = new GradientPaint(
                    0, 6, new Color(52, 53, 60, 235),
                    0, h - 6, new Color(36, 36, 42, 245)
            );
            g2.setPaint(cardGrad);
            g2.fillRoundRect(6, 6, w - 12, h - 12, arc, arc);

            // 3. Borde perimetral fino reflectante
            g2.setColor(new Color(78, 80, 92, 180));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(6, 6, w - 12, h - 12, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Contenedor para inputs (JTextField / JPasswordField) con esquinas redondeadas,
     * fondo grafito oscuro y borde con iluminación naranja interactiva al hacer foco.
     */
    private static class InputContainer extends JPanel {
        private boolean isFocused = false;
        private boolean isHovered = false;

        public InputContainer(JComponent mainInput, JComponent trailingComponent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(300, 38));
            setBorder(new EmptyBorder(2, 10, 2, 8));

            mainInput.setOpaque(false);
            mainInput.setForeground(Color.WHITE);
            mainInput.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
            mainInput.setBorder(null);

            if (mainInput instanceof JTextField) {
                ((JTextField) mainInput).setCaretColor(Color.WHITE);
            }

            mainInput.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    repaint();
                }
                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });

            add(mainInput, BorderLayout.CENTER);
            if (trailingComponent != null) {
                add(trailingComponent, BorderLayout.EAST);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = 10;

            // Fondo interior grafito oscuro
            g2.setColor(new Color(25, 26, 32));
            g2.fillRoundRect(1, 1, w - 2, h - 2, arc, arc);

            // Borde con efecto foco interactivo naranja
            if (isFocused) {
                g2.setColor(COLOR_NARANJA);
                g2.setStroke(new BasicStroke(1.5f));
            } else if (isHovered) {
                g2.setColor(new Color(88, 90, 105));
                g2.setStroke(new BasicStroke(1.0f));
            } else {
                g2.setColor(new Color(54, 55, 66));
                g2.setStroke(new BasicStroke(1.0f));
            }

            g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Botón Primario Naranja con degradado vibrante, esquinas redondeadas
     * y micro-interacción hover con resplandor.
     */
    private static class ModernOrangeButton extends JButton {
        private boolean isHover = false;
        private boolean isPress = false;

        public ModernOrangeButton(String text) {
            super(text);
            setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(300, 42));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    isPress = false;
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    isPress = true;
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    isPress = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = 10;

            Color cTop, cBottom;
            if (isPress) {
                cTop = COLOR_NARANJA_PRESS;
                cBottom = new Color(200, 70, 10);
            } else if (isHover) {
                cTop = COLOR_NARANJA_HOVER;
                cBottom = COLOR_NARANJA;
            } else {
                cTop = COLOR_NARANJA;
                cBottom = new Color(238, 92, 14);
            }

            GradientPaint gp = new GradientPaint(0, 0, cTop, 0, h, cBottom);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Borde sutil superior para simular relieve 3D
            g2.setColor(new Color(255, 255, 255, isHover ? 60 : 35));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            // Texto centrado
            FontMetrics fm = g2.getFontMetrics(getFont());
            int textX = (w - fm.stringWidth(getText())) / 2;
            int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(Color.WHITE);
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }
    }

    /**
     * Panel que renderiza el logo con un halo radial de resplandor naranja
     * en el fondo, creando el efecto retroiluminado.
     */
    private static class LogoGlowPanel extends JPanel {
        private final Image logo;
        private final int logoW;
        private final int logoH;

        public LogoGlowPanel(Image logo, int w, int h) {
            this.logo = logo;
            this.logoW = w;
            this.logoH = h;
            setOpaque(false);
            setPreferredSize(new Dimension(w + 30, h + 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            // Halo radial cálido naranja
            int glowRadius = (int) (logoW * 0.72);
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Float(cx, cy),
                    glowRadius,
                    new float[]{0.0f, 0.45f, 1.0f},
                    new Color[]{
                        new Color(255, 117, 31, 75),
                        new Color(255, 117, 31, 25),
                        new Color(255, 117, 31, 0)
                    }
            );
            g2.setPaint(rgp);
            g2.fillOval(cx - glowRadius, cy - glowRadius, glowRadius * 2, glowRadius * 2);

            // Dibujar el icono del logo centrado
            if (logo != null) {
                int imgX = cx - (logoW / 2);
                int imgY = cy - (logoH / 2);
                g2.drawImage(logo, imgX, imgY, logoW, logoH, null);
            }

            g2.dispose();
        }
    }

    // =========================================================================
    // API PÚBLICA PARA EL CONTROLADOR (COMPATIBILIDAD 100%)
    // =========================================================================
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
    public String getCorreo() {
        return txtCorreo.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JLabel getLblIrARegistro() {
        return lblIrARegistro;
    }

    // Getters Registro
    public String getRegUsername() {
        return txtRegUsername.getText().trim();
    }

    public String getRegCorreo() {
        return txtRegCorreo.getText().trim();
    }

    public String getRegRol() {
        return cbxRegRol.getSelectedItem() != null ? cbxRegRol.getSelectedItem().toString() : "Operador";
    }

    public String getRegPassword() {
        return new String(txtRegPassword.getPassword());
    }

    public String getRegConfirmPassword() {
        return new String(txtRegConfirmPassword.getPassword());
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JLabel getLblIrALogin() {
        return lblIrALogin;
    }

    public JLabel getLblRegRequisitosPass() {
        return lblRegRequisitosPass;
    }

    public void setRegRequisitosPassText(String text) {
        lblRegRequisitosPass.setText(text);
    }

    public JPasswordField getTxtRegPassword() {
        return txtRegPassword;
    }

    public String getRegCodigoAutorizacion() {
        return new String(txtRegCodigoAdmin.getPassword());
    }
}
