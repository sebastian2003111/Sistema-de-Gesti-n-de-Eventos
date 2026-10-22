package view;

import config.ThemeManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * PnlConfiguracion - Centro de Configuración y Mantenimiento
 * Adaptable 100% dinámicamente a Tema Oscuro Moderno y Tema Claro Premium.
 * Incluye un módulo de perfil renovado con verificación de contraseña actual,
 * confirmación de nueva clave, alternador de visibilidad (ojito) y proporciones perfectas.
 */
public class PnlConfiguracion extends JPanel implements ThemeManager.ThemeListener {

    // --- Componentes de Perfil ---
    private JLabel lblNombre;
    private JLabel lblCorreo;
    private JLabel lblRolBadge;
    private JLabel lblEstadoCuenta;
    private JPasswordField txtActualPass;
    private JPasswordField txtNuevaPass;
    private JPasswordField txtConfirmarPass;
    private JButton btnToggleActual;
    private JButton btnToggleNueva;
    private JButton btnToggleConfirmar;
    private JButton btnActualizarPass;

    // --- Componentes de Apariencia ---
    private JRadioButton rbDark;
    private JRadioButton rbLight;
    private JComboBox<String> cbxTamanoInterfaz;

    // --- Componentes de Notificaciones y SMTP ---
    private JCheckBox chkActivarNotificaciones;
    private JCheckBox chkAvisosEventos;
    private JTextField txtSmtpRemitente;
    private JPasswordField txtSmtpPassword;
    private JCheckBox chkSmtpHabilitado;
    private JButton btnGuardarSmtp;

    // --- Componentes de Datos ---
    private JButton btnRecargarDatos;
    private JButton btnLimpiarDatos;
    private JButton btnBackup;
    private JButton btnRestaurar;

    // --- Referencias estructurales para re-theming dinámico ---
    private JPanel pnlHeader;
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JTabbedPane tabbedPane;
    private final List<JPanel> listaPanelesBase = new ArrayList<>();
    private final List<JPanel> listaWrappers = new ArrayList<>();
    private final List<JLabel> listaTitulosSeccion = new ArrayList<>();
    private final List<JLabel> listaLabelsNormales = new ArrayList<>();
    private final List<JTextField> listaTextFields = new ArrayList<>();
    private final List<JPasswordField> listaPasswordFields = new ArrayList<>();
    private final List<JRadioButton> listaRadioButtons = new ArrayList<>();
    private final List<JCheckBox> listaCheckBoxes = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();
    private JPanel cardPerfilInfo;
    private JPanel cardPerfilSecurity;

