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

public class PnlParticipantes extends JPanel implements ThemeManager.ThemeListener {

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JComboBox<String> cbxEventos;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnExportar;
    private JTable tblParticipantes;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;

    // Componentes estructurales
    private JPanel pnlHeader;
    private JPanel pnlTitulos;
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JPanel pnlFormCard;
    private JLabel lblFormTit;
    private JLabel lblNom;
    private JLabel lblCor;
    private JLabel lblEvt;
    private JPanel pnlTablaCard;
    private JLabel lblTablaTit;
    private JLabel lblTablaSub;

    private final List<JTextField> listaTextFields = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();

    public PnlParticipantes() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ==========================================
        // 1. TÍTULO Y DESCRIPCIÓN SUPERIOR
        // ==========================================
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTitulos.setOpaque(false);

        lblTitulo = new JLabel("👥  Gestión y Registro de Participantes");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Inscribe asistentes, gestiona invitaciones automáticas por correo electrónico y exporta listados oficiales.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        pnlHeader.add(pnlTitulos, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // ==========================================
        // 2. FORMULARIO LATERAL IZQUIERDO
        // ==========================================
        pnlFormCard = new JPanel(new GridBagLayout());
        pnlFormCard.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        // Título del formulario
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 15, 0);
        lblFormTit = new JLabel("📝 Datos del Asistente");
        lblFormTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        pnlFormCard.add(lblFormTit, gbc);

        // Nombre
        gbc.gridy = 1; gbc.insets = new Insets(5, 0, 4, 0);
        lblNom = new JLabel("Nombre Completo *");
        lblNom.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        pnlFormCard.add(lblNom, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 14, 0);
        txtNombre = crearTextField("Ej. Carlos Alberto Mendoza");
        pnlFormCard.add(txtNombre, gbc);

        // Correo
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        lblCor = new JLabel("Correo Electrónico *");
        lblCor.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        pnlFormCard.add(lblCor, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 14, 0);
        txtCorreo = crearTextField("ejemplo@correo.com");
        pnlFormCard.add(txtCorreo, gbc);

        // Evento asignado
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        lblEvt = new JLabel("Evento Asignado *");
        lblEvt.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        pnlFormCard.add(lblEvt, gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 20, 0);
        cbxEventos = new JComboBox<>(new String[]{"Cargando eventos..."});
        cbxEventos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        cbxEventos.setPreferredSize(new Dimension(0, 36));
        pnlFormCard.add(cbxEventos, gbc);

