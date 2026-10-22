package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmListarEventos extends JFrame {

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnCerrar;

    // Colores dinámicos
    private final Color COLOR_BG = config.ThemeManager.getColorBg();
    private final Color COLOR_CARD_BG = config.ThemeManager.getColorCardBg();
    private final Color COLOR_BORDE = config.ThemeManager.getColorBorde();
    private final Color COLOR_NARANJA = config.ThemeManager.getColorNaranja();
    private final Color COLOR_TEXTO_TITULO = config.ThemeManager.getColorTextoTitulo();
    private final Color COLOR_TEXTO_MUTED = config.ThemeManager.getColorTextoMuted();

    public FrmListarEventos() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Catálogo General de Eventos");
        setSize(940, 540);
        setMinimumSize(new Dimension(800, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/LOGO_SGE.png"));
            setIconImage(icon);
        } catch (Exception ignored) {}

        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(22, 26, 22, 26));

        // ==========================================
        // 1. HEADER
        // ==========================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 3));
        pnlTitulos.setBackground(COLOR_BG);

        JLabel lblTitulo = new JLabel("📋  Catálogo General de Eventos");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);

        JLabel lblSub = new JLabel("Consulta tabular de todos los eventos programados, en proceso y finalizados.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSub.setForeground(COLOR_TEXTO_MUTED);

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        pnlHeader.add(pnlTitulos, BorderLayout.WEST);

        mainPanel.add(pnlHeader, BorderLayout.NORTH);

        // ==========================================
        // 2. TABLA ESTILIZADA
        // ==========================================
        String[] columnas = {"ID", "Nombre del Evento", "Tipo", "Fecha", "Hora", "Lugar / Sede", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setRowHeight(38);
        tablaEventos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tablaEventos.setShowVerticalLines(false);
        tablaEventos.setShowHorizontalLines(true);
        tablaEventos.setGridColor(new Color(40, 40, 52));
        tablaEventos.setSelectionBackground(new Color(255, 117, 31, 40));
        tablaEventos.setSelectionForeground(Color.WHITE);
        tablaEventos.setBackground(new Color(22, 22, 28));
        tablaEventos.setForeground(COLOR_TEXTO_TITULO);

        // Header de la tabla
        tablaEventos.getTableHeader().setReorderingAllowed(false);
        tablaEventos.getTableHeader().setBackground(new Color(32, 32, 42));
        tablaEventos.getTableHeader().setForeground(COLOR_TEXTO_TITULO);
        tablaEventos.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        tablaEventos.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tablaEventos.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDE));

        configurarRenderizadoresTabla();

        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        scrollPane.getViewport().setBackground(new Color(22, 22, 28));
        scrollPane.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. BARRA INFERIOR DE BOTONES
        // ==========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);

        btnActualizar = new JButton("🔄  Recargar Lista");
        btnActualizar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setBackground(COLOR_NARANJA);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBorder(new CompoundBorder(
                new LineBorder(COLOR_NARANJA, 1, true),
                new EmptyBorder(8, 18, 8, 18)
        ));
        btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnActualizar.setBackground(new Color(255, 135, 60)); }
            @Override
            public void mouseExited(MouseEvent e) { btnActualizar.setBackground(COLOR_NARANJA); }
        });

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBackground(new Color(38, 38, 48));
        btnCerrar.setForeground(COLOR_TEXTO_TITULO);
        btnCerrar.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 18, 8, 18)
        ));
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(48, 48, 62));
                btnCerrar.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(38, 38, 48));
                btnCerrar.setForeground(COLOR_TEXTO_TITULO);
            }
        });

        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void configurarRenderizadoresTabla() {
        // ID
        DefaultTableCellRenderer idRenderer = new DefaultTableCellRenderer();
        idRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        idRenderer.setForeground(new Color(130, 135, 150));
        tablaEventos.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEventos.getColumnModel().getColumn(0).setMaxWidth(70);

        // Nombre
        DefaultTableCellRenderer nomRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
                c.setForeground(COLOR_TEXTO_TITULO);
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tablaEventos.getColumnModel().getColumn(1).setCellRenderer(nomRenderer);
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(170);

        // Tipo Pill
        tablaEventos.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
                p.setOpaque(true);
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? new Color(22, 22, 28) : new Color(26, 26, 33)));

                String tipo = value != null ? value.toString() : "General";
                JLabel pill = new JLabel(" " + tipo + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
                pill.setOpaque(true);
                pill.setBackground(new Color(52, 152, 219, 35));
                pill.setForeground(new Color(90, 180, 255));
                pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                p.add(pill);
                return p;
            }
        });
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(110);

        // Fecha
        DefaultTableCellRenderer fechaRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, "📅 " + (value != null ? value.toString() : ""), isSelected, hasFocus, row, column);
                c.setForeground(new Color(200, 205, 215));
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tablaEventos.getColumnModel().getColumn(3).setCellRenderer(fechaRenderer);
        tablaEventos.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Hora
        DefaultTableCellRenderer horaRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, "🕒 " + (value != null ? value.toString() : ""), isSelected, hasFocus, row, column);
                c.setForeground(new Color(180, 185, 200));
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tablaEventos.getColumnModel().getColumn(4).setCellRenderer(horaRenderer);
        tablaEventos.getColumnModel().getColumn(4).setPreferredWidth(85);

        // Lugar
        DefaultTableCellRenderer lugarRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, "📍 " + (value != null ? value.toString() : ""), isSelected, hasFocus, row, column);
                c.setForeground(new Color(175, 180, 195));
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        tablaEventos.getColumnModel().getColumn(5).setCellRenderer(lugarRenderer);
        tablaEventos.getColumnModel().getColumn(5).setPreferredWidth(130);

        // Estado Pill Badge
        tablaEventos.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
                p.setOpaque(true);
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? new Color(22, 22, 28) : new Color(26, 26, 33)));

                String estado = value != null ? value.toString() : "Programado";
                JLabel pill = new JLabel(" " + estado + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
                pill.setOpaque(true);

                if (estado.equalsIgnoreCase("Finalizado")) {
                    pill.setBackground(new Color(46, 204, 113, 35));
                    pill.setForeground(new Color(80, 220, 130));
                    pill.setBorder(new LineBorder(new Color(46, 204, 113, 90), 1, true));
                } else if (estado.equalsIgnoreCase("En curso") || estado.equalsIgnoreCase("En proceso")) {
                    pill.setBackground(new Color(255, 117, 31, 35));
                    pill.setForeground(new Color(255, 140, 60));
                    pill.setBorder(new LineBorder(new Color(255, 117, 31, 90), 1, true));
                } else if (estado.equalsIgnoreCase("Cancelado")) {
                    pill.setBackground(new Color(231, 76, 60, 35));
                    pill.setForeground(new Color(255, 100, 90));
                    pill.setBorder(new LineBorder(new Color(231, 76, 60, 90), 1, true));
                } else {
                    pill.setBackground(new Color(52, 152, 219, 35));
                    pill.setForeground(new Color(90, 180, 255));
                    pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                }

                p.add(pill);
                return p;
            }
        });
        tablaEventos.getColumnModel().getColumn(6).setPreferredWidth(110);
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnCerrar() { return btnCerrar; }
}
