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
import javax.swing.border.MatteBorder;

public class FrmDashboard extends JFrame implements ThemeManager.ThemeListener {

    private JPanel pnlSidebar;
    private JPanel pnlMenuVertical;
    private JPanel pnlUserCard;
    private JPanel pnlTextosUserCard;
    private JLabel lblAvatarUser;
    private JPanel pnlSidebarFooter;
    private JPanel pnlContent;
    private CardLayout cardLayout;

    // Botones del Sidebar
    private JButton btnDashboard;
    private JButton btnEventos;
    private JButton btnCalendario;
    private JButton btnParticipantes;
    private JButton btnInformes;
    private JButton btnConfiguracion;
    private JButton btnActividades;
    private JButton btnNotificaciones;
    private JButton btnSalir;

    // Elementos de perfil y estado
    private JLabel lblUserName;
    private JLabel lblUserRoleBadge;
    private JLabel lblUsuarioHeader;
    private JLabel lblNotifHeader;
    private JLabel lblRelojHeader;

    // Header y componentes
    private JPanel pnlHeader;
    private JPanel pnlHeaderIzq;
    private JPanel pnlHeaderTitulos;
    private JPanel pnlHeaderDer;
    private JLabel lblHeaderTit;
    private JLabel lblHeaderSub;
    private JPanel pnlUserChip;

    // Lista de separadores y botones para actualización de tema
    private final List<JLabel> listaSeparadores = new ArrayList<>();
    private final List<JButton> listaBotonesMenu = new ArrayList<>();
    private JButton botonActivo = null;

    private java.awt.image.BufferedImage imgFondoPoligonal;