        // Botonera vertical de acciones del formulario
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 8, 0);
        btnAgregar = crearBotonInteractivo("➕  Inscribir Participante", ThemeManager.getColorNaranja(), true);
        pnlFormCard.add(btnAgregar, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 8, 0);
        btnActualizar = crearBotonInteractivo("🔄  Modificar Datos", new Color(52, 152, 219), false);
        pnlFormCard.add(btnActualizar, gbc);

        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 8, 0);
        btnEliminar = crearBotonInteractivo("🗑️  Eliminar Registro", new Color(231, 76, 60), false);
        pnlFormCard.add(btnEliminar, gbc);

        gbc.gridy = 10; gbc.insets = new Insets(0, 0, 0, 0);
        btnLimpiar = crearBotonInteractivo("Limpiar Campos", new Color(110, 115, 130), false);
        pnlFormCard.add(btnLimpiar, gbc);

        add(pnlFormCard, BorderLayout.WEST);

        // ==========================================
        // 3. TABLA Y LISTADO GENERAL (CENTRO)
        // ==========================================
        pnlTablaCard = new JPanel(new BorderLayout(0, 12));

        // Cabecera superior de la tabla
        JPanel pnlTablaTop = new JPanel(new BorderLayout());
        pnlTablaTop.setOpaque(false);

        JPanel pnlTitulosTabla = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitulosTabla.setOpaque(false);

        lblTablaTit = new JLabel("📋  Listado General de Asistentes");
        lblTablaTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));

        lblTablaSub = new JLabel("Selecciona un registro para editar o eliminar");
        lblTablaSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlTitulosTabla.add(lblTablaTit);
        pnlTitulosTabla.add(lblTablaSub);
        pnlTablaTop.add(pnlTitulosTabla, BorderLayout.WEST);

        btnExportar = crearBotonInteractivo("📥  Exportar a Excel / CSV", new Color(46, 204, 113), false);
        btnExportar.setPreferredSize(new Dimension(190, 36));
        pnlTablaTop.add(btnExportar, BorderLayout.EAST);

        pnlTablaCard.add(pnlTablaTop, BorderLayout.NORTH);

        // Configuración de la JTable
        String[] columnas = {"ID", "Nombre del Asistente", "Correo Electrónico", "Evento Asociado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblParticipantes = new JTable(modeloTabla);
        tblParticipantes.setRowHeight(38);
        tblParticipantes.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tblParticipantes.setShowVerticalLines(false);
        tblParticipantes.setShowHorizontalLines(true);
        tblParticipantes.setSelectionBackground(new Color(255, 117, 31, 35));

        // Header de la tabla
        tblParticipantes.getTableHeader().setReorderingAllowed(false);
        tblParticipantes.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        configurarRenderizadoresTabla();

        scrollPane = new JScrollPane(tblParticipantes);
        pnlTablaCard.add(scrollPane, BorderLayout.CENTER);

        add(pnlTablaCard, BorderLayout.CENTER);
    }

    private void configurarRenderizadoresTabla() {
        DefaultTableCellRenderer idRenderer = new DefaultTableCellRenderer();
        idRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        idRenderer.setForeground(new Color(130, 135, 150));
        tblParticipantes.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        tblParticipantes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblParticipantes.getColumnModel().getColumn(0).setMaxWidth(70);

        DefaultTableCellRenderer normalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(table.getForeground());
                c.setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        tblParticipantes.getColumnModel().getColumn(1).setCellRenderer(normalRenderer);
        tblParticipantes.getColumnModel().getColumn(1).setPreferredWidth(180);

        tblParticipantes.getColumnModel().getColumn(2).setCellRenderer(normalRenderer);
        tblParticipantes.getColumnModel().getColumn(2).setPreferredWidth(180);

        tblParticipantes.getColumnModel().getColumn(3).setCellRenderer(normalRenderer);
        tblParticipantes.getColumnModel().getColumn(3).setPreferredWidth(160);
    }

    private JTextField crearTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 36));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        listaTextFields.add(tf);
        return tf;
    }

    private JButton crearBotonInteractivo(String texto, Color acento, boolean esPrimario) {
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

        if (pnlFormCard != null) {
            pnlFormCard.setBackground(cardBg);
            pnlFormCard.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(20, 20, 20, 20)
            ));
        }

        if (lblFormTit != null) lblFormTit.setForeground(textoTit);
        if (lblNom != null) lblNom.setForeground(textoMuted);
        if (lblCor != null) lblCor.setForeground(textoMuted);
        if (lblEvt != null) lblEvt.setForeground(textoMuted);

        for (JTextField tf : listaTextFields) {
            tf.setBackground(inputBg);
            tf.setForeground(textoNorm);
            tf.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(inputBorde, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));
        }

        if (cbxEventos != null) {
            cbxEventos.setBackground(inputBg);
            cbxEventos.setForeground(textoNorm);
        }

        if (pnlTablaCard != null) {
            pnlTablaCard.setBackground(cardBg);
            pnlTablaCard.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(16, 20, 16, 20)
            ));
        }

        if (lblTablaTit != null) lblTablaTit.setForeground(textoTit);
        if (lblTablaSub != null) lblTablaSub.setForeground(textoMuted);

        if (tblParticipantes != null) {
            tblParticipantes.setBackground(inputBg);
            tblParticipantes.setForeground(textoNorm);
            tblParticipantes.getTableHeader().setBackground(tablaHeaderBg);
            tblParticipantes.getTableHeader().setForeground(textoTit);
            tblParticipantes.setGridColor(gridColor);
            tblParticipantes.setSelectionForeground(isDark ? Color.WHITE : textoTit);
        }

        if (scrollPane != null) {
            scrollPane.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(0, 0, 0, 0)
            ));
        }

        if (btnAgregar != null) {
            btnAgregar.setBackground(ThemeManager.getColorNaranja());
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setBorder(new CompoundBorder(
                    new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                    new EmptyBorder(6, 12, 6, 12)
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

        repaint();
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCorreo() { return txtCorreo; }
    public JComboBox<String> getCbxEventos() { return cbxEventos; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnExportar() { return btnExportar; }
    public JTable getTblParticipantes() { return tblParticipantes; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
