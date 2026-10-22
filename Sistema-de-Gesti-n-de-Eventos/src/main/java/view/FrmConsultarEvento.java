package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.logging.Logger;

public class FrmConsultarEvento extends JFrame {

    private static final Logger logger = Logger.getLogger(FrmConsultarEvento.class.getName());

    // Buscador
    private JTextField txtIdConsulta;
    public JButton btnBuscar;

    // Componentes Públicos / Privados requeridos
    public JButton btnActualizar;
    public JButton btnEliminar;
    public JButton btnVolver;

    private JLabel lblResultadoId;
    private JComboBox<String> cbxResultadoEstado;

    private JTextField txtResultadoNombre;
    private JComboBox<String> cbxResultadoTipo;
    private JTextField txtResultadoFecha;
    private JTextField txtResultadoHora;
    private JTextField txtResultadoLugar;
    private JTextArea txtResultadoDescripcion;

    // Colores dinámicos
    private final Color COLOR_BG = config.ThemeManager.getColorBg();
    private final Color COLOR_CARD_BG = config.ThemeManager.getColorCardBg();
    private final Color COLOR_BORDE = config.ThemeManager.getColorBorde();
    private final Color COLOR_NARANJA = config.ThemeManager.getColorNaranja();
    private final Color COLOR_TEXTO_TITULO = config.ThemeManager.getColorTextoTitulo();
    private final Color COLOR_TEXTO_MUTED = config.ThemeManager.getColorTextoMuted();

