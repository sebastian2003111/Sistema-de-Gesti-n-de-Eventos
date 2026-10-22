package view;

import config.ThemeManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.logging.Logger;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;

/**
 * FrmEvento - Módulo de Creación y Registro de Eventos con diseño Glassmorphic Premium.
 * Adaptable 100% dinámicamente a Tema Oscuro Moderno y Tema Claro Profesional.
 */
public class FrmEvento extends JFrame implements ThemeManager.ThemeListener {

    private static final Logger logger = Logger.getLogger(FrmEvento.class.getName());

    // Componentes Públicos (Requeridos por EventoController)
    public JButton btnConsultar;
    public JButton btnCrear;
    public JButton btnListar;

    public JComboBox<String> cbxTipo;
    public DatePicker datePickerFecha;
    public TimePicker timePickerHora;
    public JTextArea txtDescripcion;
    public JTextField txtId;
    public JTextField txtLugar;
    public JTextField txtNombre;

    public JSpinner spnFecha = new JSpinner();
    public JSpinner spnHora = new JSpinner();
    public JLabel lblLogo; // Mantenido por compatibilidad

    // Componentes estructurales para re-theming dinámico
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JLabel lblVit1;
    private JLabel lblVit2;
    private GlassmorphicCardPanel pnlFormWrapper;
    private GlassmorphicCardPanel pnlVisual;
    private final List<JLabel> listaLabelsForm = new ArrayList<>();
    private final List<JTextField> listaTextFields = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();
    private final List<JPanel> listaTarjetasShowcase = new ArrayList<>();

    public FrmEvento() {
        initComponentsCustom();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(22, 28, 22, 28));

        // =====================================
        // TÍTULO DEL MÓDULO (NORTE)
        // =====================================
        JPanel pnlTitulo = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTitulo.setOpaque(false);

        lblTitulo = new JLabel("🗓️  Creación y Registro de Eventos");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Ingresa los datos generales, fecha, hora y ubicación para programar una nueva actividad en el sistema.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        pnlTitulo.add(lblTitulo);
        pnlTitulo.add(lblSub);
        mainPanel.add(pnlTitulo, BorderLayout.NORTH);

        // =====================================
        // CONTENEDOR CENTRAL DIVIDIDO (CARDS)
        // =====================================
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setOpaque(false);
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1.0;
        gbcMain.insets = new Insets(0, 0, 0, 14);

        // --- IZQUIERDA: TARJETA FORMULARIO (GLASSMORPHIC) ---
        gbcMain.gridx = 0;
        gbcMain.weightx = 0.64;

        pnlFormWrapper = new GlassmorphicCardPanel();
        pnlFormWrapper.setLayout(new BorderLayout(0, 16));
        pnlFormWrapper.setBorder(new EmptyBorder(22, 24, 20, 24));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        gbcF.insets = new Insets(4, 8, 4, 8);
        gbcF.weightx = 0.5;

        // Fila 1: ID Evento (Izquierda) | Fecha (Derecha)
        gbcF.gridy = 0;
        gbcF.gridx = 0;
        JPanel pnlLabelId = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlLabelId.setOpaque(false);
        pnlLabelId.add(crearLabel("ID Evento"));

