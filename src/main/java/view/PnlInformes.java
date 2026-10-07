package view;

import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public class PnlInformes extends JPanel {

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

    public PnlInformes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // NORTE: Título y Subtítulo
        JPanel pnlNorte = new JPanel(new GridLayout(2, 1));
        pnlNorte.setBackground(new Color(25, 25, 25));
        JLabel lblTitulo = new JLabel("📊 INFORMES Y ESTADÍSTICAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Consulta el comportamiento de tus eventos");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSub.setForeground(Color.LIGHT_GRAY);
        pnlNorte.add(lblTitulo);
        pnlNorte.add(lblSub);
        add(pnlNorte, BorderLayout.NORTH);

        // CENTRO: Contenedor scrolleable
        JPanel pnlCentroContenido = new JPanel();
        pnlCentroContenido.setLayout(new BoxLayout(pnlCentroContenido, BoxLayout.Y_AXIS));
        pnlCentroContenido.setBackground(new Color(25, 25, 25));

        // 1. TARJETAS DE RESUMEN
        pnlCentroContenido.add(crearPanelTarjetas());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. FILTROS
        pnlCentroContenido.add(crearPanelFiltros());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. ESTADÍSTICAS (Gráficos)
        pnlCentroContenido.add(crearPanelGraficos());
        pnlCentroContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. PRÓXIMOS EVENTOS (Tabla)
        pnlCentroContenido.add(crearPanelTabla());

        JScrollPane scrollCentro = new JScrollPane(pnlCentroContenido);
        scrollCentro.setBorder(null);
        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollCentro, BorderLayout.CENTER);

        // SUR: Botón Generar Informe
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.setBackground(new Color(25, 25, 25));
        btnGenerarInforme = new JButton("📄 Generar informe");
        btnGenerarInforme.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerarInforme.setBackground(new Color(255, 117, 31));
        btnGenerarInforme.setForeground(Color.WHITE);
        btnGenerarInforme.setFocusPainted(false);
        pnlSur.add(btnGenerarInforme);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTarjetas() {
        JPanel pnl = new JPanel(new GridLayout(1, 5, 15, 0));
        pnl.setBackground(new Color(25, 25, 25));

        lblTotal = crearTarjeta(pnl, "Total", new Color(41, 128, 185));
        lblProgramados = crearTarjeta(pnl, "Programados", new Color(39, 174, 96));
        lblEnProceso = crearTarjeta(pnl, "En Proceso", new Color(243, 156, 18));
        lblFinalizados = crearTarjeta(pnl, "Finalizados", new Color(142, 68, 173));
        lblCancelados = crearTarjeta(pnl, "Cancelados", new Color(192, 57, 43));

        return pnl;
    }

    private JLabel crearTarjeta(JPanel contenedor, String titulo, Color colorBorde) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(35, 35, 35));
        card.setBorder(new CompoundBorder(
                new LineBorder(colorBorde, 3, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblT = new JLabel(titulo, SwingConstants.CENTER);
        lblT.setForeground(Color.LIGHT_GRAY);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        card.add(lblT, BorderLayout.NORTH);

        JLabel lblValor = new JLabel("0", SwingConstants.CENTER);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(lblValor, BorderLayout.CENTER);

        contenedor.add(card);
        return lblValor;
    }

    private JPanel crearPanelFiltros() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnl.setBackground(new Color(35, 35, 35));
        pnl.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(60, 60, 60)), "Filtros", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));

        dpDesde = new DatePicker();
        dpHasta = new DatePicker();
        cbxTipo = new JComboBox<>(new String[]{"Todos", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxEstado = new JComboBox<>(new String[]{"Todos", "Programado", "En proceso", "Finalizado", "Cancelado"});
        
        btnConsultar = new JButton("Consultar");
        btnConsultar.setBackground(new Color(50, 150, 250));
        btnConsultar.setForeground(Color.WHITE);
        
        btnLimpiar = new JButton("Limpiar filtros");
        btnLimpiar.setBackground(new Color(100, 100, 100));
        btnLimpiar.setForeground(Color.WHITE);

        pnl.add(crearLabelBlanco("Desde:")); pnl.add(dpDesde);
        pnl.add(crearLabelBlanco("Hasta:")); pnl.add(dpHasta);
        pnl.add(crearLabelBlanco("Tipo:")); pnl.add(cbxTipo);
        pnl.add(crearLabelBlanco("Estado:")); pnl.add(cbxEstado);
        pnl.add(btnConsultar);
        pnl.add(btnLimpiar);

        return pnl;
    }

    private JPanel crearPanelGraficos() {
        JPanel pnl = new JPanel(new GridLayout(1, 2, 20, 0));
        pnl.setBackground(new Color(25, 25, 25));

        pnlGraficoTipo = new JPanel();
        pnlGraficoTipo.setLayout(new BoxLayout(pnlGraficoTipo, BoxLayout.Y_AXIS));
        pnlGraficoTipo.setBackground(new Color(35, 35, 35));
        pnlGraficoTipo.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(60, 60, 60)), "Eventos por Tipo", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));

        pnlGraficoEstado = new JPanel();
        pnlGraficoEstado.setLayout(new BoxLayout(pnlGraficoEstado, BoxLayout.Y_AXIS));
        pnlGraficoEstado.setBackground(new Color(35, 35, 35));
        pnlGraficoEstado.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(60, 60, 60)), "Eventos por Estado", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));

        pnl.add(pnlGraficoTipo);
        pnl.add(pnlGraficoEstado);
        return pnl;
    }

    private JPanel crearPanelTabla() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(new Color(35, 35, 35));
        pnl.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(60, 60, 60)), "Próximos Eventos", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));

        modeloProximosEventos = new DefaultTableModel(new String[]{"Fecha", "Nombre", "Tipo", "Lugar", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProximosEventos = new JTable(modeloProximosEventos);
        tblProximosEventos.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tblProximosEventos);
        scroll.setPreferredSize(new Dimension(0, 150));
        pnl.add(scroll, BorderLayout.CENTER);
        return pnl;
    }

    private JLabel crearLabelBlanco(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        return l;
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
