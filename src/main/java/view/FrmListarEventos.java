package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmListarEventos extends JFrame {

    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public FrmListarEventos() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Listado de Eventos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configurar la tabla
        String[] columnas = {"ID", "Nombre", "Tipo", "Fecha", "Hora", "Lugar", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla de solo lectura
            }
        };
        
        tablaEventos = new JTable(modeloTabla);
        tablaEventos.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        
        // Configurar botones
        JPanel panelBotones = new JPanel();
        btnActualizar = new JButton("Recargar Lista");
        btnCerrar = new JButton("Cerrar");
        
        btnActualizar.setBackground(new Color(255, 117, 31));
        btnActualizar.setForeground(Color.WHITE);
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }
}