        JLabel lblAuto = new JLabel(" Automático ");
        lblAuto.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
        lblAuto.setForeground(ThemeManager.getColorNaranja());
        lblAuto.setOpaque(true);
        lblAuto.setBackground(new Color(255, 117, 31, 30));
        lblAuto.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 117, 31, 80), 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        pnlLabelId.add(lblAuto);
        formPanel.add(pnlLabelId, gbcF);

        gbcF.gridx = 1;
        formPanel.add(crearLabel("Fecha del Evento *"), gbcF);

        gbcF.gridy = 1;
        gbcF.gridx = 0;
        txtId = crearTextField();
        txtId.setEditable(false);
        txtId.setToolTipText("ID asignado secuencialmente por el sistema");
        formPanel.add(txtId, gbcF);

        gbcF.gridx = 1;
        datePickerFecha = new DatePicker();
        estilizarDatePicker(datePickerFecha);
        formPanel.add(datePickerFecha, gbcF);

        // Fila 2: Nombre del Evento (Izquierda) | Hora (Derecha)
        gbcF.gridy = 2;
        gbcF.gridx = 0;
        formPanel.add(crearLabel("Nombre del Evento *"), gbcF);

        gbcF.gridx = 1;
        formPanel.add(crearLabel("Hora del Evento *"), gbcF);

        gbcF.gridy = 3;
        gbcF.gridx = 0;
        txtNombre = crearTextField();
        txtNombre.putClientProperty("JTextField.placeholderText", "Ej. Simposio Internacional de IA");
        formPanel.add(txtNombre, gbcF);

        gbcF.gridx = 1;
        timePickerHora = new TimePicker();
        estilizarTimePicker(timePickerHora);
        formPanel.add(timePickerHora, gbcF);

        // Fila 3: Tipo de Evento (Izquierda) | Lugar / Sede (Derecha)
        gbcF.gridy = 4;
        gbcF.gridx = 0;
        formPanel.add(crearLabel("Tipo de Evento *"), gbcF);

        gbcF.gridx = 1;
        formPanel.add(crearLabel("Lugar / Sede *"), gbcF);

        gbcF.gridy = 5;
        gbcF.gridx = 0;
        cbxTipo = new JComboBox<>(new String[]{
            "Seleccione un tipo...", "Académico", "Social", "Empresarial", "Cultural", "Deportivo", "Otro"
        });
        cbxTipo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        cbxTipo.setPreferredSize(new Dimension(0, 38));
        formPanel.add(cbxTipo, gbcF);

        gbcF.gridx = 1;
        txtLugar = crearTextField();
        txtLugar.putClientProperty("JTextField.placeholderText", "Ej. Auditorio Principal - Bloque B");
        formPanel.add(txtLugar, gbcF);

        // Fila 4: Descripción y Objetivos
        gbcF.gridy = 6;
        gbcF.gridx = 0;
        gbcF.gridwidth = 2;
        formPanel.add(crearLabel("Descripción y Objetivos del Evento"), gbcF);

        gbcF.gridy = 7;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorInputBorder(), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        scrollDesc.setPreferredSize(new Dimension(0, 85));
        formPanel.add(scrollDesc, gbcF);

        // =====================================
        // BOTONERA DE ACCIÓN
        // =====================================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 8, 4, 8));

        btnCrear = new ModernOrangeButton("➕  Crear Evento");
        btnCrear.setPreferredSize(new Dimension(160, 38));

        btnConsultar = crearBotonOutlined("🔍  Consultar / Modificar");
        btnListar = crearBotonOutlined("📋  Ver Todos los Eventos");

        btnPanel.add(btnCrear);
        btnPanel.add(btnConsultar);
        btnPanel.add(btnListar);

        pnlFormWrapper.add(formPanel, BorderLayout.CENTER);
        pnlFormWrapper.add(btnPanel, BorderLayout.SOUTH);

        pnlCentro.add(pnlFormWrapper, gbcMain);

        // --- DERECHA: TARJETA VISUAL DE CATEGORÍAS (GLASSMORPHIC) ---
        gbcMain.gridx = 1;
        gbcMain.weightx = 0.37;
        gbcMain.insets = new Insets(0, 14, 0, 0);

        pnlVisual = new GlassmorphicCardPanel();
        pnlVisual.setLayout(new BorderLayout(0, 16));
        pnlVisual.setBorder(new EmptyBorder(18, 16, 18, 16));

        JPanel pnlVisText = new JPanel();
        pnlVisText.setLayout(new BoxLayout(pnlVisText, BoxLayout.Y_AXIS));
        pnlVisText.setOpaque(false);

        lblVit1 = new JLabel("Experiencias que Conectan");
        lblVit1.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        lblVit2 = new JLabel("<html><p style='margin:0;'>Gestiona de forma integral congresos, talleres y celebraciones con asistencia controlada.</p></html>");
        lblVit2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));

        pnlVisText.add(lblVit1);
        pnlVisText.add(Box.createRigidArea(new Dimension(0, 6)));
        pnlVisText.add(lblVit2);
        pnlVisText.add(Box.createRigidArea(new Dimension(0, 18)));

        // Tarjetas Visuales (Grid 2x2)
        JPanel pnlTarjetas = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlTarjetas.setOpaque(false);

        pnlTarjetas.add(crearTarjetaMuestra("🎓 Académicos", "Conferencias, talleres y seminarios.", 
                new Color(24, 34, 52, 230), new Color(45, 75, 115), new Color(77, 163, 255), "Académico"));
        pnlTarjetas.add(crearTarjetaMuestra("👥 Sociales", "Encuentros, galas y asambleas.", 
                new Color(24, 45, 34, 230), new Color(40, 95, 65), new Color(77, 214, 134), "Social"));
        pnlTarjetas.add(crearTarjetaMuestra("💼 Empresariales", "Capacitaciones y ferias corporativas.", 
                new Color(48, 34, 26, 230), new Color(110, 68, 42), new Color(255, 140, 66), "Empresarial"));
        pnlTarjetas.add(crearTarjetaMuestra("⭐ Culturales", "Festivales y muestras artísticas.", 
                new Color(40, 26, 52, 230), new Color(95, 52, 125), new Color(195, 130, 255), "Cultural"));

        pnlVisText.add(pnlTarjetas);
        pnlVisual.add(pnlVisText, BorderLayout.NORTH);

        pnlCentro.add(pnlVisual, gbcMain);

        mainPanel.add(pnlCentro, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        listaLabelsForm.add(label);
        return label;
    }

    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(0, 38));

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.isEditable()) {
                    textField.setBorder(new CompoundBorder(
                            new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                            new EmptyBorder(8, 12, 8, 12)
                    ));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorInputBorder(), 1, true),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        listaTextFields.add(textField);
        return textField;
    }

    private void estilizarDatePicker(DatePicker picker) {
        DatePickerSettings dateSettings = picker.getSettings();
        dateSettings.setFontValidDate(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        picker.getComponentDateTextField().setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        picker.getComponentDateTextField().setBorder(new EmptyBorder(6, 12, 6, 8));

        JButton btnCal = picker.getComponentToggleCalendarButton();
        btnCal.setText("📅");
        btnCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnCal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCal.setMargin(new Insets(0, 0, 0, 0));
        btnCal.setPreferredSize(new Dimension(36, 32));
        btnCal.setBorder(new EmptyBorder(2, 4, 2, 4));

        picker.setPreferredSize(new Dimension(0, 38));
    }

    private void estilizarTimePicker(TimePicker picker) {
        picker.getComponentTimeTextField().setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        picker.getComponentTimeTextField().setBorder(new EmptyBorder(6, 12, 6, 8));

        JButton btnTime = picker.getComponentToggleTimeMenuButton();
        btnTime.setText("🕒");
        btnTime.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnTime.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTime.setMargin(new Insets(0, 0, 0, 0));
        btnTime.setPreferredSize(new Dimension(36, 32));
        btnTime.setBorder(new EmptyBorder(2, 4, 2, 4));

        picker.setPreferredSize(new Dimension(0, 38));
    }

    private JButton crearBotonOutlined(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 38));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(48, 50, 62) : new Color(228, 233, 240));
                btn.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                        new EmptyBorder(8, 16, 8, 16)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(36, 37, 46) : new Color(241, 245, 249));
                btn.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(8, 16, 8, 16)
                ));
                btn.setForeground(ThemeManager.getColorTextoTitulo());
            }
        });

        listaBotonesSecundarios.add(btn);
        return btn;
    }

    private JPanel crearTarjetaMuestra(String titulo, String subtitulo, Color bgTintDark, Color bordeColorDark, Color textAcento, String tipoAsociado) {
        JPanel pnl = new JPanel(new BorderLayout(0, 6));
        pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnl.setToolTipText("Haga clic para seleccionar " + tipoAsociado);

        pnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pnl.setBorder(new CompoundBorder(
                        new LineBorder(textAcento, 1, true),
                        new EmptyBorder(14, 12, 14, 12)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                pnl.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.isDark() ? bordeColorDark : ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(14, 12, 14, 12)
                ));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cbxTipo != null) {
                    cbxTipo.setSelectedItem(tipoAsociado);
                }
            }
        });

        JLabel lblT = new JLabel(titulo);
        lblT.setForeground(textAcento);
        lblT.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

        JLabel lblS = new JLabel(subtitulo);
        lblS.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));

        pnl.add(lblT, BorderLayout.NORTH);
        pnl.add(lblS, BorderLayout.CENTER);
        listaTarjetasShowcase.add(pnl);
        return pnl;
    }

    @Override
    public void aplicarTema(boolean isDark) {
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();
        Color textoNorm = ThemeManager.getColorTextoNormal();
        Color inputBg = ThemeManager.getColorInputBg();
        Color inputBorde = ThemeManager.getColorInputBorder();
        Color borde = ThemeManager.getColorBorde();

        lblTitulo.setForeground(textoTit);
        lblSub.setForeground(textoMuted);
        if (lblVit1 != null) lblVit1.setForeground(textoTit);
        if (lblVit2 != null) lblVit2.setForeground(textoMuted);

        for (JLabel l : listaLabelsForm) {
            l.setForeground(isDark ? new Color(165, 170, 185) : new Color(50, 60, 75));
        }

        for (JTextField tf : listaTextFields) {
            tf.setBackground(inputBg);
            tf.setForeground(textoNorm);
            tf.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
            tf.setBorder(new CompoundBorder(
                    new LineBorder(inputBorde, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));
        }

        if (txtDescripcion != null) {
            txtDescripcion.setBackground(inputBg);
            txtDescripcion.setForeground(textoNorm);
            txtDescripcion.setCaretColor(isDark ? Color.WHITE : Color.BLACK);
        }

        if (cbxTipo != null) {
            cbxTipo.setBackground(inputBg);
            cbxTipo.setForeground(textoNorm);
        }

        if (datePickerFecha != null) {
            datePickerFecha.getSettings().setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, inputBg);
            datePickerFecha.getComponentDateTextField().setForeground(textoNorm);
            datePickerFecha.getComponentToggleCalendarButton().setBackground(isDark ? new Color(36, 37, 46) : new Color(241, 245, 249));
            datePickerFecha.getComponentToggleCalendarButton().setForeground(ThemeManager.getColorNaranja());
            datePickerFecha.setBorder(new LineBorder(inputBorde, 1, true));
        }

        if (timePickerHora != null) {
            timePickerHora.getSettings().setColor(TimePickerSettings.TimeArea.TextFieldBackgroundValidTime, inputBg);
            timePickerHora.getComponentTimeTextField().setForeground(textoNorm);
            timePickerHora.getComponentToggleTimeMenuButton().setBackground(isDark ? new Color(36, 37, 46) : new Color(241, 245, 249));
            timePickerHora.getComponentToggleTimeMenuButton().setForeground(ThemeManager.getColorNaranja());
            timePickerHora.setBorder(new LineBorder(inputBorde, 1, true));
        }

        for (JButton btn : listaBotonesSecundarios) {
            btn.setBackground(isDark ? new Color(36, 37, 46) : new Color(241, 245, 249));
            btn.setForeground(textoTit);
            btn.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(8, 16, 8, 16)
            ));
        }

        for (JPanel tc : listaTarjetasShowcase) {
            tc.setBackground(isDark ? new Color(28, 28, 36) : new Color(248, 250, 252));
            tc.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(14, 12, 14, 12)
            ));
            Component[] comps = tc.getComponents();
            if (comps.length > 1 && comps[1] instanceof JLabel) {
                ((JLabel) comps[1]).setForeground(textoMuted);
            }
        }

        if (pnlFormWrapper != null) pnlFormWrapper.repaint();
        if (pnlVisual != null) pnlVisual.repaint();
        repaint();
    }

    // =========================================================================
    // CLASES INTERNAS: GLASSMORPHIC CARD & MODERN ORANGE BUTTON
    // =========================================================================
    private static class GlassmorphicCardPanel extends JPanel {
        public GlassmorphicCardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = 18;

            boolean isDark = ThemeManager.isDark();

            if (isDark) {
                // Sombra suave multicapa modo oscuro
                for (int i = 1; i <= 5; i++) {
                    int alpha = Math.max(0, 14 - (i * 2));
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.drawRoundRect(5 - i, 5 - i + 2, w - (5 - i) * 2, h - (5 - i) * 2, arc + i * 2, arc + i * 2);
                }

                // Cristal oscuro acrílico
                GradientPaint cardGrad = new GradientPaint(
                        0, 5, new Color(46, 47, 54, 235),
                        0, h - 5, new Color(32, 33, 38, 245)
                );
                g2.setPaint(cardGrad);
                g2.fillRoundRect(5, 5, w - 10, h - 10, arc, arc);

                // Borde perimetral reflectante
                g2.setColor(new Color(72, 74, 86, 170));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(5, 5, w - 10, h - 10, arc, arc);
            } else {
                // Sombra suave multicapa modo claro
                for (int i = 1; i <= 5; i++) {
                    int alpha = Math.max(0, 10 - (i * 2));
                    g2.setColor(new Color(110, 125, 145, alpha));
                    g2.drawRoundRect(5 - i, 5 - i + 2, w - (5 - i) * 2, h - (5 - i) * 2, arc + i * 2, arc + i * 2);
                }

                // Frosted acrylic glass blanco nítido
                GradientPaint cardGrad = new GradientPaint(
                        0, 5, new Color(255, 255, 255, 248),
                        0, h - 5, new Color(248, 250, 252, 245)
                );
                g2.setPaint(cardGrad);
                g2.fillRoundRect(5, 5, w - 10, h - 10, arc, arc);

                // Borde suave claro
                g2.setColor(new Color(226, 232, 240, 220));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(5, 5, w - 10, h - 10, arc, arc);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

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
            setPreferredSize(new Dimension(170, 38));

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
                cTop = new Color(225, 80, 15);
                cBottom = new Color(195, 65, 10);
            } else if (isHover) {
                cTop = ThemeManager.getColorNaranjaHover();
                cBottom = ThemeManager.getColorNaranja();
            } else {
                cTop = ThemeManager.getColorNaranja();
                cBottom = new Color(238, 92, 14);
            }

            GradientPaint gp = new GradientPaint(0, 0, cTop, 0, h, cBottom);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            g2.setColor(new Color(255, 255, 255, isHover ? 60 : 35));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

            FontMetrics fm = g2.getFontMetrics(getFont());
            int textX = (w - fm.stringWidth(getText())) / 2;
            int textY = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(Color.WHITE);
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }
    }

    // --- Getters y Setters requeridos por el Controlador ---
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnConsultar() { return btnConsultar; }
    public JButton getBtnListar() { return btnListar; }

    public String getIdEvento() { return txtId.getText(); }
    public void setIdGenerado(String id) { txtId.setText(id); }

    public String getNombre() { return txtNombre.getText(); }
    public String getDescripcion() { return txtDescripcion.getText(); }

    public String getTipo() {
        if (cbxTipo.getSelectedIndex() == 0) return "";
        return cbxTipo.getSelectedItem().toString();
    }

    public String getLugar() { return txtLugar.getText(); }

    public java.util.Date getFecha() {
        LocalDate ld = datePickerFecha.getDate();
        if (ld == null) return null;
        return java.util.Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void setFecha(java.util.Date date) {
        if (date != null) datePickerFecha.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        else datePickerFecha.clear();
    }

    public java.util.Date getHora() {
        LocalTime lt = timePickerHora.getTime();
        if (lt == null) return null;
        return java.util.Date.from(lt.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setHora(java.util.Date date) {
        if (date != null) timePickerHora.setTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        else timePickerHora.clear();
    }

    // --- Mensajes y Limpieza ---
    public void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        cbxTipo.setSelectedIndex(0);
        txtLugar.setText("");
        datePickerFecha.clear();
        timePickerHora.clear();
    }

    public void mostrarMensajeExito(String msj) {
        JOptionPane.showMessageDialog(null, msj, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensajeAdvertencia(String msj) {
        JOptionPane.showMessageDialog(null, msj, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarMensajeError(String msj) {
        JOptionPane.showMessageDialog(null, msj, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
