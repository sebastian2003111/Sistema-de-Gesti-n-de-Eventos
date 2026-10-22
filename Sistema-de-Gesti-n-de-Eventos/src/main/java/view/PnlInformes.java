package view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
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

public class PnlInformes extends JPanel implements ThemeManager.ThemeListener {

    // Filtros
    private DatePicker dpDesde;
    private DatePicker dpHasta;
    private JComboBox<String> cbxTipo;
    private JComboBox<String> cbxEstado;
    private JButton btnConsultar;
    private JButton btnLimpiar;

    // Tarjetas (KPIs)
    private JLabel lblTotal;
    private JLabel lblProgramados;
    private JLabel lblEnProceso;
    private JLabel lblFinalizados;
    private JLabel lblCancelados;

    // Gráficos
    private JPanel pnlGraficoTipo;
    private JPanel pnlGraficoEstado;

    // Tabla
    private DefaultTableModel modeloProximosEventos;
    private JTable tblProximosEventos;

    // Acción final
    private JButton btnGenerarInforme;

    // Elementos estructurales
    private JPanel pnlNorte;
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JPanel pnlCentroContenido;
    private JScrollPane scrollCentro;
    private JPanel pnlTarjetas;
    private JPanel pnlFiltros;
    private JPanel pnlGraficos;
    private JPanel pnlTabla;
    private JLabel lblTitTabla;
    private JLabel lblSubTabla;

    private final List<JPanel> listaTarjetasKPI = new ArrayList<>();
    private final List<JLabel> listaLabelsKPITit = new ArrayList<>();
    private final List<JLabel> listaLabelsFiltro = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();
    private final List<JPanel> listaCardsGraficos = new ArrayList<>();
    private final List<JLabel> listaTitulosGraficos = new ArrayList<>();

