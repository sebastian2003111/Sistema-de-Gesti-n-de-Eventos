package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

public class PnlRecursosActividades extends JPanel {
    // Actividades
    private JTextField txtActNombre;
    private JTextField txtActHorario;
    private JTextField txtActResponsable;
    private JComboBox<String> cbxActEvento;
    private JButton btnAgregarActividad;
    private DefaultTableModel modeloActividades;
    private JTable tblActividades;

    // Recursos
    private JTextField txtRecTipo;
    private JSpinner spRecCantidad;
    private JComboBox<String> cbxRecEvento;
    private JButton btnAgregarRecurso;
    private DefaultTableModel modeloRecursos;
    private JTable tblRecursos;

    public PnlRecursosActividades() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Gestión de Actividades y Recursos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        add(lblTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(35, 35, 35));
        tabbedPane.setForeground(Color.WHITE);
        
        tabbedPane.addTab("Actividades", crearPanelActividades());
        tabbedPane.addTab("Recursos", crearPanelRecursos());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelActividades() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(new Color(25, 25, 25));
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(35, 35, 35));
        pnlForm.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(20, 20, 20, 20)));
        pnlForm.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(10, 0, 5, 0);

        pnlForm.add(crearLabel("Nombre de la Actividad"), gbc);
        gbc.gridy++; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtActNombre = new JTextField(); pnlForm.add(txtActNombre, gbc);

        gbc.gridy++; pnlForm.add(crearLabel("Horario (ej. 10:00 - 12:00)"), gbc);
        gbc.gridy++; txtActHorario = new JTextField(); pnlForm.add(txtActHorario, gbc);

        gbc.gridy++; pnlForm.add(crearLabel("Responsable (Staff)"), gbc);
        gbc.gridy++; txtActResponsable = new JTextField(); pnlForm.add(txtActResponsable, gbc);

        gbc.gridy++; pnlForm.add(crearLabel("Asignar a Evento"), gbc);
        gbc.gridy++; cbxActEvento = new JComboBox<>(); pnlForm.add(cbxActEvento, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 10, 0);
        btnAgregarActividad = new JButton("Agregar Actividad");
        btnAgregarActividad.setBackground(new Color(255, 117, 31));
        btnAgregarActividad.setForeground(Color.WHITE);
        pnlForm.add(btnAgregarActividad, gbc);
        
        gbc.gridy++; gbc.weighty = 1.0; pnlForm.add(new JLabel(""), gbc);
        pnl.add(pnlForm, BorderLayout.WEST);

        modeloActividades = new DefaultTableModel(new String[]{"ID", "Nombre", "Horario", "Responsable", "Evento"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblActividades = new JTable(modeloActividades);
        pnl.add(new JScrollPane(tblActividades), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel crearPanelRecursos() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(new Color(25, 25, 25));
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(35, 35, 35));
        pnlForm.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(20, 20, 20, 20)));
        pnlForm.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(10, 0, 5, 0);

        pnlForm.add(crearLabel("Tipo (Proyector, Catering...)"), gbc);
        gbc.gridy++; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtRecTipo = new JTextField(); pnlForm.add(txtRecTipo, gbc);

        gbc.gridy++; pnlForm.add(crearLabel("Cantidad"), gbc);
        gbc.gridy++; spRecCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1)); pnlForm.add(spRecCantidad, gbc);

        gbc.gridy++; pnlForm.add(crearLabel("Asignar a Evento"), gbc);
        gbc.gridy++; cbxRecEvento = new JComboBox<>(); pnlForm.add(cbxRecEvento, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 10, 0);
        btnAgregarRecurso = new JButton("Agregar Recurso");
        btnAgregarRecurso.setBackground(new Color(255, 117, 31));
        btnAgregarRecurso.setForeground(Color.WHITE);
        pnlForm.add(btnAgregarRecurso, gbc);
        
        gbc.gridy++; gbc.weighty = 1.0; pnlForm.add(new JLabel(""), gbc);
        pnl.add(pnlForm, BorderLayout.WEST);

        modeloRecursos = new DefaultTableModel(new String[]{"ID", "Tipo/Elemento", "Cantidad", "Evento"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRecursos = new JTable(modeloRecursos);
        pnl.add(new JScrollPane(tblRecursos), BorderLayout.CENTER);
        return pnl;
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t); l.setForeground(Color.LIGHT_GRAY); return l;
    }

    // Getters
    public JTextField getTxtActNombre() { return txtActNombre; }
    public JTextField getTxtActHorario() { return txtActHorario; }
    public JTextField getTxtActResponsable() { return txtActResponsable; }
    public JComboBox<String> getCbxActEvento() { return cbxActEvento; }
    public JButton getBtnAgregarActividad() { return btnAgregarActividad; }
    public DefaultTableModel getModeloActividades() { return modeloActividades; }

    public JTextField getTxtRecTipo() { return txtRecTipo; }
    public JSpinner getSpRecCantidad() { return spRecCantidad; }
    public JComboBox<String> getCbxRecEvento() { return cbxRecEvento; }
    public JButton getBtnAgregarRecurso() { return btnAgregarRecurso; }
    public DefaultTableModel getModeloRecursos() { return modeloRecursos; }
}