    public FrmConsultarEvento() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consultar / Modificar Evento");
        setSize(780, 660);
        setMinimumSize(new Dimension(680, 560));
        setLocationRelativeTo(null);
        setBackground(COLOR_BG);

        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/LOGO_SGE.png"));
            setIconImage(icon);
        } catch (Exception ignored) {}

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Título del formulario
        JLabel lblTitulo = new JLabel("🔍  Consultar / Modificar Evento");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitulo);

        JLabel lblSub = new JLabel("Busca un evento por su identificador único para editar su programación o actualizar su estado.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSub.setForeground(COLOR_TEXTO_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        mainPanel.add(lblSub);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Panel Buscador
        JPanel searchCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchCard.setBackground(COLOR_CARD_BG);
        searchCard.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        searchCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel lblBuscarTit = new JLabel("ID del Evento:");
        lblBuscarTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lblBuscarTit.setForeground(COLOR_TEXTO_TITULO);
        searchCard.add(lblBuscarTit);

        txtIdConsulta = crearTextField();
        txtIdConsulta.setPreferredSize(new Dimension(160, 34));
        txtIdConsulta.putClientProperty("JTextField.placeholderText", "Ej. 1");
        searchCard.add(txtIdConsulta);

        btnBuscar = new JButton("🔍  Buscar Evento");
        btnBuscar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnBuscar.setBackground(COLOR_NARANJA);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuscar.setPreferredSize(new Dimension(140, 34));
        btnBuscar.setBorder(new CompoundBorder(
                new LineBorder(COLOR_NARANJA, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        btnBuscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnBuscar.setBackground(new Color(255, 135, 60)); }
            @Override
            public void mouseExited(MouseEvent e) { btnBuscar.setBackground(COLOR_NARANJA); }
        });
        searchCard.add(btnBuscar);

        mainPanel.add(searchCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Panel de Resultados (Formulario)
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(COLOR_CARD_BG);
        formCard.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.weightx = 0.5;

        // Fila 1: ID Label | Estado Label
        gbc.gridy = 0; gbc.gridx = 0;
        formPanelLabel(formCard, "ID Evento Encontrado:", gbc);

        gbc.gridx = 1;
        formPanelLabel(formCard, "Estado Actual del Evento:", gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        lblResultadoId = new JLabel("---");
        lblResultadoId.setForeground(COLOR_NARANJA);
        lblResultadoId.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblResultadoId.setBorder(new EmptyBorder(4, 4, 4, 4));
        formCard.add(lblResultadoId, gbc);

        gbc.gridx = 1;
        cbxResultadoEstado = new JComboBox<>(new String[]{"Programado", "En preparación", "En curso", "Finalizado", "Cancelado"});
        cbxResultadoEstado.setBackground(new Color(22, 22, 28));
        cbxResultadoEstado.setForeground(new Color(80, 220, 130)); // Verde
        cbxResultadoEstado.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        cbxResultadoEstado.setPreferredSize(new Dimension(0, 34));
        formCard.add(cbxResultadoEstado, gbc);

        // Fila 2: Nombre | Fecha
        gbc.gridy = 2; gbc.gridx = 0;
        gbc.insets = new Insets(12, 8, 4, 8);
        formPanelLabel(formCard, "Nombre del Evento *", gbc);

        gbc.gridx = 1;
        formPanelLabel(formCard, "Fecha Programada (dd/MM/yyyy) *", gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        gbc.insets = new Insets(4, 8, 4, 8);
        txtResultadoNombre = crearTextField();
        formCard.add(txtResultadoNombre, gbc);

        gbc.gridx = 1;
        txtResultadoFecha = crearTextField();
        formCard.add(txtResultadoFecha, gbc);

        // Fila 3: Tipo | Hora
        gbc.gridy = 4; gbc.gridx = 0;
        gbc.insets = new Insets(12, 8, 4, 8);
        formPanelLabel(formCard, "Tipo de Evento *", gbc);

        gbc.gridx = 1;
        formPanelLabel(formCard, "Hora Programada (HH:mm) *", gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        gbc.insets = new Insets(4, 8, 4, 8);
        cbxResultadoTipo = new JComboBox<>(new String[]{"Seleccione...", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxResultadoTipo.setBackground(new Color(22, 22, 28));
        cbxResultadoTipo.setForeground(COLOR_TEXTO_TITULO);
        cbxResultadoTipo.setPreferredSize(new Dimension(0, 34));
        formCard.add(cbxResultadoTipo, gbc);

        gbc.gridx = 1;
        txtResultadoHora = crearTextField();
        formCard.add(txtResultadoHora, gbc);

        // Fila 4: Lugar (Full Width)
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 4, 8);
        formPanelLabel(formCard, "Lugar / Sede *", gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 8, 4, 8);
        txtResultadoLugar = crearTextField();
        formCard.add(txtResultadoLugar, gbc);

        // Fila 5: Descripción
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 4, 8);
        formPanelLabel(formCard, "Descripción del Evento", gbc);

        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 8, 8, 8);
        txtResultadoDescripcion = new JTextArea(3, 20);
        txtResultadoDescripcion.setLineWrap(true);
        txtResultadoDescripcion.setWrapStyleWord(true);
        txtResultadoDescripcion.setBackground(new Color(22, 22, 28));
        txtResultadoDescripcion.setForeground(COLOR_TEXTO_TITULO);
        txtResultadoDescripcion.setCaretColor(Color.WHITE);
        txtResultadoDescripcion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        txtResultadoDescripcion.setBorder(new EmptyBorder(6, 8, 6, 8));

        JScrollPane scrollDesc = new JScrollPane(txtResultadoDescripcion);
        scrollDesc.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        formCard.add(scrollDesc, gbc);

        mainPanel.add(formCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Panel de Botones de Acción
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnActualizar = new JButton("💾  Guardar Cambios");
        btnActualizar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setBorder(new CompoundBorder(
                new LineBorder(new Color(52, 152, 219), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnActualizar.setBackground(new Color(65, 165, 230)); }
            @Override
            public void mouseExited(MouseEvent e) { btnActualizar.setBackground(new Color(52, 152, 219)); }
        });

        btnEliminar = new JButton("🗑️  Eliminar Evento");
        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setBorder(new CompoundBorder(
                new LineBorder(new Color(231, 76, 60), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btnEliminar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnEliminar.setBackground(new Color(245, 90, 75)); }
            @Override
            public void mouseExited(MouseEvent e) { btnEliminar.setBackground(new Color(231, 76, 60)); }
        });

        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnVolver.setBackground(new Color(38, 38, 48));
        btnVolver.setForeground(COLOR_TEXTO_TITULO);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVolver.setBackground(new Color(48, 48, 62));
                btnVolver.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVolver.setBackground(new Color(38, 38, 48));
                btnVolver.setForeground(COLOR_TEXTO_TITULO);
            }
        });

        btnPanel.add(btnActualizar);
        btnPanel.add(btnEliminar);
        btnPanel.add(btnVolver);

        mainPanel.add(btnPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void formPanelLabel(JPanel panel, String texto, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COLOR_TEXTO_MUTED);
        lbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        panel.add(lbl, gbc);
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setBackground(new Color(22, 22, 28));
        txt.setForeground(COLOR_TEXTO_TITULO);
        txt.setCaretColor(Color.WHITE);
        txt.setPreferredSize(new Dimension(0, 34));
        txt.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return txt;
    }

    // ==========================================
    // GETTERS Y SETTERS PARA EL CONTROLLER
    // ==========================================
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }

    public String getIdConsulta() { return txtIdConsulta.getText(); }
    public String getResultadoId() { return lblResultadoId.getText(); }
    public void setResultadoId(String texto) { lblResultadoId.setText(texto); }
    public String getResultadoEstado() { return cbxResultadoEstado.getSelectedItem() != null ? cbxResultadoEstado.getSelectedItem().toString() : ""; }
    public void setResultadoEstado(String texto) { cbxResultadoEstado.setSelectedItem(texto); }
    public JComboBox<String> getCbxResultadoEstado() { return cbxResultadoEstado; }

    public String getResultadoNombre() { return txtResultadoNombre.getText(); }
    public String getResultadoTipo() { return cbxResultadoTipo.getSelectedItem() != null ? cbxResultadoTipo.getSelectedItem().toString() : ""; }
    public String getResultadoFecha() { return txtResultadoFecha.getText(); }
    public String getResultadoHora() { return txtResultadoHora.getText(); }
    public String getResultadoLugar() { return txtResultadoLugar.getText(); }
    public String getResultadoDescripcion() { return txtResultadoDescripcion.getText(); }

    public void setResultadoNombre(String texto) { txtResultadoNombre.setText(texto); }
    public void setResultadoTipo(String texto) { cbxResultadoTipo.setSelectedItem(texto); }
    public void setResultadoFecha(String texto) { txtResultadoFecha.setText(texto); }
    public void setResultadoHora(String texto) { txtResultadoHora.setText(texto); }
    public void setResultadoLugar(String texto) { txtResultadoLugar.setText(texto); }
    public void setResultadoDescripcion(String texto) { txtResultadoDescripcion.setText(texto); }

    public void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
