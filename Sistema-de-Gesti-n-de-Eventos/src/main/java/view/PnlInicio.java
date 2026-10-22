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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PnlInicio extends JPanel implements ThemeManager.ThemeListener {

    private JLabel lblTotalEventos;
    private JLabel lblProximosEventos;
    private JLabel lblFinalizados;
    private JLabel lblEfectividad;
    private JLabel lblTituloBienvenida;
    private JLabel lblSubtituloHero;

    private JTable tblRecientes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;

    // Botones de Acciones Rápidas
    private JButton btnAccionCrear;
    private JButton btnAccionCalendario;
    private JButton btnAccionParticipantes;
    private JButton btnAccionInformes;

    // Elementos estructurales para re-theming
    private JPanel pnlHero;
    private JPanel pnlCenter;
    private JPanel pnlTarjetas;
    private JPanel pnlInferior;
    private JPanel pnlTablaContainer;
    private JPanel pnlHeaderBloque;
    private JPanel pnlTitulosAgenda;
    private JLabel lblTituloAgenda;
    private JLabel lblSubAgenda;
    private JPanel pnlAccionesContainer;
    private JLabel lblTitAcciones;
    private JLabel lblSubAcciones;
    private JPanel pnlTip;
    private JLabel lblTipText;

    private final List<JPanel> listaTarjetasKPI = new ArrayList<>();
    private final List<JLabel> listaLabelsKPITitulo = new ArrayList<>();
    private final List<JLabel> listaLabelsKPISub = new ArrayList<>();
    private final List<JButton> listaBotonesAccion = new ArrayList<>();

    private int totalActual = 0;
    private int finalizadosActual = 0;

    public PnlInicio() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ==========================================
        // 1. HERO BANNER DE BIENVENIDA SUPERIOR
        // ==========================================
        pnlHero = crearHeroBanner();
        add(pnlHero, BorderLayout.NORTH);

        // ==========================================
        // 2. CONTENEDOR CENTRAL (KPIs + Split Agenda/Acciones)
        // ==========================================
        pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

        // Grid de Tarjetas KPI
        pnlTarjetas = new JPanel(new GridLayout(1, 4, 12, 0));
        pnlTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        pnlTarjetas.setPreferredSize(new Dimension(0, 115));

        lblTotalEventos = new JLabel("0");
        JPanel cardTotal = crearTarjetaKPI("TOTAL EVENTOS", lblTotalEventos, "📅", new Color(52, 152, 219), "Eventos en plataforma");
        pnlTarjetas.add(cardTotal);

        lblProximosEventos = new JLabel("0");
        JPanel cardProximos = crearTarjetaKPI("PRÓXIMOS EVENTOS", lblProximosEventos, "⏳", ThemeManager.getColorNaranja(), "Por realizarse");
        pnlTarjetas.add(cardProximos);

        lblFinalizados = new JLabel("0");
        JPanel cardFin = crearTarjetaKPI("FINALIZADOS", lblFinalizados, "✅", new Color(46, 204, 113), "Completados con éxito");
        pnlTarjetas.add(cardFin);

        lblEfectividad = new JLabel("0%");
        JPanel cardEfectividad = crearTarjetaKPI("TASA DE EJECUCIÓN", lblEfectividad, "📈", new Color(155, 89, 182), "Culminados vs total");
        pnlTarjetas.add(cardEfectividad);

        pnlCenter.add(pnlTarjetas);
        pnlCenter.add(Box.createRigidArea(new Dimension(0, 20)));

        // ==========================================
        // 3. SECCIÓN INFERIOR DIVIDIDA (AGENDA + ACCIONES)
        // ==========================================
        pnlInferior = new JPanel(new BorderLayout(20, 0));

        // Columna Izquierda: Tabla de Próximos Eventos
        pnlTablaContainer = crearPanelTablaAgenda();
        pnlInferior.add(pnlTablaContainer, BorderLayout.CENTER);

        // Columna Derecha: Acciones Rápidas & Tip
        pnlAccionesContainer = crearPanelAccionesRapidas();
        pnlInferior.add(pnlAccionesContainer, BorderLayout.EAST);

        pnlCenter.add(pnlInferior);
        add(pnlCenter, BorderLayout.CENTER);
    }

    private JPanel crearHeroBanner() {
        JPanel hero = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isDark = ThemeManager.isDark();
                if (isDark) {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(38, 28, 24),
                            getWidth(), 0, new Color(26, 26, 34)
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(new Color(255, 117, 31, 70));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                } else {
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(255, 247, 237),
                            getWidth(), 0, new Color(255, 255, 255)
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(new Color(255, 180, 120, 160));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                }
                g2.dispose();
            }
        };
        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(18, 24, 18, 24));
        hero.setPreferredSize(new Dimension(0, 95));

        JPanel pnlTextos = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTextos.setOpaque(false);

        lblTituloBienvenida = new JLabel("¡Bienvenido al Panel de Control! 👋");
        lblTituloBienvenida.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));

        lblSubtituloHero = new JLabel("Supervisa la programación en tiempo real, controla la logística y gestiona tus asistentes.");
        lblSubtituloHero.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlTextos.add(lblTituloBienvenida);
        pnlTextos.add(lblSubtituloHero);
        hero.add(pnlTextos, BorderLayout.CENTER);

        // Badge a la derecha
        JPanel pnlBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pnlBadge.setOpaque(false);

        JLabel lblEstadoPill = new JLabel("● Sistema Operativo & SQLite Conectado");
        lblEstadoPill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lblEstadoPill.setForeground(new Color(46, 204, 113));
        lblEstadoPill.setOpaque(true);
        lblEstadoPill.setBackground(new Color(46, 204, 113, 25));
        lblEstadoPill.setBorder(new CompoundBorder(
                new LineBorder(new Color(46, 204, 113, 60), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        pnlBadge.add(lblEstadoPill);
        hero.add(pnlBadge, BorderLayout.EAST);

        return hero;
    }

    private JPanel crearTarjetaKPI(String titulo, JLabel lblValor, String icono, Color colorAcento, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(ThemeManager.isDark() ? new Color(36, 36, 46) : new Color(248, 250, 252));
                card.setBorder(new CompoundBorder(
                        new LineBorder(colorAcento, 1, true),
                        new EmptyBorder(10, 12, 10, 10)
                ));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(ThemeManager.getColorCardBg());
                card.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(10, 12, 10, 10)
                ));
                card.repaint();
            }
        });

        // Barra superior de acento
        JPanel pnlBarraAcento = new JPanel();
        pnlBarraAcento.setBackground(colorAcento);
        pnlBarraAcento.setPreferredSize(new Dimension(0, 3));
        card.add(pnlBarraAcento, BorderLayout.NORTH);

        // Textos del centro
        JPanel pnlTextos = new JPanel();
        pnlTextos.setLayout(new BoxLayout(pnlTextos, BoxLayout.Y_AXIS));
        pnlTextos.setOpaque(false);

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        listaLabelsKPITitulo.add(lblTitle);

        lblValor.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        listaLabelsKPISub.add(lblSub);

        pnlTextos.add(Box.createRigidArea(new Dimension(0, 6)));
        pnlTextos.add(lblTitle);
        pnlTextos.add(Box.createRigidArea(new Dimension(0, 2)));
        pnlTextos.add(lblValor);
        pnlTextos.add(Box.createRigidArea(new Dimension(0, 2)));
        pnlTextos.add(lblSub);

        card.add(pnlTextos, BorderLayout.CENTER);

        // Icono a la derecha
        JLabel lblIcon = new JLabel(icono);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setVerticalAlignment(SwingConstants.CENTER);
        card.add(lblIcon, BorderLayout.EAST);

        listaTarjetasKPI.add(card);
        return card;
    }

    private JPanel crearPanelTablaAgenda() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));

        // Encabezado del bloque
        pnlHeaderBloque = new JPanel(new BorderLayout());
        pnlHeaderBloque.setOpaque(false);

        lblTituloAgenda = new JLabel("📅  Próximos Eventos a Realizarse");
        lblTituloAgenda.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));

        lblSubAgenda = new JLabel("Ordenados cronológicamente por fecha más cercana");
        lblSubAgenda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlTitulosAgenda = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitulosAgenda.setOpaque(false);
        pnlTitulosAgenda.add(lblTituloAgenda);
        pnlTitulosAgenda.add(lblSubAgenda);

        pnlHeaderBloque.add(pnlTitulosAgenda, BorderLayout.WEST);
        panel.add(pnlHeaderBloque, BorderLayout.NORTH);

        // Configuración de la JTable
        String[] columnas = {"ID", "Nombre del Evento", "Tipo", "Fecha Programada", "Lugar / Sede"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblRecientes = new JTable(modeloTabla);
        tblRecientes.setRowHeight(38);
        tblRecientes.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tblRecientes.setShowVerticalLines(false);
        tblRecientes.setShowHorizontalLines(true);
        tblRecientes.setSelectionBackground(new Color(255, 117, 31, 35));

        // Header de la tabla
        tblRecientes.getTableHeader().setReorderingAllowed(false);
        tblRecientes.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        configurarRenderizadoresTabla();

        scrollTabla = new JScrollPane(tblRecientes);
        panel.add(scrollTabla, BorderLayout.CENTER);
        return panel;
    }

    private void configurarRenderizadoresTabla() {
        DefaultTableCellRenderer idRenderer = new DefaultTableCellRenderer();
        idRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        idRenderer.setForeground(new Color(130, 135, 150));
        tblRecientes.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        tblRecientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblRecientes.getColumnModel().getColumn(0).setMaxWidth(70);

        DefaultTableCellRenderer nombreRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        tblRecientes.getColumnModel().getColumn(1).setCellRenderer(nombreRenderer);
        tblRecientes.getColumnModel().getColumn(1).setPreferredWidth(190);

        tblRecientes.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
                p.setOpaque(true);
                boolean isDark = ThemeManager.isDark();
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? (isDark ? new Color(22, 22, 28) : Color.WHITE) : (isDark ? new Color(26, 26, 33) : new Color(248, 250, 252))));

                String tipo = value != null ? value.toString() : "General";
                JLabel pill = new JLabel(" " + tipo + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
                pill.setOpaque(true);

                if (tipo.equalsIgnoreCase("Conferencia") || tipo.equalsIgnoreCase("Académico")) {
                    pill.setBackground(new Color(52, 152, 219, 35));
                    pill.setForeground(new Color(52, 152, 219));
                    pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                } else if (tipo.equalsIgnoreCase("Taller") || tipo.equalsIgnoreCase("Social")) {
                    pill.setBackground(new Color(46, 204, 113, 35));
                    pill.setForeground(new Color(39, 174, 96));
                    pill.setBorder(new LineBorder(new Color(46, 204, 113, 90), 1, true));
                } else if (tipo.equalsIgnoreCase("Seminario") || tipo.equalsIgnoreCase("Empresarial")) {
                    pill.setBackground(new Color(255, 117, 31, 35));
                    pill.setForeground(ThemeManager.getColorNaranja());
                    pill.setBorder(new LineBorder(new Color(255, 117, 31, 90), 1, true));
                } else {
                    pill.setBackground(new Color(155, 89, 182, 35));
                    pill.setForeground(new Color(142, 68, 173));
                    pill.setBorder(new LineBorder(new Color(155, 89, 182, 90), 1, true));
                }

                p.add(pill);
                return p;
            }
        });
        tblRecientes.getColumnModel().getColumn(2).setPreferredWidth(120);

        DefaultTableCellRenderer fechaRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, "📅 " + (value != null ? value.toString() : ""), isSelected, hasFocus, row, column);
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tblRecientes.getColumnModel().getColumn(3).setCellRenderer(fechaRenderer);
        tblRecientes.getColumnModel().getColumn(3).setPreferredWidth(120);

        DefaultTableCellRenderer lugarRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, "📍 " + (value != null ? value.toString() : ""), isSelected, hasFocus, row, column);
                c.setForeground(ThemeManager.getColorTextoMuted());
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tblRecientes.getColumnModel().getColumn(4).setCellRenderer(lugarRenderer);
        tblRecientes.getColumnModel().getColumn(4).setPreferredWidth(140);
    }

    private JPanel crearPanelAccionesRapidas() {
        pnlAccionesContainer = new JPanel();
        pnlAccionesContainer.setLayout(new BoxLayout(pnlAccionesContainer, BoxLayout.Y_AXIS));
        pnlAccionesContainer.setPreferredSize(new Dimension(280, 0));

        lblTitAcciones = new JLabel("⚡ Acciones Rápidas");
        lblTitAcciones.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        lblTitAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblSubAcciones = new JLabel("Accesos directos a operaciones");
        lblSubAcciones.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSubAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlAccionesContainer.add(lblTitAcciones);
        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 2)));
        pnlAccionesContainer.add(lblSubAcciones);
        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 16)));

        btnAccionCrear = crearBotonAccionRapida("➕  Crear Nuevo Evento", ThemeManager.getColorNaranja(), true);
        btnAccionCalendario = crearBotonAccionRapida("🗓️  Ver Calendario Mensual", new Color(52, 152, 219), false);
        btnAccionParticipantes = crearBotonAccionRapida("👥  Registrar Participantes", new Color(46, 204, 113), false);
        btnAccionInformes = crearBotonAccionRapida("📊  Dashboard de Informes", new Color(155, 89, 182), false);

        pnlAccionesContainer.add(btnAccionCrear);
        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlAccionesContainer.add(btnAccionCalendario);
        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlAccionesContainer.add(btnAccionParticipantes);
        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlAccionesContainer.add(btnAccionInformes);

        pnlAccionesContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Box de Consejo Operativo (Tip Box)
        pnlTip = new JPanel(new BorderLayout(8, 6));
        pnlTip.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel lblTipIcon = new JLabel("💡");
        lblTipIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        pnlTip.add(lblTipIcon, BorderLayout.WEST);

        lblTipText = new JLabel("<html><b>Tip Operativo:</b><br>Al inscribir asistentes, el sistema despacha invitaciones automáticas por correo electrónico.</html>");
        lblTipText.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        pnlTip.add(lblTipText, BorderLayout.CENTER);

        pnlAccionesContainer.add(pnlTip);

        return pnlAccionesContainer;
    }

    private JButton crearBotonAccionRapida(String texto, Color acento, boolean esPrimario) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (esPrimario) {
                    btn.setBackground(ThemeManager.getColorNaranjaHover());
                } else {
                    btn.setBackground(ThemeManager.isDark() ? new Color(48, 48, 60) : new Color(228, 233, 240));
                    btn.setBorder(new CompoundBorder(
                            new LineBorder(acento, 1, true),
                            new EmptyBorder(8, 14, 8, 14)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(esPrimario ? ThemeManager.getColorNaranja() : (ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249)));
                btn.setForeground(esPrimario ? Color.WHITE : ThemeManager.getColorTextoTitulo());
                btn.setBorder(new CompoundBorder(
                        new LineBorder(esPrimario ? ThemeManager.getColorNaranja() : ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
        });

        listaBotonesAccion.add(btn);
        return btn;
    }

    @Override
    public void aplicarTema(boolean isDark) {
        Color bg = ThemeManager.getColorBg();
        Color cardBg = ThemeManager.getColorCardBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();
        Color textoNorm = ThemeManager.getColorTextoNormal();
        Color inputBg = ThemeManager.getColorInputBg();
        Color tablaHeaderBg = ThemeManager.getColorTablaHeaderBg();
        Color gridColor = ThemeManager.getColorTablaGrid();

        setBackground(bg);
        if (pnlCenter != null) pnlCenter.setBackground(bg);
        if (pnlTarjetas != null) pnlTarjetas.setBackground(bg);
        if (pnlInferior != null) pnlInferior.setBackground(bg);
        if (pnlTablaContainer != null) pnlTablaContainer.setBackground(bg);

        if (lblTituloBienvenida != null) lblTituloBienvenida.setForeground(textoTit);
        if (lblSubtituloHero != null) lblSubtituloHero.setForeground(textoMuted);
        if (pnlHero != null) pnlHero.repaint();

        for (JPanel card : listaTarjetasKPI) {
            card.setBackground(cardBg);
            card.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
        }

        for (JLabel l : listaLabelsKPITitulo) l.setForeground(textoMuted);
        for (JLabel l : listaLabelsKPISub) l.setForeground(textoMuted);
        if (lblTotalEventos != null) lblTotalEventos.setForeground(textoTit);
        if (lblProximosEventos != null) lblProximosEventos.setForeground(textoTit);
        if (lblFinalizados != null) lblFinalizados.setForeground(textoTit);
        if (lblEfectividad != null) lblEfectividad.setForeground(textoTit);

        if (lblTituloAgenda != null) lblTituloAgenda.setForeground(textoTit);
        if (lblSubAgenda != null) lblSubAgenda.setForeground(textoMuted);

        if (tblRecientes != null) {
            tblRecientes.setBackground(inputBg);
            tblRecientes.setForeground(textoNorm);
            tblRecientes.getTableHeader().setBackground(tablaHeaderBg);
            tblRecientes.getTableHeader().setForeground(textoTit);
            tblRecientes.setGridColor(gridColor);
            tblRecientes.setSelectionForeground(isDark ? Color.WHITE : textoTit);
        }

        if (scrollTabla != null) {
            scrollTabla.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(0, 0, 0, 0)
            ));
        }

        if (pnlAccionesContainer != null) {
            pnlAccionesContainer.setBackground(cardBg);
            pnlAccionesContainer.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(16, 16, 16, 16)
            ));
        }

        if (lblTitAcciones != null) lblTitAcciones.setForeground(textoTit);
        if (lblSubAcciones != null) lblSubAcciones.setForeground(textoMuted);

        for (JButton btn : listaBotonesAccion) {
            boolean esPrimario = (btn == btnAccionCrear);
            btn.setBackground(esPrimario ? ThemeManager.getColorNaranja() : (isDark ? new Color(38, 38, 48) : new Color(241, 245, 249)));
            btn.setForeground(esPrimario ? Color.WHITE : textoTit);
            btn.setBorder(new CompoundBorder(
                    new LineBorder(esPrimario ? ThemeManager.getColorNaranja() : borde, 1, true),
                    new EmptyBorder(8, 14, 8, 14)
            ));
        }

        if (pnlTip != null) {
            pnlTip.setBackground(isDark ? new Color(255, 117, 31, 14) : new Color(255, 117, 31, 20));
            pnlTip.setBorder(new CompoundBorder(
                    new LineBorder(new Color(255, 117, 31, isDark ? 55 : 80), 1, true),
                    new EmptyBorder(12, 12, 12, 12)
            ));
        }
        if (lblTipText != null) {
            lblTipText.setForeground(isDark ? new Color(220, 210, 195) : new Color(130, 70, 25));
        }

        repaint();
    }

    private void recalcularEfectividad() {
        if (totalActual > 0) {
            int pct = (int) Math.round(((double) finalizadosActual / totalActual) * 100);
            lblEfectividad.setText(pct + "%");
        } else {
            lblEfectividad.setText("0%");
        }
    }

    public void setTotalEventos(int total) {
        this.totalActual = total;
        lblTotalEventos.setText(String.valueOf(total));
        recalcularEfectividad();
    }

    public void setProximosEventos(int total) {
        lblProximosEventos.setText(String.valueOf(total));
    }

    public void setEventosFinalizados(int total) {
        this.finalizadosActual = total;
        lblFinalizados.setText(String.valueOf(total));
        recalcularEfectividad();
    }

    public void setNombreBienvenida(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty() && !nombre.equalsIgnoreCase("Invitado")) {
            lblTituloBienvenida.setText("¡Bienvenido de nuevo, " + nombre + "! 👋");
        } else {
            lblTituloBienvenida.setText("¡Bienvenido al Panel de Control! 👋");
        }
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBtnAccionCrear() { return btnAccionCrear; }
    public JButton getBtnAccionCalendario() { return btnAccionCalendario; }
    public JButton getBtnAccionParticipantes() { return btnAccionParticipantes; }
    public JButton getBtnAccionInformes() { return btnAccionInformes; }
}
