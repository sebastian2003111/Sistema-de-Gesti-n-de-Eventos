package view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PnlInicio extends JPanel {

    private JLabel lblTotalEventos;
    private JLabel lblProximosEventos;
    private JLabel lblFinalizados;
    private JTable tblRecientes;
    private DefaultTableModel modeloTabla;

    public PnlInicio() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(35, 35, 35));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título Principal
        JLabel lblTitulo = new JLabel("Resumen del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        add(lblTitulo, BorderLayout.NORTH);

        // Contenedor Central
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(new Color(35, 35, 35));

        // ==========================================
        // PANEL DE TARJETAS (KPIs)
        // ==========================================
        JPanel pnlTarjetas = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlTarjetas.setBackground(new Color(35, 35, 35));
        pnlTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Tarjeta 1
        lblTotalEventos = new JLabel("0");
        JPanel card1 = crearTarjetaKPI("Total Eventos", lblTotalEventos, new Color(41, 128, 185));
        pnlTarjetas.add(card1);

        // Tarjeta 2
        lblProximosEventos = new JLabel("0");
        JPanel card2 = crearTarjetaKPI("Próximos Eventos", lblProximosEventos, new Color(255, 117, 31)); // Naranja
        pnlTarjetas.add(card2);

        // Tarjeta 3
        lblFinalizados = new JLabel("0");
        JPanel card3 = crearTarjetaKPI("Eventos Finalizados", lblFinalizados, new Color(39, 174, 96)); // Verde
        pnlTarjetas.add(card3);

        pnlCenter.add(pnlTarjetas);
        pnlCenter.add(Box.createRigidArea(new Dimension(0, 30)));

        // ==========================================
        // TABLA DE PRÓXIMOS EVENTOS
        // ==========================================
        JLabel lblSubtitulo = new JLabel("Próximos Eventos a Realizarse");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubtitulo.setForeground(new Color(200, 200, 200));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel pnlTablaContainer = new JPanel(new BorderLayout());
        pnlTablaContainer.setBackground(new Color(35, 35, 35));
        pnlTablaContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] columnas = {"ID", "Nombre", "Tipo", "Fecha", "Lugar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRecientes = new JTable(modeloTabla);
        tblRecientes.setRowHeight(30);
        tblRecientes.getTableHeader().setReorderingAllowed(false);
        tblRecientes.getTableHeader().setBackground(new Color(45, 45, 45));
        tblRecientes.getTableHeader().setForeground(Color.WHITE);
        tblRecientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblRecientes.setBackground(new Color(25, 25, 25));
        tblRecientes.setForeground(Color.WHITE);
        tblRecientes.setGridColor(new Color(50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(tblRecientes);
        scrollPane.getViewport().setBackground(new Color(25, 25, 25));
        scrollPane.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));

        pnlTablaContainer.add(lblSubtitulo, BorderLayout.NORTH);
        pnlTablaContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing manual al usar borde
        pnlTablaContainer.add(scrollPane, BorderLayout.CENTER);

        pnlCenter.add(pnlTablaContainer);

        add(pnlCenter, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaKPI(String titulo, JLabel lblValor, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Línea de color acento arriba (simulado usando borde superior o un panel)
        JPanel pnlAccent = new JPanel();
        pnlAccent.setBackground(accentColor);
        pnlAccent.setPreferredSize(new Dimension(0, 4));
        panel.add(pnlAccent, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(200, 200, 200));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(Color.WHITE);

        JPanel pnlTexts = new JPanel();
        pnlTexts.setLayout(new BoxLayout(pnlTexts, BoxLayout.Y_AXIS));
        pnlTexts.setBackground(new Color(45, 45, 45));
        
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlTexts.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlTexts.add(lblTitle);
        pnlTexts.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlTexts.add(lblValor);

        panel.add(pnlTexts, BorderLayout.CENTER);

        return panel;
    }

    public void setTotalEventos(int total) {
        lblTotalEventos.setText(String.valueOf(total));
    }

    public void setProximosEventos(int total) {
        lblProximosEventos.setText(String.valueOf(total));
    }

    public void setEventosFinalizados(int total) {
        lblFinalizados.setText(String.valueOf(total));
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}
