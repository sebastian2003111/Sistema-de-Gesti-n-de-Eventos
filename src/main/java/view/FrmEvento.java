package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;

public class FrmEvento extends JFrame {

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

    public FrmEvento() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(25, 25, 25));

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(25, 25, 25));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // =====================================
        // TÍTULO DEL MÓDULO (NORTE)
        // =====================================
        JPanel pnlTitulo = new JPanel(new GridLayout(2, 1));
        pnlTitulo.setBackground(new Color(25, 25, 25));
        JLabel lblTitulo = new JLabel("📅 Crear Nuevo Evento");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Completa la información del evento que deseas registrar en el sistema.");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSub.setForeground(Color.LIGHT_GRAY);
        pnlTitulo.add(lblTitulo);
        pnlTitulo.add(lblSub);
        mainPanel.add(pnlTitulo, BorderLayout.NORTH);

        // =====================================
        // CONTENEDOR CENTRAL DIVIDIDO
        // =====================================
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setBackground(new Color(25, 25, 25));
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1.0;
        gbcMain.insets = new Insets(0, 0, 0, 10);

        // --- IZQUIERDA: FORMULARIO ---
        gbcMain.gridx = 0; gbcMain.weightx = 0.65;
        JPanel pnlFormWrapper = new JPanel(new BorderLayout());
        pnlFormWrapper.setBackground(new Color(35, 35, 35));
        pnlFormWrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(35, 35, 35));
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        gbcF.insets = new Insets(5, 10, 5, 10);
        gbcF.weightx = 0.5;

        // Fila 1: ID Evento (Izquierda) | Fecha (Derecha)
        gbcF.gridy = 0; gbcF.gridx = 0;
        JPanel pnlLabelId = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlLabelId.setBackground(new Color(35, 35, 35));
        pnlLabelId.add(crearLabel("ID Evento "));
        JLabel lblAuto = new JLabel(" Automático ");
        lblAuto.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblAuto.setForeground(Color.WHITE);
        lblAuto.setOpaque(true);
        lblAuto.setBackground(new Color(200, 80, 20)); // Naranja quemado
        pnlLabelId.add(lblAuto);
        formPanel.add(pnlLabelId, gbcF);

        gbcF.gridx = 1;
        formPanel.add(crearLabel("Fecha del evento"), gbcF);

        gbcF.gridy = 1; gbcF.gridx = 0;
        txtId = crearTextField();
        txtId.setEditable(false);
        txtId.setText("1");
        formPanel.add(txtId, gbcF);

        gbcF.gridx = 1;
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
        datePickerFecha = new DatePicker(dateSettings);
        formPanel.add(datePickerFecha, gbcF);

        // Fila 2: Nombre (Izquierda) | Hora (Derecha)
        gbcF.gridy = 2; gbcF.gridx = 0; gbcF.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Nombre del evento *"), gbcF);
        
        gbcF.gridx = 1;
        formPanel.add(crearLabel("Hora del evento"), gbcF);

        gbcF.gridy = 3; gbcF.gridx = 0; gbcF.insets = new Insets(5, 10, 5, 10);
        txtNombre = crearTextField();
        txtNombre.putClientProperty("JTextField.placeholderText", "Ej. Feria de Tecnología");
        formPanel.add(txtNombre, gbcF);

        gbcF.gridx = 1;
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.use24HourClockFormat();
        timeSettings.setFormatForDisplayTime("HH:mm");
        timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FifteenMinutes, null, null);
        timePickerHora = new TimePicker(timeSettings);
        formPanel.add(timePickerHora, gbcF);

        // Fila 3: Tipo (Izquierda) | Lugar (Derecha)
        gbcF.gridy = 4; gbcF.gridx = 0; gbcF.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Tipo de evento *"), gbcF);
        
        gbcF.gridx = 1;
        formPanel.add(crearLabel("Lugar"), gbcF);

        gbcF.gridy = 5; gbcF.gridx = 0; gbcF.insets = new Insets(5, 10, 5, 10);
        cbxTipo = new JComboBox<>(new String[]{"Seleccione un tipo...", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxTipo.setBackground(new Color(25, 25, 25));
        cbxTipo.setForeground(Color.WHITE);
        formPanel.add(cbxTipo, gbcF);

        gbcF.gridx = 1;
        txtLugar = crearTextField();
        txtLugar.putClientProperty("JTextField.placeholderText", "Ej. Auditorio Principal");
        formPanel.add(txtLugar, gbcF);

        // Fila 4: Descripción (Full Width)
        gbcF.gridy = 6; gbcF.gridx = 0; gbcF.gridwidth = 2; gbcF.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Descripción del evento"), gbcF);

        gbcF.gridy = 7; gbcF.gridx = 0; gbcF.gridwidth = 2; gbcF.insets = new Insets(5, 10, 20, 10);
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(new Color(25, 25, 25));
        txtDescripcion.setForeground(Color.WHITE);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));
        formPanel.add(scrollDesc, gbcF);

        // Panel de Botones (Sur del Formulario)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnPanel.setBackground(new Color(35, 35, 35));

        btnCrear = new JButton("+ Crear Evento");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.setBackground(new Color(255, 117, 31)); // Naranja
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setBorder(new EmptyBorder(8, 20, 8, 20));

        btnConsultar = new JButton("🔍 Consultar Evento");
        btnConsultar.setBackground(new Color(25, 25, 25));
        btnConsultar.setForeground(new Color(255, 117, 31));
        btnConsultar.setBorder(new CompoundBorder(new LineBorder(new Color(255, 117, 31), 1, true), new EmptyBorder(8, 15, 8, 15)));

        btnListar = new JButton("☷ Ver Todos los Eventos");
        btnListar.setBackground(new Color(25, 25, 25));
        btnListar.setForeground(new Color(255, 117, 31));
        btnListar.setBorder(new CompoundBorder(new LineBorder(new Color(255, 117, 31), 1, true), new EmptyBorder(8, 15, 8, 15)));

        btnPanel.add(btnCrear);
        btnPanel.add(btnConsultar);
        btnPanel.add(btnListar);

        pnlFormWrapper.add(formPanel, BorderLayout.CENTER);
        pnlFormWrapper.add(btnPanel, BorderLayout.SOUTH);

        pnlCentro.add(pnlFormWrapper, gbcMain);

        // --- DERECHA: PANEL VISUAL ---
        gbcMain.gridx = 1; gbcMain.weightx = 0.35; gbcMain.insets = new Insets(0, 10, 0, 0);
        JPanel pnlVisual = new JPanel(new BorderLayout());
        pnlVisual.setBackground(new Color(20, 20, 20));
        pnlVisual.setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(30, 20, 30, 20)
        ));

        // Textos Visuales
        JPanel pnlVisText = new JPanel();
        pnlVisText.setLayout(new BoxLayout(pnlVisText, BoxLayout.Y_AXIS));
        pnlVisText.setBackground(new Color(20, 20, 20));

        JLabel lblVit1 = new JLabel("Crea experiencias que conectan personas");
        lblVit1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVit1.setForeground(Color.WHITE);
        
        JLabel lblVit2 = new JLabel("<html><p style='color:#BBBBBB;'>Registra eventos académicos, culturales, empresariales y mucho más de forma rápida y sencilla.</p></html>");
        lblVit2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnlVisText.add(lblVit1);
        pnlVisText.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlVisText.add(lblVit2);
        pnlVisText.add(Box.createRigidArea(new Dimension(0, 30)));

        // Tarjetas Visuales (Grid 2x2)
        JPanel pnlTarjetas = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlTarjetas.setBackground(new Color(20, 20, 20));

        pnlTarjetas.add(crearTarjetaMuestra("🎓 Académicos", "Conferencias, talleres."));
        pnlTarjetas.add(crearTarjetaMuestra("👥 Sociales", "Encuentros, comunidades."));
        pnlTarjetas.add(crearTarjetaMuestra("💼 Empresariales", "Capacitaciones, ferias."));
        pnlTarjetas.add(crearTarjetaMuestra("⭐ Culturales", "Festivales, torneos."));

        pnlVisText.add(pnlTarjetas);

        pnlVisual.add(pnlVisText, BorderLayout.NORTH);

        pnlCentro.add(pnlVisual, gbcMain);

        mainPanel.add(pnlCentro, BorderLayout.CENTER);
        
        // El controller extrae mainPanel con getContentPane()
        setContentPane(mainPanel);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(25, 25, 25));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JPanel crearTarjetaMuestra(String titulo, String subtitulo) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(new Color(30, 30, 30));
        pnl.setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(15, 10, 15, 10)
        ));
        JLabel lblT = new JLabel(titulo);
        lblT.setForeground(Color.WHITE);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JLabel lblS = new JLabel("<html><p style='color:gray;'>" + subtitulo + "</p></html>");
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        pnl.add(lblT, BorderLayout.NORTH);
        pnl.add(lblS, BorderLayout.CENTER);
        return pnl;
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
        if(cbxTipo.getSelectedIndex() == 0) return "";
        return cbxTipo.getSelectedItem().toString(); 
    }
    
    public String getLugar() { return txtLugar.getText(); }
    
    public java.util.Date getFecha() { 
        LocalDate ld = datePickerFecha.getDate();
        if(ld == null) return null;
        return java.util.Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public void setFecha(java.util.Date date) { 
        if(date != null) datePickerFecha.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        else datePickerFecha.clear();
    }
    
    public java.util.Date getHora() { 
        LocalTime lt = timePickerHora.getTime();
        if(lt == null) return null;
        return java.util.Date.from(lt.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
    public void setHora(java.util.Date date) {
        if(date != null) timePickerHora.setTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
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