    public FrmDashboard() {
        initComponents();
        iniciarReloj();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Eventos");
        setSize(1150, 780);
        setMinimumSize(new Dimension(950, 650));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            java.net.URL bgUrl = getClass().getResource("/img/login_bg.png");
            if (bgUrl != null) {
                imgFondoPoligonal = javax.imageio.ImageIO.read(bgUrl);
            }
        } catch (Exception ignored) {}

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/LOGO_SGE.png"));
            setIconImage(icon);
        } catch (Exception ignored) {}

        // ==========================================
        // SIDEBAR (Izquierda)
        // ==========================================
        pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BorderLayout());
        pnlSidebar.setPreferredSize(new Dimension(280, 0));

        // Contenedor vertical para el menú de navegación
        pnlMenuVertical = new JPanel();
        pnlMenuVertical.setLayout(new BoxLayout(pnlMenuVertical, BoxLayout.Y_AXIS));
        pnlMenuVertical.setBorder(new EmptyBorder(18, 12, 18, 12));

        // 1. Logo superior
        try {
            java.net.URL logoUrl = getClass().getResource("/img/LOGO_SGE.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image img = logoIcon.getImage();
                int width = 170;
                int height = (int) (((double) width / img.getWidth(null)) * img.getHeight(null));
                JLabel lblLogo = new JLabel(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
                pnlMenuVertical.add(lblLogo);
            }
        } catch (Exception ignored) {}

        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 14)));

        // 2. Mini-Card de Usuario en el Sidebar
        pnlUserCard = crearPanelUsuarioSidebar();
        pnlUserCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMenuVertical.add(pnlUserCard);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 18)));

        // 3. Secciones del Menú de Navegación
        pnlMenuVertical.add(crearSeparadorCategoria("PRINCIPAL"));
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 6)));

        btnDashboard = crearBotonMenu("🏠  Inicio");
        btnEventos = crearBotonMenu("📅  Gestión de Eventos");
        btnCalendario = crearBotonMenu("🗓️  Calendario");

        pnlMenuVertical.add(btnDashboard);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlMenuVertical.add(btnEventos);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlMenuVertical.add(btnCalendario);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 14)));

        pnlMenuVertical.add(crearSeparadorCategoria("OPERACIONES"));
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 6)));

        btnParticipantes = crearBotonMenu("👥  Participantes");
        btnActividades = crearBotonMenu("📋  Recursos y Actividades");

        pnlMenuVertical.add(btnParticipantes);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlMenuVertical.add(btnActividades);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 14)));

        pnlMenuVertical.add(crearSeparadorCategoria("SISTEMA"));
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 6)));

        btnInformes = crearBotonMenu("📊  Informes");
        btnNotificaciones = crearBotonMenu("🔔  Notificaciones");
        btnConfiguracion = crearBotonMenu("⚙️  Configuración");

        pnlMenuVertical.add(btnInformes);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlMenuVertical.add(btnNotificaciones);
        pnlMenuVertical.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlMenuVertical.add(btnConfiguracion);

        pnlMenuVertical.add(Box.createVerticalGlue());

        pnlSidebar.add(pnlMenuVertical, BorderLayout.CENTER);

        // 4. Footer del Sidebar con Botón Salir / Login
        pnlSidebarFooter = new JPanel(new BorderLayout());
        pnlSidebarFooter.setBorder(new EmptyBorder(10, 14, 15, 14));

        btnSalir = new JButton("🚪  Cerrar Sesión");
        btnSalir.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(0, 42));
        btnSalir.setBackground(new Color(231, 76, 60, 22));
        btnSalir.setForeground(new Color(255, 107, 95));
        btnSalir.setBorder(new CompoundBorder(
                new LineBorder(new Color(231, 76, 60, 65), 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSalir.setBackground(new Color(231, 76, 60, 45));
                btnSalir.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(new Color(231, 76, 60, 22));
                btnSalir.setForeground(new Color(255, 107, 95));
            }
        });
        pnlSidebarFooter.add(btnSalir, BorderLayout.CENTER);
        pnlSidebar.add(pnlSidebarFooter, BorderLayout.SOUTH);

        add(pnlSidebar, BorderLayout.WEST);

        // Marcamos por defecto el botón Dashboard como activo
        marcarBotonActivo(btnDashboard);

        // ==========================================
        // ÁREA CENTRAL (Header + Contenido)
        // ==========================================
        JPanel pnlCentro = new JPanel(new BorderLayout());

        // HEADER PRINCIPAL SUPERIOR
        pnlHeader = new JPanel(new BorderLayout(15, 0));

        // Izquierda del Header
        pnlHeaderIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlHeaderIzq.setOpaque(false);

        JLabel lblBadgeApp = new JLabel(" SGE PRO ");
        lblBadgeApp.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        lblBadgeApp.setForeground(ThemeManager.getColorNaranja());
        lblBadgeApp.setOpaque(true);
        lblBadgeApp.setBackground(new Color(255, 117, 31, 30));
        lblBadgeApp.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 117, 31, 90), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));

        pnlHeaderTitulos = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlHeaderTitulos.setOpaque(false);

        lblHeaderTit = new JLabel("Sistema de Gestión de Eventos");
        lblHeaderTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));

        lblHeaderSub = new JLabel("Plataforma Integral de Planificación, Logística y Asistencia");
        lblHeaderSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlHeaderTitulos.add(lblHeaderTit);
        pnlHeaderTitulos.add(lblHeaderSub);

        pnlHeaderIzq.add(lblBadgeApp);
        pnlHeaderIzq.add(pnlHeaderTitulos);

        // Derecha del Header
        pnlHeaderDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        pnlHeaderDer.setOpaque(false);

        // Widget de Reloj en tiempo real
        lblRelojHeader = new JLabel("🕒 Cargando hora...");
        lblRelojHeader.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblRelojHeader.setOpaque(true);

        // Botón / Icono de Notificaciones interactivo
        lblNotifHeader = new JLabel("🔔");
        lblNotifHeader.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        lblNotifHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblNotifHeader.setOpaque(true);
        lblNotifHeader.setToolTipText("Buzón de auditoría y notificaciones");
        lblNotifHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblNotifHeader.setBackground(new Color(255, 117, 31, 35));
                lblNotifHeader.setForeground(ThemeManager.getColorNaranja());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblNotifHeader.setBackground(ThemeManager.getColorChipBg());
                lblNotifHeader.setForeground(ThemeManager.getColorTextoTitulo());
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                btnNotificaciones.doClick();
            }
        });

        // Chip de Usuario en línea
        pnlUserChip = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        pnlUserChip.setOpaque(true);

        JLabel lblDotOnline = new JLabel("●");
        lblDotOnline.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lblDotOnline.setForeground(new Color(46, 204, 113)); // Verde online

        lblUsuarioHeader = new JLabel("Cargando...");
        lblUsuarioHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        pnlUserChip.add(lblDotOnline);
        pnlUserChip.add(lblUsuarioHeader);

        pnlHeaderDer.add(lblRelojHeader);
        pnlHeaderDer.add(lblNotifHeader);
        pnlHeaderDer.add(pnlUserChip);

        pnlHeader.add(pnlHeaderIzq, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderDer, BorderLayout.EAST);

        pnlCentro.add(pnlHeader, BorderLayout.NORTH);

        // ÁREA DE CONTENIDO (CardLayout)
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                if (ThemeManager.isDark()) {
                    if (imgFondoPoligonal != null) {
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.drawImage(imgFondoPoligonal, 0, 0, getWidth(), getHeight(), null);
                    } else {
                        g2.setColor(ThemeManager.getColorBg());
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                } else {
                    // Gradiente sofisticado para tema claro SaaS
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    GradientPaint lightGrad = new GradientPaint(
                            0, 0, new Color(244, 247, 251),
                            0, getHeight(), new Color(236, 240, 246)
                    );
                    g2.setPaint(lightGrad);
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Patrón sutil y elegante de micro-puntos
                    g2.setColor(new Color(210, 218, 230, 90));
                    for (int x = 30; x < getWidth(); x += 40) {
                        for (int y = 30; y < getHeight(); y += 40) {
                            g2.fillOval(x, y, 2, 2);
                        }
                    }
                }
                g2.dispose();
            }
        };

        pnlCentro.add(pnlContent, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);
    }

    private JPanel crearPanelUsuarioSidebar() {
        pnlUserCard = new JPanel(new BorderLayout(10, 0));
        pnlUserCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        lblAvatarUser = new JLabel("👤");
        lblAvatarUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        lblAvatarUser.setForeground(ThemeManager.getColorNaranja());
        pnlUserCard.add(lblAvatarUser, BorderLayout.WEST);

        pnlTextosUserCard = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTextosUserCard.setOpaque(false);

        lblUserName = new JLabel("Invitado");
        lblUserName.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        lblUserRoleBadge = new JLabel("Invitado");
        lblUserRoleBadge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        lblUserRoleBadge.setForeground(new Color(80, 220, 130));

        pnlTextosUserCard.add(lblUserName);
        pnlTextosUserCard.add(lblUserRoleBadge);
        pnlUserCard.add(pnlTextosUserCard, BorderLayout.CENTER);

        return pnlUserCard;
    }

    private JLabel crearSeparadorCategoria(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        lbl.setBorder(new EmptyBorder(6, 12, 2, 12));
        listaSeparadores.add(lbl);
        return lbl;
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean esActivo = (this == botonActivo);
                boolean isDark = ThemeManager.isDark();

                if (esActivo) {
                    // Fondo activo con acento suave
                    g2.setColor(isDark ? new Color(255, 117, 31, 35) : new Color(255, 117, 31, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                    // Barra vertical indicadora izquierda
                    g2.setColor(ThemeManager.getColorNaranja());
                    g2.fillRoundRect(2, 6, 4, getHeight() - 12, 4, 4);
                } else if (getModel().isRollover()) {
                    // Fondo hover suave
                    g2.setColor(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(256, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 10));
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != botonActivo) {
                    btn.setForeground(ThemeManager.isDark() ? Color.WHITE : ThemeManager.getColorTextoTitulo());
                }
                btn.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != botonActivo) {
                    btn.setForeground(ThemeManager.getColorTextoMuted());
                }
                btn.repaint();
            }
        });

        btn.addActionListener(e -> marcarBotonActivo(btn));

        listaBotonesMenu.add(btn);
        return btn;
    }

    public void marcarBotonActivo(JButton boton) {
        this.botonActivo = boton;
        boolean isDark = ThemeManager.isDark();
        for (JButton b : listaBotonesMenu) {
            if (b == boton) {
                b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                b.setForeground(isDark ? Color.WHITE : ThemeManager.getColorNaranja());
            } else {
                b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                b.setForeground(ThemeManager.getColorTextoMuted());
            }
            b.repaint();
        }
    }

    @Override
    public void aplicarTema(boolean isDark) {
        Color sidebarBg = ThemeManager.getColorSidebarBg();
        Color headerBg = ThemeManager.getColorHeaderBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();
        Color chipBg = ThemeManager.getColorChipBg();
        Color cardAlt = ThemeManager.getColorCardAlt();

        // 1. Sidebar
        pnlSidebar.setBackground(sidebarBg);
        pnlSidebar.setBorder(new MatteBorder(0, 0, 0, 1, borde));
        pnlMenuVertical.setBackground(sidebarBg);

        pnlUserCard.setBackground(cardAlt);
        pnlUserCard.setBorder(new CompoundBorder(
                new LineBorder(borde, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        lblUserName.setForeground(textoTit);
        pnlSidebarFooter.setBackground(sidebarBg);

        for (JLabel sep : listaSeparadores) {
            sep.setForeground(isDark ? new Color(110, 115, 130) : new Color(120, 130, 145));
        }

        // Refrescar botones del menú
        for (JButton b : listaBotonesMenu) {
            if (b == botonActivo) {
                b.setForeground(isDark ? Color.WHITE : ThemeManager.getColorNaranja());
            } else {
                b.setForeground(textoMuted);
            }
            b.repaint();
        }

        // 2. Header
        pnlHeader.setBackground(headerBg);
        pnlHeader.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, borde),
                new EmptyBorder(14, 30, 14, 30)
        ));
        lblHeaderTit.setForeground(textoTit);
        lblHeaderSub.setForeground(textoMuted);

        lblRelojHeader.setBackground(chipBg);
        lblRelojHeader.setForeground(textoTit);
        lblRelojHeader.setBorder(new CompoundBorder(
                new LineBorder(borde, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));

        lblNotifHeader.setBackground(chipBg);
        lblNotifHeader.setForeground(textoTit);
        lblNotifHeader.setBorder(new CompoundBorder(
                new LineBorder(borde, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        pnlUserChip.setBackground(chipBg);
        pnlUserChip.setBorder(new CompoundBorder(
                new LineBorder(borde, 1, true),
                new EmptyBorder(5, 12, 5, 12)
        ));
        lblUsuarioHeader.setForeground(textoTit);

        // 3. Contenido
        pnlContent.repaint();
        repaint();
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM | hh:mm:ss a", new java.util.Locale("es", "ES"));
            String texto = dtf.format(now);
            if (texto.length() > 0) {
                texto = texto.substring(0, 1).toUpperCase() + texto.substring(1);
            }
            lblRelojHeader.setText("🕒 " + texto);
        });
        timer.setRepeats(true);
        timer.start();
    }

    public void agregarPanelContenido(Container panel, String nombreCard) {
        pnlContent.add(panel, nombreCard);
    }

    public void mostrarPanel(String nombreCard) {
        cardLayout.show(pnlContent, nombreCard);
    }

    public void setLblUserRole(String rol) {
        if (lblUserRoleBadge != null) {
            lblUserRoleBadge.setText(rol);
            if ("Administrador".equalsIgnoreCase(rol)) {
                lblUserRoleBadge.setForeground(ThemeManager.getColorNaranja());
            } else if ("Operador".equalsIgnoreCase(rol)) {
                lblUserRoleBadge.setForeground(new Color(52, 152, 219));
            } else {
                lblUserRoleBadge.setForeground(new Color(46, 204, 113));
            }
        }
    }

    public void setLblUsuarioHeader(String nombre) {
        if (lblUsuarioHeader != null) {
            lblUsuarioHeader.setText(nombre);
        }
        if (lblUserName != null) {
            lblUserName.setText(nombre);
        }
    }

    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnEventos() { return btnEventos; }
    public JButton getBtnCalendario() { return btnCalendario; }
    public JButton getBtnParticipantes() { return btnParticipantes; }
    public JButton getBtnInformes() { return btnInformes; }
    public JButton getBtnActividades() { return btnActividades; }
    public JButton getBtnNotificaciones() { return btnNotificaciones; }
    public JButton getBtnConfiguracion() { return btnConfiguracion; }
    public JButton getBtnSalir() { return btnSalir; }
    public JLabel getLblNotifHeader() { return lblNotifHeader; }
}
