package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
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
    private JLabel lblResultadoEstado;
    
    private JTextField txtResultadoNombre;
    private JComboBox<String> cbxResultadoTipo;
    private JTextField txtResultadoFecha;
    private JTextField txtResultadoHora;
    private JTextField txtResultadoLugar;
    private JTextArea txtResultadoDescripcion;
    
    public FrmConsultarEvento() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consultar Evento");
        setBackground(new Color(25, 25, 25));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(25, 25, 25));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel("Consultar / Modificar Evento");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitulo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel Buscador (Borde naranja)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(new Color(25, 25, 25));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        searchPanel.add(crearLabel("ID del Evento:"));
        
        txtIdConsulta = crearTextField();
        txtIdConsulta.setPreferredSize(new Dimension(150, 30));
        searchPanel.add(txtIdConsulta);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(255, 117, 31)); // Naranja
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        searchPanel.add(btnBuscar);
        
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel del Formulario (Gris oscuro redondeado)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(35, 35, 35));
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weightx = 0.5;

        // Fila 1: ID Label | Estado Label
        gbc.gridy = 0; gbc.gridx = 0;
        formPanel.add(crearLabel("ID Evento Encontrado:"), gbc);
        gbc.gridx = 1;
        formPanel.add(crearLabel("Estado actual:"), gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        lblResultadoId = new JLabel("---");
        lblResultadoId.setForeground(new Color(255, 117, 31));
        lblResultadoId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblResultadoId, gbc);
        
        gbc.gridx = 1;
        lblResultadoEstado = new JLabel("---");
        lblResultadoEstado.setForeground(new Color(50, 205, 50)); // Verde
        lblResultadoEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblResultadoEstado, gbc);

        // Fila 2: Nombre | Fecha
        gbc.gridy = 2; gbc.gridx = 0;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Nombre"), gbc);
        gbc.gridx = 1;
        formPanel.add(crearLabel("Fecha (dd/MM/yyyy)"), gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        txtResultadoNombre = crearTextField();
        formPanel.add(txtResultadoNombre, gbc);
        
        gbc.gridx = 1;
        txtResultadoFecha = crearTextField();
        formPanel.add(txtResultadoFecha, gbc);

        // Fila 3: Tipo | Hora
        gbc.gridy = 4; gbc.gridx = 0;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Tipo"), gbc);
        gbc.gridx = 1;
        formPanel.add(crearLabel("Hora (HH:mm)"), gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        cbxResultadoTipo = new JComboBox<>(new String[]{"Seleccione...", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        formPanel.add(cbxResultadoTipo, gbc);
        
        gbc.gridx = 1;
        txtResultadoHora = crearTextField();
        formPanel.add(txtResultadoHora, gbc);
        
        // Fila 4: Lugar
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Lugar"), gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        txtResultadoLugar = crearTextField();
        formPanel.add(txtResultadoLugar, gbc);

        // Fila 5: Descripción
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(crearLabel("Descripción:"), gbc);

        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 10, 10);
        txtResultadoDescripcion = new JTextArea(4, 20);
        txtResultadoDescripcion.setLineWrap(true);
        txtResultadoDescripcion.setWrapStyleWord(true);
        txtResultadoDescripcion.setBackground(new Color(25, 25, 25));
        txtResultadoDescripcion.setForeground(Color.WHITE);
        JScrollPane scrollDesc = new JScrollPane(txtResultadoDescripcion);
        scrollDesc.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));
        formPanel.add(scrollDesc, gbc);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de Botones
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBackground(new Color(25, 25, 25));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnActualizar = new JButton("Actualizar Evento");
        btnActualizar.setBackground(new Color(255, 117, 31)); // Naranja lleno
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        
        btnEliminar = new JButton("Eliminar Evento");
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo lleno
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(25, 25, 25));
        btnVolver.setForeground(new Color(255, 117, 31));
        btnVolver.setBorder(new CompoundBorder(new LineBorder(new Color(255, 117, 31), 1, true), new EmptyBorder(5, 15, 5, 15)));
        btnVolver.setFocusPainted(false);

        btnPanel.add(btnActualizar);
        btnPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        btnPanel.add(btnEliminar);
        btnPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        btnPanel.add(btnVolver);

        mainPanel.add(btnPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setBackground(new Color(25, 25, 25));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(5, 8, 5, 8)
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
    public void setResultadoEstado(String texto) { lblResultadoEstado.setText(texto); }
    
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