    public PnlConfiguracion() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(22, 28, 22, 28));

        // Título del módulo
        pnlHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlHeader.setOpaque(false);

        lblTitulo = new JLabel("⚙️  Centro de Configuración y Mantenimiento");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Personaliza la apariencia, administra credenciales, gestiona el servidor SMTP y realiza respaldos de base de datos.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlHeader.add(lblTitulo);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // JTabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        tabbedPane.addTab("👤  Perfil de Usuario", crearPanelPerfil());
        tabbedPane.addTab("🎨  Apariencia & Tema", crearPanelApariencia());
        tabbedPane.addTab("🔔  Notificaciones & SMTP", crearPanelNotificaciones());
        tabbedPane.addTab("📅  Reglas de Eventos", crearPanelEventos());
        tabbedPane.addTab("💾  Base de Datos & Backup", crearPanelDatos());
        tabbedPane.addTab("ℹ️  Acerca del Sistema", crearPanelAcerca());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelBase() {
        JPanel pnl = new JPanel(new GridBagLayout());
        listaPanelesBase.add(pnl);
        return pnl;
    }

    private JPanel crearContenedorTab(JPanel contenido) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(16, 10, 10, 10));
        wrapper.add(contenido, BorderLayout.NORTH);
        listaWrappers.add(wrapper);
        return wrapper;
    }

    private GridBagConstraints getDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 6, 0);
        return gbc;
    }

    // =========================================================================
    // 1. PANEL PERFIL (REDISEÑADO CON VERIFICACIÓN Y TAMAÑOS PROPORCIONADOS)
    // =========================================================================
    private JPanel crearPanelPerfil() {
        JPanel pnlPrincipal = new JPanel(new GridBagLayout());
        pnlPrincipal.setOpaque(false);
        GridBagConstraints gbcM = new GridBagConstraints();
        gbcM.gridx = 0;
        gbcM.gridy = 0;
        gbcM.weightx = 1.0;
        gbcM.fill = GridBagConstraints.HORIZONTAL;
        gbcM.insets = new Insets(0, 0, 16, 0);

        // --- TARJETA 1: INFORMACIÓN DEL USUARIO ---
        cardPerfilInfo = new JPanel(new BorderLayout(15, 0));
        cardPerfilInfo.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorBorde(), 1, true),
                new EmptyBorder(18, 22, 18, 22)
        ));

        JPanel pnlAvatar = new JPanel(new GridBagLayout());
        pnlAvatar.setOpaque(false);
        pnlAvatar.setPreferredSize(new Dimension(54, 54));
        JLabel lblAvatarIcon = new JLabel("👤");
        lblAvatarIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblAvatarIcon.setForeground(ThemeManager.getColorNaranja());
        pnlAvatar.add(lblAvatarIcon);
        cardPerfilInfo.add(pnlAvatar, BorderLayout.WEST);

        JPanel pnlDetallesUser = new JPanel(new GridLayout(3, 1, 0, 3));
        pnlDetallesUser.setOpaque(false);

        JPanel pnlFila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFila1.setOpaque(false);

        lblNombre = new JLabel("Cargando usuario...");
        lblNombre.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        lblNombre.setForeground(ThemeManager.getColorTextoTitulo());
        listaLabelsNormales.add(lblNombre);

        lblRolBadge = new JLabel(" Rol: Cargando ");
        lblRolBadge.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        lblRolBadge.setForeground(ThemeManager.getColorNaranja());
        lblRolBadge.setOpaque(true);
        lblRolBadge.setBackground(new Color(255, 117, 31, 28));
        lblRolBadge.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 117, 31, 80), 1, true),
                new EmptyBorder(2, 8, 2, 8)
        ));

        pnlFila1.add(lblNombre);
        pnlFila1.add(lblRolBadge);

        lblCorreo = new JLabel("Correo: Cargando...");
        lblCorreo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        lblCorreo.setForeground(ThemeManager.getColorTextoMuted());
        listaLabelsNormales.add(lblCorreo);

        lblEstadoCuenta = new JLabel("● Cuenta verificada • Cifrado SHA-256 con Salt criptográfico de 16 bytes");
        lblEstadoCuenta.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        lblEstadoCuenta.setForeground(new Color(46, 204, 113));

        pnlDetallesUser.add(pnlFila1);
        pnlDetallesUser.add(lblCorreo);
        pnlDetallesUser.add(lblEstadoCuenta);

        cardPerfilInfo.add(pnlDetallesUser, BorderLayout.CENTER);
        pnlPrincipal.add(cardPerfilInfo, gbcM);

        // --- TARJETA 2: SEGURIDAD Y CAMBIO DE CONTRASEÑA ---
        gbcM.gridy++;
        cardPerfilSecurity = new JPanel(new GridBagLayout());
        cardPerfilSecurity.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorBorde(), 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        GridBagConstraints gbcS = new GridBagConstraints();
        gbcS.gridx = 0;
        gbcS.gridy = 0;
        gbcS.anchor = GridBagConstraints.WEST;
        gbcS.insets = new Insets(0, 0, 4, 0);

        JLabel lblTitSec = crearTituloSeccion("🛡️  ACTUALIZAR CONTRASEÑA DE ACCESO");
        cardPerfilSecurity.add(lblTitSec, gbcS);

        gbcS.gridy++;
        gbcS.insets = new Insets(0, 0, 14, 0);
        JLabel lblSecSub = new JLabel("Para proteger la integridad de tu cuenta, ingresa tu clave actual antes de establecer la nueva contraseña.");
        lblSecSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSecSub.setForeground(ThemeManager.getColorTextoMuted());
        listaLabelsNormales.add(lblSecSub);
        cardPerfilSecurity.add(lblSecSub, gbcS);

        // Campo 1: Contraseña Actual
        gbcS.gridy++;
        gbcS.insets = new Insets(4, 0, 4, 0);
        cardPerfilSecurity.add(crearLabelNormal("Contraseña Actual: *"), gbcS);

        gbcS.gridy++;
        gbcS.insets = new Insets(0, 0, 10, 0);
        txtActualPass = new JPasswordField();
        btnToggleActual = crearBotonOjo(txtActualPass);
        JPanel pnlActualWrapper = crearPasswordWrapper(txtActualPass, btnToggleActual);
        cardPerfilSecurity.add(pnlActualWrapper, gbcS);

        // Campo 2: Nueva Contraseña
        gbcS.gridy++;
        gbcS.insets = new Insets(4, 0, 4, 0);
        cardPerfilSecurity.add(crearLabelNormal("Nueva Contraseña: *"), gbcS);

        gbcS.gridy++;
        gbcS.insets = new Insets(0, 0, 10, 0);
        txtNuevaPass = new JPasswordField();
        btnToggleNueva = crearBotonOjo(txtNuevaPass);
        JPanel pnlNuevaWrapper = crearPasswordWrapper(txtNuevaPass, btnToggleNueva);
        cardPerfilSecurity.add(pnlNuevaWrapper, gbcS);

        // Campo 3: Confirmar Nueva Contraseña
        gbcS.gridy++;
        gbcS.insets = new Insets(4, 0, 4, 0);
        cardPerfilSecurity.add(crearLabelNormal("Confirmar Nueva Contraseña: *"), gbcS);

        gbcS.gridy++;
        gbcS.insets = new Insets(0, 0, 12, 0);
        txtConfirmarPass = new JPasswordField();
        btnToggleConfirmar = crearBotonOjo(txtConfirmarPass);
        JPanel pnlConfWrapper = crearPasswordWrapper(txtConfirmarPass, btnToggleConfirmar);
        cardPerfilSecurity.add(pnlConfWrapper, gbcS);

        // Checklist de Seguridad
        gbcS.gridy++;
        gbcS.insets = new Insets(2, 0, 16, 0);
        JPanel pnlReqs = new JPanel(new GridLayout(2, 2, 12, 4));
        pnlReqs.setOpaque(false);
        pnlReqs.add(crearRequisitoLabel("• Mínimo 8 caracteres"));
        pnlReqs.add(crearRequisitoLabel("• Letra mayúscula y minúscula"));
        pnlReqs.add(crearRequisitoLabel("• Al menos un número (0-9)"));
        pnlReqs.add(crearRequisitoLabel("• Al menos un símbolo (!@#$%^&*)"));
        cardPerfilSecurity.add(pnlReqs, gbcS);

        // Botón Actualizar Contraseña
        gbcS.gridy++;
        gbcS.insets = new Insets(4, 0, 4, 0);
        btnActualizarPass = crearBotonAccion("🔒  Actualizar Contraseña", ThemeManager.getColorNaranja(), true);
        btnActualizarPass.setPreferredSize(new Dimension(210, 40));
        cardPerfilSecurity.add(btnActualizarPass, gbcS);

        pnlPrincipal.add(cardPerfilSecurity, gbcM);

        return crearContenedorTab(pnlPrincipal);
    }

    private JPanel crearPasswordWrapper(JPasswordField pf, JButton btnToggle) {
        JPanel wrapper = new JPanel(new BorderLayout(4, 0));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(380, 38));
        wrapper.setMaximumSize(new Dimension(380, 38));

        pf.setPreferredSize(new Dimension(335, 38));
        pf.setCaretColor(Color.WHITE);
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pf.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorInputBorder(), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        listaPasswordFields.add(pf);

        wrapper.add(pf, BorderLayout.CENTER);
        wrapper.add(btnToggle, BorderLayout.EAST);
        return wrapper;
    }

    private static class EyeIcon implements Icon {
        private final boolean revealed;
        private final Color iconColor;

        public EyeIcon(boolean revealed, Color iconColor) {
            this.revealed = revealed;
            this.iconColor = iconColor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(iconColor != null ? iconColor : c.getForeground());
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int cx = x + 10;
            int cy = y + 10;

            // Arcos superior e inferior del ojo
            g2.drawArc(cx - 8, cy - 5, 16, 10, 25, 130);
            g2.drawArc(cx - 8, cy - 5, 16, 10, 205, 130);

            // Pupila central
            g2.fillOval(cx - 3, cy - 3, 6, 6);

            // Si está revelada, traza línea diagonal en naranja
            if (revealed) {
                g2.setColor(ThemeManager.getColorNaranja());
                g2.drawLine(cx - 7, cy + 6, cx + 7, cy - 6);
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 20; }

        @Override
        public int getIconHeight() { return 20; }
    }

    private JButton crearBotonOjo(JPasswordField pf) {
        JButton btn = new JButton();
        btn.setIcon(new EyeIcon(false, ThemeManager.getColorTextoMuted()));
        btn.setPreferredSize(new Dimension(38, 38));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Mostrar contraseña");

        btn.addActionListener(e -> {
            if (pf.getEchoChar() != (char) 0) {
                pf.setEchoChar((char) 0);
                btn.setIcon(new EyeIcon(true, ThemeManager.getColorNaranja()));
                btn.setToolTipText("Ocultar contraseña");
            } else {
                pf.setEchoChar('•');
                btn.setIcon(new EyeIcon(false, ThemeManager.getColorTextoMuted()));
                btn.setToolTipText("Mostrar contraseña");
            }
        });

        listaBotonesSecundarios.add(btn);
        return btn;
    }

    private JLabel crearRequisitoLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        lbl.setForeground(ThemeManager.getColorTextoMuted());
        listaLabelsNormales.add(lbl);
        return lbl;
    }

    // =========================================================================
    // 2. PANEL APARIENCIA & TEMA
    // =========================================================================
    private JPanel crearPanelApariencia() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("🎨  TEMA VISUAL DE LA INTERFAZ"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel lblDescTema = new JLabel("Elige el estilo visual del sistema. Toda la interfaz se adaptará de forma fluida y armoniosa.");
        lblDescTema.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        listaLabelsNormales.add(lblDescTema);
        pnl.add(lblDescTema, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 6, 0);
        rbDark = new JRadioButton("Tema Oscuro Moderno (FlatDarkLaf)");
        rbDark.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        rbDark.setSelected(ThemeManager.isDark());
        listaRadioButtons.add(rbDark);
        pnl.add(rbDark, gbc);

        gbc.gridy++;
        rbLight = new JRadioButton("Tema Claro Profesional (FlatLightLaf)");
        rbLight.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        rbLight.setSelected(!ThemeManager.isDark());
        listaRadioButtons.add(rbLight);
        pnl.add(rbLight, gbc);

        ButtonGroup group = new ButtonGroup();
        group.add(rbDark);
        group.add(rbLight);

        gbc.gridy++;
        gbc.insets = new Insets(24, 0, 8, 0);
        pnl.add(crearTituloSeccion("📐  ESCALA Y TAMAÑO DE FUENTE"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        cbxTamanoInterfaz = new JComboBox<>(new String[]{"Pequeño", "Normal", "Grande"});
        cbxTamanoInterfaz.setSelectedIndex(1);
        cbxTamanoInterfaz.setPreferredSize(new Dimension(180, 36));
        pnl.add(cbxTamanoInterfaz, gbc);

        return crearContenedorTab(pnl);
    }

    // =========================================================================
    // 3. PANEL NOTIFICACIONES & SMTP
    // =========================================================================
    private JPanel crearPanelNotificaciones() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("🔔  PREFERENCIAS DE AVISOS Y ALERTAS"), gbc);

        gbc.gridy++;
        chkActivarNotificaciones = new JCheckBox("Activar notificaciones globales del sistema");
        chkActivarNotificaciones.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        chkActivarNotificaciones.setSelected(true);
        listaCheckBoxes.add(chkActivarNotificaciones);
        pnl.add(chkActivarNotificaciones, gbc);

        gbc.gridy++;
        chkAvisosEventos = new JCheckBox("Registrar alertas automáticas ante creación y cambio de estado de eventos");
        chkAvisosEventos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        chkAvisosEventos.setSelected(true);
        listaCheckBoxes.add(chkAvisosEventos);
        pnl.add(chkAvisosEventos, gbc);

        // --- SECCIÓN SMTP ---
        gbc.gridy++;
        gbc.insets = new Insets(24, 0, 8, 0);
        JPanel pnlTitSmtp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlTitSmtp.setOpaque(false);
        pnlTitSmtp.add(crearTituloSeccion("✉️  SERVIDOR DE CORREO SALIENTE (SMTP)"));

        JLabel lblBadgeAdmin = new JLabel(" Solo Administrador ");
        lblBadgeAdmin.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
        lblBadgeAdmin.setForeground(ThemeManager.getColorNaranja());
        lblBadgeAdmin.setOpaque(true);
        lblBadgeAdmin.setBackground(new Color(255, 117, 31, 30));
        lblBadgeAdmin.setBorder(new LineBorder(ThemeManager.getColorNaranja(), 1, true));
        pnlTitSmtp.add(lblBadgeAdmin);

        pnl.add(pnlTitSmtp, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 4, 0);
        chkSmtpHabilitado = new JCheckBox("Habilitar despacho real de correos mediante Gmail SMTP (TLS 587)");
        chkSmtpHabilitado.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        listaCheckBoxes.add(chkSmtpHabilitado);
        pnl.add(chkSmtpHabilitado, gbc);

        gbc.gridy++;
        pnl.add(crearLabelNormal("Correo Remitente (ej. miempresa@gmail.com):"), gbc);

        gbc.gridy++;
        txtSmtpRemitente = crearTextFieldModerno("correo_remitente@gmail.com");
        txtSmtpRemitente.setPreferredSize(new Dimension(380, 36));
        pnl.add(txtSmtpRemitente, gbc);

        gbc.gridy++;
        pnl.add(crearLabelNormal("Contraseña de Aplicación de Google (16 caracteres):"), gbc);

        gbc.gridy++;
        txtSmtpPassword = new JPasswordField();
        txtSmtpPassword.setPreferredSize(new Dimension(380, 36));
        txtSmtpPassword.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorInputBorder(), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        listaPasswordFields.add(txtSmtpPassword);
        pnl.add(txtSmtpPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(14, 0, 8, 0);
        btnGuardarSmtp = crearBotonAccion("💾  Guardar Configuración SMTP", new Color(52, 152, 219), false);
        btnGuardarSmtp.setPreferredSize(new Dimension(240, 38));
        pnl.add(btnGuardarSmtp, gbc);

        return crearContenedorTab(pnl);
    }

    // =========================================================================
    // 4. PANEL REGLAS DE EVENTOS
    // =========================================================================
    private JPanel crearPanelEventos() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("📅  REGLAS DE NEGOCIO Y POLÍTICAS DE EVENTOS"), gbc);

        gbc.gridy++;
        pnl.add(crearLabelNormal("• La eliminación de eventos requiere permisos de Administrador."), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• El cambio de estado a 'Finalizado' o 'Cancelado' está restringido a administradores."), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Los operadores registrados pueden crear y modificar detalles de logística y participantes."), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Las invitaciones por correo electrónico se despachan automáticamente al registrar cada asistente."), gbc);

        return crearContenedorTab(pnl);
    }

    // =========================================================================
    // 5. PANEL DATOS & BACKUP
    // =========================================================================
    private JPanel crearPanelDatos() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("💾  COPIAS DE SEGURIDAD Y MANTENIMIENTO DE BASE DE DATOS"), gbc);

        gbc.gridy++;
        JLabel lblDescDatos = new JLabel("Genera respaldos completos de la base de datos SQLite (gestion_eventos.db) para salvaguardar tu información histórica o restaurar el sistema ante contingencias.");
        lblDescDatos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        listaLabelsNormales.add(lblDescDatos);
        pnl.add(lblDescDatos, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 8, 0);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBotones.setOpaque(false);

        btnBackup = crearBotonAccion("💾  Crear Backup (.db)", new Color(46, 204, 113), false);
        btnRestaurar = crearBotonAccion("🔄  Restaurar Copia", new Color(52, 152, 219), false);
        btnRecargarDatos = crearBotonAccion("Recargar Vistas", new Color(110, 115, 130), false);
        btnLimpiarDatos = crearBotonAccion("Limpiar Datos", new Color(231, 76, 60), false);

        pnlBotones.add(btnBackup);
        pnlBotones.add(btnRestaurar);
        pnlBotones.add(btnRecargarDatos);
        pnlBotones.add(btnLimpiarDatos);

        pnl.add(pnlBotones, gbc);

        return crearContenedorTab(pnl);
    }

    // =========================================================================
    // 6. PANEL ACERCA DEL SISTEMA
    // =========================================================================
    private JPanel crearPanelAcerca() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("ℹ️  SISTEMA DE GESTIÓN DE EVENTOS"), gbc);

        gbc.gridy++;
        pnl.add(crearLabelNormal("• Versión: 1.0 Oficial (Producción Pro)"), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Arquitectura: Java 17 LTS / MVC Estricto / FlatLaf 3.4.1 / SQLite 3"), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Seguridad: SHA-256 con Salt Criptográfico de 16 bytes y Clave Maestra"), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Temas: Soporte dinámico para Modo Oscuro y Modo Claro de alta gama"), gbc);
        gbc.gridy++;
        pnl.add(crearLabelNormal("• Estado del Sistema: Óptimo, autónomo y portable."), gbc);

        return crearContenedorTab(pnl);
    }

    // =========================================================================
    // MÉTODOS DE APLICACIÓN DE TEMA DINÁMICO
    // =========================================================================
    @Override
    public void aplicarTema(boolean isDark) {
        Color bg = ThemeManager.getColorBg();
        Color cardBg = ThemeManager.getColorCardBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoNorm = ThemeManager.getColorTextoNormal();
        Color textoMuted = ThemeManager.getColorTextoMuted();
        Color inputBg = ThemeManager.getColorInputBg();
        Color inputBorde = ThemeManager.getColorInputBorder();

        setBackground(bg);

        lblTitulo.setForeground(textoTit);
        lblSub.setForeground(textoMuted);

        tabbedPane.setBackground(cardBg);
        tabbedPane.setForeground(textoTit);

        for (JPanel w : listaWrappers) {
            w.setBackground(bg);
        }

        for (JPanel p : listaPanelesBase) {
            p.setBackground(cardBg);
            p.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(22, 26, 22, 26)
            ));
        }

        if (cardPerfilInfo != null) {
            cardPerfilInfo.setBackground(cardBg);
            cardPerfilInfo.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(18, 22, 18, 22)
            ));
        }

        if (cardPerfilSecurity != null) {
            cardPerfilSecurity.setBackground(cardBg);
            cardPerfilSecurity.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(20, 24, 20, 24)
            ));
        }

        for (JLabel l : listaTitulosSeccion) {
            l.setForeground(ThemeManager.getColorNaranja());
        }

        for (JLabel l : listaLabelsNormales) {
            l.setForeground(textoNorm);
        }

        for (JTextField tf : listaTextFields) {
            tf.setBackground(inputBg);
            tf.setForeground(textoNorm);
            tf.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(inputBorde, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));
        }

        for (JPasswordField pf : listaPasswordFields) {
            pf.setBackground(inputBg);
            pf.setForeground(textoNorm);
            pf.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
            pf.setBorder(new CompoundBorder(
                    new LineBorder(inputBorde, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));
        }

        for (JRadioButton rb : listaRadioButtons) {
            rb.setBackground(cardBg);
            rb.setForeground(textoNorm);
        }

        for (JCheckBox cb : listaCheckBoxes) {
            cb.setBackground(cardBg);
            cb.setForeground(textoNorm);
        }

        for (JButton btn : listaBotonesSecundarios) {
            btn.setBackground(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
            btn.setForeground(textoTit);
            btn.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(6, 10, 6, 10)
            ));
        }

        if (btnToggleActual != null) {
            btnToggleActual.setIcon(new EyeIcon(txtActualPass.getEchoChar() == (char) 0, txtActualPass.getEchoChar() == (char) 0 ? ThemeManager.getColorNaranja() : textoMuted));
        }
        if (btnToggleNueva != null) {
            btnToggleNueva.setIcon(new EyeIcon(txtNuevaPass.getEchoChar() == (char) 0, txtNuevaPass.getEchoChar() == (char) 0 ? ThemeManager.getColorNaranja() : textoMuted));
        }
        if (btnToggleConfirmar != null) {
            btnToggleConfirmar.setIcon(new EyeIcon(txtConfirmarPass.getEchoChar() == (char) 0, txtConfirmarPass.getEchoChar() == (char) 0 ? ThemeManager.getColorNaranja() : textoMuted));
        }

        if (cbxTamanoInterfaz != null) {
            cbxTamanoInterfaz.setBackground(inputBg);
            cbxTamanoInterfaz.setForeground(textoNorm);
        }

        if (rbDark != null) rbDark.setSelected(isDark);
        if (rbLight != null) rbLight.setSelected(!isDark);

        repaint();
    }

    // =========================================================================
    // GENERADORES DE COMPONENTES AUXILIARES
    // =========================================================================
    private JLabel crearTituloSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(ThemeManager.getColorNaranja());
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        listaTitulosSeccion.add(lbl);
        return lbl;
    }

    private JLabel crearLabelNormal(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(ThemeManager.getColorTextoNormal());
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        listaLabelsNormales.add(lbl);
        return lbl;
    }

    private JTextField crearTextFieldModerno(String placeholder) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(380, 36));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorInputBorder(), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        listaTextFields.add(tf);
        return tf;
    }

    private JButton crearBotonAccion(String texto, Color acento, boolean esPrimario) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 38));

        Color bgNormal = esPrimario ? acento : new Color(38, 38, 48);
        Color fgNormal = esPrimario ? Color.WHITE : ThemeManager.getColorTextoTitulo();
        Color borderNormal = esPrimario ? acento : ThemeManager.getColorBorde();

        btn.setBackground(bgNormal);
        btn.setForeground(fgNormal);
        btn.setBorder(new CompoundBorder(
                new LineBorder(borderNormal, 1, true),
                new EmptyBorder(7, 14, 7, 14)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (esPrimario) {
                    btn.setBackground(ThemeManager.getColorNaranjaHover());
                } else {
                    btn.setBackground(ThemeManager.isDark() ? new Color(48, 48, 62) : new Color(228, 233, 240));
                    btn.setBorder(new CompoundBorder(
                            new LineBorder(acento, 1, true),
                            new EmptyBorder(7, 14, 7, 14)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(esPrimario ? acento : (ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249)));
                btn.setForeground(esPrimario ? Color.WHITE : ThemeManager.getColorTextoTitulo());
                btn.setBorder(new CompoundBorder(
                        new LineBorder(esPrimario ? acento : ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(7, 14, 7, 14)
                ));
            }
        });

        if (!esPrimario) {
            listaBotonesSecundarios.add(btn);
        }
        return btn;
    }

    // =========================================================================
    // GETTERS PARA EL CONTROLADOR
    // =========================================================================
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblCorreo() { return lblCorreo; }
    public JLabel getLblRolBadge() { return lblRolBadge; }
    public JPasswordField getTxtActualPass() { return txtActualPass; }
    public JPasswordField getTxtNuevaPass() { return txtNuevaPass; }
    public JPasswordField getTxtConfirmarPass() { return txtConfirmarPass; }
    public JButton getBtnActualizarPass() { return btnActualizarPass; }
    public JRadioButton getRbDark() { return rbDark; }
    public JRadioButton getRbLight() { return rbLight; }
    public JComboBox<String> getCbxTamanoInterfaz() { return cbxTamanoInterfaz; }
    public JCheckBox getChkActivarNotificaciones() { return chkActivarNotificaciones; }
    public JCheckBox getChkAvisosEventos() { return chkAvisosEventos; }
    public JButton getBtnRecargarDatos() { return btnRecargarDatos; }
    public JButton getBtnLimpiarDatos() { return btnLimpiarDatos; }
    public JButton getBtnBackup() { return btnBackup; }
    public JButton getBtnRestaurar() { return btnRestaurar; }
    public JTextField getTxtSmtpRemitente() { return txtSmtpRemitente; }
    public JPasswordField getTxtSmtpPassword() { return txtSmtpPassword; }
    public JCheckBox getChkSmtpHabilitado() { return chkSmtpHabilitado; }
    public JButton getBtnGuardarSmtp() { return btnGuardarSmtp; }
}
