package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

public class PnlNotificaciones extends JPanel {
    private JPanel pnlLista;
    private JButton btnMarcarLeidas;

    public PnlNotificaciones() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setBackground(new Color(25, 25, 25));
        
        JLabel lblTitulo = new JLabel("Notificaciones y Avisos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);

        btnMarcarLeidas = new JButton("Marcar todas como leídas");
        btnMarcarLeidas.setBackground(new Color(25, 25, 25));
        btnMarcarLeidas.setForeground(new Color(255, 117, 31));
        pnlNorte.add(btnMarcarLeidas, BorderLayout.EAST);
        add(pnlNorte, BorderLayout.NORTH);

        pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setBackground(new Color(35, 35, 35));
        
        JScrollPane scroll = new JScrollPane(pnlLista);
        scroll.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(10, 10, 10, 10)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void renderNotificaciones(java.util.List<model.Notificacion> notificaciones) {
        pnlLista.removeAll();
        for (model.Notificacion n : notificaciones) {
            JPanel card = new JPanel(new BorderLayout(10, 5));
            card.setBackground(n.isLeida() ? new Color(45, 45, 45) : new Color(55, 55, 65));
            card.setBorder(new CompoundBorder(new LineBorder(new Color(70, 70, 70), 1, true), new EmptyBorder(15, 15, 15, 15)));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JLabel lblTit = new JLabel(n.getTitulo());
            lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTit.setForeground(n.isLeida() ? Color.LIGHT_GRAY : Color.WHITE);
            card.add(lblTit, BorderLayout.NORTH);

            JLabel lblMsg = new JLabel("<html>" + n.getMensaje() + "</html>");
            lblMsg.setForeground(Color.LIGHT_GRAY);
            card.add(lblMsg, BorderLayout.CENTER);

            JLabel lblFec = new JLabel(n.getFechaHora());
            lblFec.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblFec.setForeground(Color.GRAY);
            card.add(lblFec, BorderLayout.SOUTH);

            pnlLista.add(card);
            pnlLista.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        pnlLista.revalidate();
        pnlLista.repaint();
    }

    public JButton getBtnMarcarLeidas() { return btnMarcarLeidas; }
}
