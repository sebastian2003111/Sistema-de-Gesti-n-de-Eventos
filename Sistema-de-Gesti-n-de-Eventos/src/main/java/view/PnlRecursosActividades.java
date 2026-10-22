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

public class PnlRecursosActividades extends JPanel implements ThemeManager.ThemeListener {

    // Actividades
    private JTextField txtActNombre;
    private JTextField txtActHorario;
    private JTextField txtActResponsable;
    private JComboBox<String> cbxActEvento;
    private JButton btnAgregarActividad;
    private JButton btnActualizarActividad;
    private JButton btnEliminarActividad;
    private JButton btnLimpiarActividad;
    private DefaultTableModel modeloActividades;
    private JTable tblActividades;

    // Recursos
    private JTextField txtRecTipo;
    private JSpinner spRecCantidad;
    private JComboBox<String> cbxRecEvento;
    private JButton btnAgregarRecurso;
    private JButton btnActualizarRecurso;
    private JButton btnEliminarRecurso;
    private JButton btnLimpiarRecurso;
    private DefaultTableModel modeloRecursos;
    private JTable tblRecursos;

    // Elementos estructurales para re-theming
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JTabbedPane tabbedPane;
    private final List<JPanel> listaWrappers = new ArrayList<>();
    private final List<JPanel> listaPanelesCard = new ArrayList<>();
    private final List<JLabel> listaTitulosCard = new ArrayList<>();
    private final List<JLabel> listaLabelsForm = new ArrayList<>();
    private final List<JTextField> listaTextFields = new ArrayList<>();
    private final List<JScrollPane> listaScrolls = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();

