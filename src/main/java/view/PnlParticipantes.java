package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

public class PnlParticipantes extends JPanel {
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JComboBox<String> cbxEventos;
    private JButton btnAgregar;
    private JTable tblParticipantes;
    private DefaultTableModel modeloTabla;

    public PnlParticipantes() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Titulo
        JLabel lblTitulo = new JLabel("Gestión de Participantes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(35, 35, 35));
        pnlForm.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(20, 20, 20, 20)));
        pnlForm.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 5, 0);

        JLabel lblNom = new JLabel("Nombre Completo");
        lblNom.setForeground(Color.LIGHT_GRAY);
        pnlForm.add(lblNom, gbc);

        gbc.gridy++; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNombre = new JTextField();
        pnlForm.add(txtNombre, gbc);

        gbc.gridy++; gbc.insets = new Insets(15, 0, 5, 0);
        JLabel lblCorreo = new JLabel("Correo Electrónico");
        lblCorreo.setForeground(Color.LIGHT_GRAY);
        pnlForm.add(lblCorreo, gbc);

        gbc.gridy++;
        txtCorreo = new JTextField();
        pnlForm.add(txtCorreo, gbc);

        gbc.gridy++; gbc.insets = new Insets(15, 0, 5, 0);
        JLabel lblEv = new JLabel("Asignar a Evento");
        lblEv.setForeground(Color.LIGHT_GRAY);
        pnlForm.add(lblEv, gbc);

        gbc.gridy++;
        cbxEventos = new JComboBox<>();
        pnlForm.add(cbxEventos, gbc);

        gbc.gridy++; gbc.insets = new Insets(30, 0, 10, 0);
        btnAgregar = new JButton("Agregar Participante");
        btnAgregar.setBackground(new Color(255, 117, 31));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        pnlForm.add(btnAgregar, gbc);
        
        // Empujar hacia arriba
        gbc.gridy++; gbc.weighty = 1.0;
        pnlForm.add(new JLabel(""), gbc);

        add(pnlForm, BorderLayout.WEST);

        // Panel de Tabla
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBackground(new Color(25, 25, 25));

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Correo", "Evento"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblParticipantes = new JTable(modeloTabla);
        tblParticipantes.setRowHeight(30);
        tblParticipantes.getTableHeader().setBackground(new Color(40, 40, 40));
        tblParticipantes.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(tblParticipantes);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        add(pnlTabla, BorderLayout.CENTER);
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCorreo() { return txtCorreo; }
    public JComboBox<String> getCbxEventos() { return cbxEventos; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