    public PnlInformes() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(22, 28, 22, 28));

        // NORTE: Título y Subtítulo
        pnlNorte = new JPanel(new GridLayout(2, 1, 0, 3));
        pnlNorte.setOpaque(false);

        lblTitulo = new JLabel("📊  Módulo de Informes, Estadísticas y Auditoría");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Analiza el comportamiento histórico y proyectado de tus eventos con filtros avanzados y exportación ejecutiva.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlNorte.add(lblTitulo);
        pnlNorte.add(lblSub);
        add(pnlNorte, BorderLayout.NORTH);

        // CENTRO: Contenedor con scroll vertical
        pnlCentroContenido = new JPanel();
        pnlCentroContenido.setLayout(new BoxLayout(pnlCentroContenido, BoxLayout.Y_AXIS));
        pnlCentroContenido.setOpaque(false);

        // 1. TARJETAS DE RESUMEN KPI
        pnlCentroContenido.add(crearPanelTarjetas());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 16)));

        // 2. FILTROS AVANZADOS
        pnlCentroContenido.add(crearPanelFiltros());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 16)));

        // 3. ESTADÍSTICAS Y GRÁFICOS
        pnlCentroContenido.add(crearPanelGraficos());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 16)));

        // 4. TABLA DE EVENTOS FILTRADOS
        pnlCentroContenido.add(crearPanelTabla());

        scrollCentro = new JScrollPane(pnlCentroContenido);
        scrollCentro.setBorder(null);
        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollCentro, BorderLayout.CENTER);
    }

    private JPanel crearPanelTarjetas() {
        pnlTarjetas = new JPanel(new GridLayout(1, 5, 12, 0));
        pnlTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        pnlTarjetas.setPreferredSize(new Dimension(0, 95));
        pnlTarjetas.setOpaque(false);

        lblTotal = new JLabel("0");
        lblProgramados = new JLabel("0");
        lblEnProceso = new JLabel("0");
        lblFinalizados = new JLabel("0");
        lblCancelados = new JLabel("0");

        pnlTarjetas.add(crearCardKPI("TOTAL EVENTOS", lblTotal, new Color(52, 152, 219), "📅"));
        pnlTarjetas.add(crearCardKPI("PROGRAMADOS", lblProgramados, new Color(46, 204, 113), "⏳"));
        pnlTarjetas.add(crearCardKPI("EN PROCESO", lblEnProceso, ThemeManager.getColorNaranja(), "⚡"));
        pnlTarjetas.add(crearCardKPI("FINALIZADOS", lblFinalizados, new Color(155, 89, 182), "✅"));
        pnlTarjetas.add(crearCardKPI("CANCELADOS", lblCancelados, new Color(231, 76, 60), "❌"));

        return pnlTarjetas;
    }

    private JPanel crearCardKPI(String titulo, JLabel lblValor, Color acento, String icono) {
        JPanel card = new JPanel(new BorderLayout(8, 0));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel pnlBarra = new JPanel();
        pnlBarra.setBackground(acento);
        pnlBarra.setPreferredSize(new Dimension(0, 3));
        card.add(pnlBarra, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setOpaque(false);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        listaLabelsKPITit.add(lblTit);

        lblValor.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlCenter.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlCenter.add(lblTit);
        pnlCenter.add(Box.createRigidArea(new Dimension(0, 2)));
        pnlCenter.add(lblValor);

        card.add(pnlCenter, BorderLayout.CENTER);

        JLabel lblIco = new JLabel(icono);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        card.add(lblIco, BorderLayout.EAST);

        listaTarjetasKPI.add(card);
        return card;
    }

    private JPanel crearPanelFiltros() {
        pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pnlFiltros.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel lblFiltroTit = new JLabel("🔍 Parámetros:");
        lblFiltroTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lblFiltroTit.setForeground(ThemeManager.getColorNaranja());
        pnlFiltros.add(lblFiltroTit);

        pnlFiltros.add(crearLabelBlanco("Desde:"));
        dpDesde = new DatePicker();
        estilizarDatePicker(dpDesde);
        pnlFiltros.add(dpDesde);

        pnlFiltros.add(crearLabelBlanco("Hasta:"));
        dpHasta = new DatePicker();
        estilizarDatePicker(dpHasta);
        pnlFiltros.add(dpHasta);

        pnlFiltros.add(crearLabelBlanco("Tipo:"));
        cbxTipo = new JComboBox<>(new String[]{"Todos", "Académico", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxTipo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(cbxTipo);

        pnlFiltros.add(crearLabelBlanco("Estado:"));
        cbxEstado = new JComboBox<>(new String[]{"Todos", "Programado", "En proceso", "Finalizado", "Cancelado"});
        cbxEstado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(cbxEstado);

        btnConsultar = crearBotonSimple("Filtrar", new Color(52, 152, 219));
        btnLimpiar = crearBotonSimple("Restablecer", new Color(110, 115, 130));

        pnlFiltros.add(btnConsultar);
        pnlFiltros.add(btnLimpiar);

        return pnlFiltros;
    }

    private JPanel crearPanelGraficos() {
        pnlGraficos = new JPanel(new GridLayout(1, 2, 16, 0));
        pnlGraficos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        pnlGraficos.setPreferredSize(new Dimension(0, 220));
        pnlGraficos.setOpaque(false);

        // Gráfico por Tipo
        JPanel cardTipo = new JPanel(new BorderLayout(0, 10));
        listaCardsGraficos.add(cardTipo);

        JLabel lblTitTipo = new JLabel("📊 Distribución por Tipo de Evento");
        lblTitTipo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        listaTitulosGraficos.add(lblTitTipo);
        cardTipo.add(lblTitTipo, BorderLayout.NORTH);

        pnlGraficoTipo = new JPanel();
        pnlGraficoTipo.setLayout(new BoxLayout(pnlGraficoTipo, BoxLayout.Y_AXIS));
        pnlGraficoTipo.setOpaque(false);
        cardTipo.add(new JScrollPane(pnlGraficoTipo), BorderLayout.CENTER);
        pnlGraficos.add(cardTipo);

        // Gráfico por Estado
        JPanel cardEstado = new JPanel(new BorderLayout(0, 10));
        listaCardsGraficos.add(cardEstado);

        JLabel lblTitEst = new JLabel("📈 Distribución por Estado Operativo");
        lblTitEst.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        listaTitulosGraficos.add(lblTitEst);
        cardEstado.add(lblTitEst, BorderLayout.NORTH);

        pnlGraficoEstado = new JPanel();
        pnlGraficoEstado.setLayout(new BoxLayout(pnlGraficoEstado, BoxLayout.Y_AXIS));
        pnlGraficoEstado.setOpaque(false);
        cardEstado.add(new JScrollPane(pnlGraficoEstado), BorderLayout.CENTER);
        pnlGraficos.add(cardEstado);

        return pnlGraficos;
    }

    private JPanel crearPanelTabla() {
        pnlTabla = new JPanel(new BorderLayout(0, 10));
        pnlTabla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 290));
        pnlTabla.setPreferredSize(new Dimension(0, 290));

        JPanel pnlHeaderTbl = new JPanel(new BorderLayout());
        pnlHeaderTbl.setOpaque(false);

        JPanel pnlTitulosTbl = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitulosTbl.setOpaque(false);

        lblTitTabla = new JLabel("📋 Detalle de Eventos Consolidados");
        lblTitTabla.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        lblSubTabla = new JLabel("Resultados de acuerdo a los criterios y filtros aplicados");
        lblSubTabla.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlTitulosTbl.add(lblTitTabla);
        pnlTitulosTbl.add(lblSubTabla);
        pnlHeaderTbl.add(pnlTitulosTbl, BorderLayout.WEST);

        btnGenerarInforme = new JButton("📥  Exportar Informe (CSV)");
        btnGenerarInforme.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnGenerarInforme.setFocusPainted(false);
        btnGenerarInforme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        listaBotonesSecundarios.add(btnGenerarInforme);
        pnlHeaderTbl.add(btnGenerarInforme, BorderLayout.EAST);

        pnlTabla.add(pnlHeaderTbl, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Tipo", "Fecha", "Estado"};
        modeloProximosEventos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblProximosEventos = new JTable(modeloProximosEventos);
        tblProximosEventos.setRowHeight(34);
        tblProximosEventos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        tblProximosEventos.setShowVerticalLines(false);
        tblProximosEventos.setShowHorizontalLines(true);
        tblProximosEventos.setSelectionBackground(new Color(255, 117, 31, 35));
        tblProximosEventos.getTableHeader().setReorderingAllowed(false);
        tblProximosEventos.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        configurarRenderersTabla();

        JScrollPane sp = new JScrollPane(tblProximosEventos);
        sp.setBorder(null);
        pnlTabla.add(sp, BorderLayout.CENTER);

        return pnlTabla;
    }

    private void configurarRenderersTabla() {
        DefaultTableCellRenderer idR = new DefaultTableCellRenderer();
        idR.setHorizontalAlignment(SwingConstants.CENTER);
        idR.setForeground(new Color(130, 135, 150));
        tblProximosEventos.getColumnModel().getColumn(0).setCellRenderer(idR);
        tblProximosEventos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblProximosEventos.getColumnModel().getColumn(0).setMaxWidth(70);

        DefaultTableCellRenderer normalR = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        tblProximosEventos.getColumnModel().getColumn(1).setCellRenderer(normalR);
        tblProximosEventos.getColumnModel().getColumn(1).setPreferredWidth(200);

        tblProximosEventos.getColumnModel().getColumn(2).setCellRenderer(normalR);
        tblProximosEventos.getColumnModel().getColumn(2).setPreferredWidth(120);

        tblProximosEventos.getColumnModel().getColumn(3).setCellRenderer(normalR);
        tblProximosEventos.getColumnModel().getColumn(3).setPreferredWidth(110);

        tblProximosEventos.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
                p.setOpaque(true);
                boolean isDark = ThemeManager.isDark();
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? (isDark ? new Color(22, 22, 28) : Color.WHITE) : (isDark ? new Color(26, 26, 33) : new Color(248, 250, 252))));

                String est = value != null ? value.toString() : "Programado";
                JLabel pill = new JLabel(" " + est + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
                pill.setOpaque(true);

                if ("Finalizado".equalsIgnoreCase(est)) {
                    pill.setBackground(new Color(46, 204, 113, 35));
                    pill.setForeground(new Color(39, 174, 96));
                    pill.setBorder(new LineBorder(new Color(46, 204, 113, 90), 1, true));
                } else if ("En proceso".equalsIgnoreCase(est) || "En curso".equalsIgnoreCase(est)) {
                    pill.setBackground(new Color(255, 117, 31, 35));
                    pill.setForeground(ThemeManager.getColorNaranja());
                    pill.setBorder(new LineBorder(new Color(255, 117, 31, 90), 1, true));
                } else {
                    pill.setBackground(new Color(52, 152, 219, 35));
                    pill.setForeground(new Color(52, 152, 219));
                    pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                }

                p.add(pill);
                return p;
            }
        });
        tblProximosEventos.getColumnModel().getColumn(4).setPreferredWidth(110);
    }

    private void estilizarDatePicker(DatePicker picker) {
        DatePickerSettings s = picker.getSettings();
        s.setFontValidDate(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        picker.setPreferredSize(new Dimension(140, 32));
    }

    private JLabel crearLabelBlanco(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        listaLabelsFiltro.add(l);
        return l;
    }

    private JButton crearBotonSimple(String texto, Color acento) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(acento);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249));
                btn.setForeground(ThemeManager.getColorTextoTitulo());
            }
        });
        listaBotonesSecundarios.add(btn);
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
        if (scrollCentro != null) scrollCentro.getViewport().setBackground(bg);

        if (lblTitulo != null) lblTitulo.setForeground(textoTit);
        if (lblSub != null) lblSub.setForeground(textoMuted);

        for (JPanel card : listaTarjetasKPI) {
            card.setBackground(cardBg);
            card.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(10, 14, 10, 14)
            ));
        }

        for (JLabel l : listaLabelsKPITit) l.setForeground(textoMuted);
        if (lblTotal != null) lblTotal.setForeground(textoTit);
        if (lblProgramados != null) lblProgramados.setForeground(textoTit);
        if (lblEnProceso != null) lblEnProceso.setForeground(textoTit);
        if (lblFinalizados != null) lblFinalizados.setForeground(textoTit);
        if (lblCancelados != null) lblCancelados.setForeground(textoTit);

        if (pnlFiltros != null) {
            pnlFiltros.setBackground(cardBg);
            pnlFiltros.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(4, 12, 4, 12)
            ));
        }

        for (JLabel l : listaLabelsFiltro) l.setForeground(textoMuted);

        if (cbxTipo != null) {
            cbxTipo.setBackground(inputBg);
            cbxTipo.setForeground(textoNorm);
        }
        if (cbxEstado != null) {
            cbxEstado.setBackground(inputBg);
            cbxEstado.setForeground(textoNorm);
        }

        for (JPanel cg : listaCardsGraficos) {
            cg.setBackground(cardBg);
            cg.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
        }

        for (JLabel l : listaTitulosGraficos) l.setForeground(textoTit);

        if (pnlTabla != null) {
            pnlTabla.setBackground(cardBg);
            pnlTabla.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
        }

        if (lblTitTabla != null) lblTitTabla.setForeground(textoTit);
        if (lblSubTabla != null) lblSubTabla.setForeground(textoMuted);

        if (tblProximosEventos != null) {
            tblProximosEventos.setBackground(inputBg);
            tblProximosEventos.setForeground(textoNorm);
            tblProximosEventos.getTableHeader().setBackground(tablaHeaderBg);
            tblProximosEventos.getTableHeader().setForeground(textoTit);
            tblProximosEventos.setGridColor(gridColor);
            tblProximosEventos.setSelectionForeground(isDark ? Color.WHITE : textoTit);
        }

        for (JButton b : listaBotonesSecundarios) {
            b.setBackground(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
            b.setForeground(textoTit);
            b.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(5, 12, 5, 12)
            ));
        }

        repaint();
    }

    // --- Getters ---
    public DatePicker getDpDesde() { return dpDesde; }
    public DatePicker getDpHasta() { return dpHasta; }
    public JComboBox<String> getCbxTipo() { return cbxTipo; }
    public JComboBox<String> getCbxEstado() { return cbxEstado; }
    public JButton getBtnConsultar() { return btnConsultar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JLabel getLblTotal() { return lblTotal; }
    public JLabel getLblProgramados() { return lblProgramados; }
    public JLabel getLblEnProceso() { return lblEnProceso; }
    public JLabel getLblFinalizados() { return lblFinalizados; }
    public JLabel getLblCancelados() { return lblCancelados; }
    public JPanel getPnlGraficoTipo() { return pnlGraficoTipo; }
    public JPanel getPnlGraficoEstado() { return pnlGraficoEstado; }
    public DefaultTableModel getModeloProximosEventos() { return modeloProximosEventos; }
    public JButton getBtnGenerarInforme() { return btnGenerarInforme; }
}