    public PnlRecursosActividades() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Título del módulo
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1, 0, 3));
        pnlHeader.setOpaque(false);

        lblTitulo = new JLabel("📋  Logística: Cronograma de Actividades y Recursos");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Planifica las fases internas de cada evento y gestiona el inventario de equipamiento y suministros requeridos.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlHeader.add(lblTitulo);
        pnlHeader.add(lblSub);
        add(pnlHeader, BorderLayout.NORTH);

        // JTabbedPane Estilizado
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        tabbedPane.addTab("🗓️  Actividades y Cronograma", crearPanelActividades());
        tabbedPane.addTab("📦  Inventario de Recursos", crearPanelRecursos());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelActividades() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBorder(new EmptyBorder(16, 10, 10, 10));
        listaWrappers.add(pnl);

        // Formulario Lateral
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setPreferredSize(new Dimension(340, 0));
        listaPanelesCard.add(pnlForm);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 15, 0);
        JLabel lblTitAct = new JLabel("📝 Registrar Actividad");
        lblTitAct.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        listaTitulosCard.add(lblTitAct);
        pnlForm.add(lblTitAct, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 0, 4, 0);
        pnlForm.add(crearLabel("Nombre de la Actividad *"), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 12, 0);
        txtActNombre = crearTextField("Ej. Registro y Acreditaciones");
        pnlForm.add(txtActNombre, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        pnlForm.add(crearLabel("Horario Estimado *"), gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 12, 0);
        txtActHorario = crearTextField("Ej. 08:00 AM - 09:30 AM");
        pnlForm.add(txtActHorario, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        pnlForm.add(crearLabel("Líder / Responsable *"), gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 12, 0);
        txtActResponsable = crearTextField("Ej. Lic. Marina Duarte");
        pnlForm.add(txtActResponsable, gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 4, 0);
        pnlForm.add(crearLabel("Evento Vinculado *"), gbc);

        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 18, 0);
        cbxActEvento = new JComboBox<>(new String[]{"Cargando eventos..."});
        cbxActEvento.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        cbxActEvento.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(cbxActEvento, gbc);

        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 8, 0);
        btnAgregarActividad = crearBotonAccion("➕  Agregar Actividad", ThemeManager.getColorNaranja(), true);
        pnlForm.add(btnAgregarActividad, gbc);

        gbc.gridy = 10; gbc.insets = new Insets(0, 0, 8, 0);
        btnActualizarActividad = crearBotonAccion("🔄  Modificar Selección", new Color(52, 152, 219), false);
        pnlForm.add(btnActualizarActividad, gbc);

        gbc.gridy = 11; gbc.insets = new Insets(0, 0, 8, 0);
        btnEliminarActividad = crearBotonAccion("🗑️  Eliminar Actividad", new Color(231, 76, 60), false);
        pnlForm.add(btnEliminarActividad, gbc);

        gbc.gridy = 12; gbc.insets = new Insets(0, 0, 0, 0);
        btnLimpiarActividad = crearBotonAccion("Limpiar Campos", new Color(110, 115, 130), false);
        pnlForm.add(btnLimpiarActividad, gbc);

        pnl.add(pnlForm, BorderLayout.WEST);

        // Tabla de Actividades
        JPanel pnlTabla = new JPanel(new BorderLayout(0, 10));
        listaPanelesCard.add(pnlTabla);

        JLabel lblTitTabla = new JLabel("🗓️  Cronograma de Actividades Programadas");
        lblTitTabla.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        listaTitulosCard.add(lblTitTabla);
        pnlTabla.add(lblTitTabla, BorderLayout.NORTH);

        String[] cols = {"ID", "Actividad", "Horario", "Responsable", "Evento"};
        modeloActividades = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblActividades = new JTable(modeloActividades);
        tblActividades.setRowHeight(38);
        tblActividades.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tblActividades.setShowVerticalLines(false);
        tblActividades.setShowHorizontalLines(true);
        tblActividades.setSelectionBackground(new Color(255, 117, 31, 35));
        tblActividades.getTableHeader().setReorderingAllowed(false);
        tblActividades.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        configurarRenderersActividades();

        JScrollPane sp = new JScrollPane(tblActividades);
        listaScrolls.add(sp);
        pnlTabla.add(sp, BorderLayout.CENTER);

        pnl.add(pnlTabla, BorderLayout.CENTER);
        return pnl;
    }

    private void configurarRenderersActividades() {
        DefaultTableCellRenderer idR = new DefaultTableCellRenderer();
        idR.setHorizontalAlignment(SwingConstants.CENTER);
        idR.setForeground(new Color(130, 135, 150));
        tblActividades.getColumnModel().getColumn(0).setCellRenderer(idR);
        tblActividades.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblActividades.getColumnModel().getColumn(0).setMaxWidth(60);

        DefaultTableCellRenderer normalR = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        tblActividades.getColumnModel().getColumn(1).setCellRenderer(normalR);
        tblActividades.getColumnModel().getColumn(1).setPreferredWidth(170);

        tblActividades.getColumnModel().getColumn(2).setCellRenderer(normalR);
        tblActividades.getColumnModel().getColumn(2).setPreferredWidth(130);

        tblActividades.getColumnModel().getColumn(3).setCellRenderer(normalR);
        tblActividades.getColumnModel().getColumn(3).setPreferredWidth(140);

        tblActividades.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
                p.setOpaque(true);
                boolean isDark = ThemeManager.isDark();
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? (isDark ? new Color(22, 22, 28) : Color.WHITE) : (isDark ? new Color(26, 26, 33) : new Color(248, 250, 252))));

                String ev = value != null ? value.toString() : "General";
                JLabel pill = new JLabel(" 📅 " + ev + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
                pill.setOpaque(true);
                pill.setBackground(new Color(52, 152, 219, 35));
                pill.setForeground(new Color(52, 152, 219));
                pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                p.add(pill);
                return p;
            }
        });
        tblActividades.getColumnModel().getColumn(4).setPreferredWidth(150);
    }

    private JPanel crearPanelRecursos() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBorder(new EmptyBorder(16, 10, 10, 10));
        listaWrappers.add(pnl);

        // Formulario Lateral
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setPreferredSize(new Dimension(340, 0));
        listaPanelesCard.add(pnlForm);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 15, 0);
        JLabel lblTitRec = new JLabel("📦 Asignar Recurso / Equipo");
        lblTitRec.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        listaTitulosCard.add(lblTitRec);
        pnlForm.add(lblTitRec, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 0, 4, 0);
        pnlForm.add(crearLabel("Descripción o Tipo de Recurso *"), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 12, 0);
        txtRecTipo = crearTextField("Ej. Micrófono Inalámbrico Shure");
        pnlForm.add(txtRecTipo, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        pnlForm.add(crearLabel("Cantidad Requerida *"), gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 12, 0);
        spRecCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        spRecCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spRecCantidad.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(spRecCantidad, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        pnlForm.add(crearLabel("Evento Vinculado *"), gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 18, 0);
        cbxRecEvento = new JComboBox<>(new String[]{"Cargando eventos..."});
        cbxRecEvento.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        cbxRecEvento.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(cbxRecEvento, gbc);

        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 8, 0);
        btnAgregarRecurso = crearBotonAccion("➕  Asignar Recurso", ThemeManager.getColorNaranja(), true);
        pnlForm.add(btnAgregarRecurso, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 8, 0);
        btnActualizarRecurso = crearBotonAccion("🔄  Modificar Cantidad / Tipo", new Color(52, 152, 219), false);
        pnlForm.add(btnActualizarRecurso, gbc);

        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 8, 0);
        btnEliminarRecurso = crearBotonAccion("🗑️  Quitar Recurso", new Color(231, 76, 60), false);
        pnlForm.add(btnEliminarRecurso, gbc);

        gbc.gridy = 10; gbc.insets = new Insets(0, 0, 0, 0);
        btnLimpiarRecurso = crearBotonAccion("Limpiar Campos", new Color(110, 115, 130), false);
        pnlForm.add(btnLimpiarRecurso, gbc);

        pnl.add(pnlForm, BorderLayout.WEST);

        // Tabla de Recursos
        JPanel pnlTabla = new JPanel(new BorderLayout(0, 10));
        listaPanelesCard.add(pnlTabla);

        JLabel lblTitTabla = new JLabel("📦  Inventario de Recursos y Equipamiento por Evento");
        lblTitTabla.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        listaTitulosCard.add(lblTitTabla);
        pnlTabla.add(lblTitTabla, BorderLayout.NORTH);

        String[] cols = {"ID", "Tipo de Recurso / Equipo", "Cantidad", "Evento Asociado"};
        modeloRecursos = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblRecursos = new JTable(modeloRecursos);
        tblRecursos.setRowHeight(38);
        tblRecursos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tblRecursos.setShowVerticalLines(false);
        tblRecursos.setShowHorizontalLines(true);
        tblRecursos.setSelectionBackground(new Color(255, 117, 31, 35));
        tblRecursos.getTableHeader().setReorderingAllowed(false);
        tblRecursos.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        configurarRenderersRecursos();

        JScrollPane sp = new JScrollPane(tblRecursos);
        listaScrolls.add(sp);
        pnlTabla.add(sp, BorderLayout.CENTER);

        pnl.add(pnlTabla, BorderLayout.CENTER);
        return pnl;
    }

    private void configurarRenderersRecursos() {
        DefaultTableCellRenderer idR = new DefaultTableCellRenderer();
        idR.setHorizontalAlignment(SwingConstants.CENTER);
        idR.setForeground(new Color(130, 135, 150));
        tblRecursos.getColumnModel().getColumn(0).setCellRenderer(idR);
        tblRecursos.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblRecursos.getColumnModel().getColumn(0).setMaxWidth(60);

        DefaultTableCellRenderer normalR = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        tblRecursos.getColumnModel().getColumn(1).setCellRenderer(normalR);
        tblRecursos.getColumnModel().getColumn(1).setPreferredWidth(210);

        DefaultTableCellRenderer cantR = new DefaultTableCellRenderer();
        cantR.setHorizontalAlignment(SwingConstants.CENTER);
        cantR.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cantR.setForeground(ThemeManager.getColorNaranja());
        tblRecursos.getColumnModel().getColumn(2).setCellRenderer(cantR);
        tblRecursos.getColumnModel().getColumn(2).setPreferredWidth(80);

        tblRecursos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
                p.setOpaque(true);
                boolean isDark = ThemeManager.isDark();
                p.setBackground(isSelected ? table.getSelectionBackground() : ((row % 2 == 0) ? (isDark ? new Color(22, 22, 28) : Color.WHITE) : (isDark ? new Color(26, 26, 33) : new Color(248, 250, 252))));

                String ev = value != null ? value.toString() : "General";
                JLabel pill = new JLabel(" 📅 " + ev + " ");
                pill.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
                pill.setOpaque(true);
                pill.setBackground(new Color(52, 152, 219, 35));
                pill.setForeground(new Color(52, 152, 219));
                pill.setBorder(new LineBorder(new Color(52, 152, 219, 90), 1, true));
                p.add(pill);
                return p;
            }
        });
        tblRecursos.getColumnModel().getColumn(3).setPreferredWidth(160);
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        listaLabelsForm.add(l);
        return l;
    }

    private JTextField crearTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 36));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        listaTextFields.add(tf);
        return tf;
    }

    private JButton crearBotonAccion(String texto, Color acento, boolean esPrimario) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 36));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    if (esPrimario) {
                        btn.setBackground(ThemeManager.getColorNaranjaHover());
                    } else {
                        btn.setBackground(ThemeManager.isDark() ? new Color(48, 48, 62) : new Color(228, 233, 240));
                        btn.setBorder(new CompoundBorder(
                                new LineBorder(acento, 1, true),
                                new EmptyBorder(6, 12, 6, 12)
                        ));
                    }
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(esPrimario ? ThemeManager.getColorNaranja() : (ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249)));
                    btn.setForeground(esPrimario ? Color.WHITE : ThemeManager.getColorTextoTitulo());
                    btn.setBorder(new CompoundBorder(
                            new LineBorder(esPrimario ? ThemeManager.getColorNaranja() : ThemeManager.getColorBorde(), 1, true),
                            new EmptyBorder(6, 12, 6, 12)
                    ));
                }
            }
        });

        if (!esPrimario) {
            listaBotonesSecundarios.add(btn);
        }
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
        Color inputBorde = ThemeManager.getColorInputBorder();
        Color tablaHeaderBg = ThemeManager.getColorTablaHeaderBg();
        Color gridColor = ThemeManager.getColorTablaGrid();

        setBackground(bg);
        if (lblTitulo != null) lblTitulo.setForeground(textoTit);
        if (lblSub != null) lblSub.setForeground(textoMuted);

        if (tabbedPane != null) {
            tabbedPane.setBackground(cardBg);
            tabbedPane.setForeground(textoTit);
        }

        for (JPanel w : listaWrappers) {
            w.setBackground(bg);
        }

        for (JPanel p : listaPanelesCard) {
            p.setBackground(cardBg);
            p.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(16, 20, 16, 20)
            ));
        }

        for (JLabel l : listaTitulosCard) l.setForeground(textoTit);
        for (JLabel l : listaLabelsForm) l.setForeground(textoMuted);

        for (JTextField tf : listaTextFields) {
            tf.setBackground(inputBg);
            tf.setForeground(textoNorm);
            tf.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(inputBorde, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));
        }

        if (cbxActEvento != null) {
            cbxActEvento.setBackground(inputBg);
            cbxActEvento.setForeground(textoNorm);
        }
        if (cbxRecEvento != null) {
            cbxRecEvento.setBackground(inputBg);
            cbxRecEvento.setForeground(textoNorm);
        }

        if (tblActividades != null) {
            tblActividades.setBackground(inputBg);
            tblActividades.setForeground(textoNorm);
            tblActividades.getTableHeader().setBackground(tablaHeaderBg);
            tblActividades.getTableHeader().setForeground(textoTit);
            tblActividades.setGridColor(gridColor);
            tblActividades.setSelectionForeground(isDark ? Color.WHITE : textoTit);
        }

        if (tblRecursos != null) {
            tblRecursos.setBackground(inputBg);
            tblRecursos.setForeground(textoNorm);
            tblRecursos.getTableHeader().setBackground(tablaHeaderBg);
            tblRecursos.getTableHeader().setForeground(textoTit);
            tblRecursos.setGridColor(gridColor);
            tblRecursos.setSelectionForeground(isDark ? Color.WHITE : textoTit);
        }

        for (JScrollPane sp : listaScrolls) {
            sp.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(0, 0, 0, 0)
            ));
        }

        for (JButton btn : listaBotonesSecundarios) {
            btn.setBackground(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
            btn.setForeground(textoTit);
            btn.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));
        }

        if (btnAgregarActividad != null) {
            btnAgregarActividad.setBackground(ThemeManager.getColorNaranja());
            btnAgregarActividad.setForeground(Color.WHITE);
        }
        if (btnAgregarRecurso != null) {
            btnAgregarRecurso.setBackground(ThemeManager.getColorNaranja());
            btnAgregarRecurso.setForeground(Color.WHITE);
        }

        repaint();
    }

    // Getters Actividades
    public JTextField getTxtActNombre() { return txtActNombre; }
    public JTextField getTxtActHorario() { return txtActHorario; }
    public JTextField getTxtActResponsable() { return txtActResponsable; }
    public JComboBox<String> getCbxActEvento() { return cbxActEvento; }
    public JButton getBtnAgregarActividad() { return btnAgregarActividad; }
    public JButton getBtnActualizarActividad() { return btnActualizarActividad; }
    public JButton getBtnEliminarActividad() { return btnEliminarActividad; }
    public JButton getBtnLimpiarActividad() { return btnLimpiarActividad; }
    public JTable getTblActividades() { return tblActividades; }
    public DefaultTableModel getModeloActividades() { return modeloActividades; }

    // Getters Recursos
    public JTextField getTxtRecTipo() { return txtRecTipo; }
    public JSpinner getSpRecCantidad() { return spRecCantidad; }
    public JComboBox<String> getCbxRecEvento() { return cbxRecEvento; }
    public JButton getBtnAgregarRecurso() { return btnAgregarRecurso; }
    public JButton getBtnActualizarRecurso() { return btnActualizarRecurso; }
    public JButton getBtnEliminarRecurso() { return btnEliminarRecurso; }
    public JButton getBtnLimpiarRecurso() { return btnLimpiarRecurso; }
    public JTable getTblRecursos() { return tblRecursos; }
    public DefaultTableModel getModeloRecursos() { return modeloRecursos; }
}
