package view;

import config.ThemeManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Notificacion;

public class PnlNotificaciones extends JPanel implements ThemeManager.ThemeListener {

    private JPanel pnlLista;
    private JButton btnMarcarLeidas;
    private JLabel lblContador;
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JPanel pnlNorte;
    private JScrollPane scroll;

    private List<Notificacion> ultimasNotificaciones;

    public PnlNotificaciones() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // NORTE
        pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 3));
        pnlTitulos.setOpaque(false);

        JPanel pnlTituloConBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlTituloConBadge.setOpaque(false);

        lblTitulo = new JLabel("🔔  Buzón de Notificaciones y Auditoría");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        pnlTituloConBadge.add(lblTitulo);

        lblContador = new JLabel(" 0 Avisos ");
        lblContador.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        lblContador.setForeground(ThemeManager.getColorNaranja());
        lblContador.setOpaque(true);
        lblContador.setBackground(new Color(255, 117, 31, 30));
        lblContador.setBorder(new CompoundBorder(
                new LineBorder(new Color(255, 117, 31, 80), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));
        pnlTituloConBadge.add(lblContador);

        lblSub = new JLabel("Registro histórico de eventos creados, actualizaciones de estado y confirmaciones de correo.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlTitulos.add(pnlTituloConBadge);
        pnlTitulos.add(lblSub);
        pnlNorte.add(pnlTitulos, BorderLayout.WEST);

        // Botón Marcar todas como leídas
        btnMarcarLeidas = new JButton("✓  Marcar todas como leídas");
        btnMarcarLeidas.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnMarcarLeidas.setFocusPainted(false);
        btnMarcarLeidas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMarcarLeidas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnMarcarLeidas.setBackground(ThemeManager.isDark() ? new Color(48, 48, 62) : new Color(228, 233, 240));
                btnMarcarLeidas.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                        new EmptyBorder(8, 16, 8, 16)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnMarcarLeidas.setBackground(ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249));
                btnMarcarLeidas.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(8, 16, 8, 16)
                ));
                btnMarcarLeidas.setForeground(ThemeManager.getColorTextoTitulo());
            }
        });
        pnlNorte.add(btnMarcarLeidas, BorderLayout.EAST);

        add(pnlNorte, BorderLayout.NORTH);

        // CENTRO: Lista scrolleable
        pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(pnlLista);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void renderNotificaciones(List<Notificacion> notificaciones) {
        this.ultimasNotificaciones = notificaciones;
        pnlLista.removeAll();

        boolean isDark = ThemeManager.isDark();
        Color bgCardDefault = ThemeManager.getColorCardBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();

        if (notificaciones == null || notificaciones.isEmpty()) {
            lblContador.setText(" 0 Avisos ");
            JPanel pnlVacio = new JPanel(new GridBagLayout());
            pnlVacio.setBackground(ThemeManager.getColorBg());

            JLabel lblEmpty = new JLabel("<html><center><font size='5'>🔔</font><br><br><b>No hay notificaciones registradas.</b><br><font color='" + (isDark ? "#9BA0AF" : "#64748B") + "'>Las acciones del sistema se registrarán aquí automáticamente.</font></center></html>");
            lblEmpty.setForeground(textoMuted);
            pnlVacio.add(lblEmpty);

            pnlLista.add(pnlVacio);
            pnlLista.revalidate();
            pnlLista.repaint();
            return;
        }

        long sinLeer = notificaciones.stream().filter(n -> !n.isLeida()).count();
        lblContador.setText(" " + notificaciones.size() + " Total (" + sinLeer + " Nuevas) ");

        for (Notificacion n : notificaciones) {
            boolean noLeida = !n.isLeida();
            Color bgCard = noLeida ? (isDark ? new Color(34, 34, 46) : new Color(240, 247, 255)) : bgCardDefault;
            Color borderColor = noLeida ? (isDark ? new Color(52, 152, 219, 130) : new Color(52, 152, 219)) : borde;

            JPanel card = new JPanel(new BorderLayout(12, 6)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);
            card.setBackground(bgCard);
            card.setBorder(new CompoundBorder(
                    new LineBorder(borderColor, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));

            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(isDark ? new Color(42, 42, 56) : new Color(235, 243, 252));
                    card.setBorder(new CompoundBorder(
                            new LineBorder(noLeida ? new Color(52, 152, 219) : ThemeManager.getColorNaranja(), 1, true),
                            new EmptyBorder(12, 16, 12, 16)
                    ));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBackground(bgCard);
                    card.setBorder(new CompoundBorder(
                            new LineBorder(borderColor, 1, true),
                            new EmptyBorder(12, 16, 12, 16)
                    ));
                }
            });

            // Barra izquierda indicadora
            JPanel pnlBarra = new JPanel();
            pnlBarra.setBackground(noLeida ? new Color(52, 152, 219) : (isDark ? new Color(70, 75, 90) : new Color(200, 205, 215)));
            pnlBarra.setPreferredSize(new Dimension(4, 0));
            card.add(pnlBarra, BorderLayout.WEST);

            // Contenido central
            JPanel pnlCentro = new JPanel();
            pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
            pnlCentro.setOpaque(false);

            JPanel pnlTopCard = new JPanel(new BorderLayout());
            pnlTopCard.setOpaque(false);

            JLabel lblTit = new JLabel(n.getTitulo());
            lblTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            lblTit.setForeground(textoTit);

            JLabel lblBadgeEstado = new JLabel(noLeida ? " ● NUEVA " : " ✓ Leída ");
            lblBadgeEstado.setFont(new Font("Segoe UI Emoji", Font.BOLD, 10));
            lblBadgeEstado.setForeground(noLeida ? new Color(52, 152, 219) : textoMuted);

            pnlTopCard.add(lblTit, BorderLayout.WEST);
            pnlTopCard.add(lblBadgeEstado, BorderLayout.EAST);
            pnlCentro.add(pnlTopCard);
            pnlCentro.add(Box.createRigidArea(new Dimension(0, 4)));

            String msgColor = isDark ? "#C5CAD6" : "#334155";
            JLabel lblMsg = new JLabel("<html><p style='color:" + msgColor + ";'>" + n.getMensaje() + "</p></html>");
            lblMsg.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            pnlCentro.add(lblMsg);
            pnlCentro.add(Box.createRigidArea(new Dimension(0, 4)));

            JLabel lblFec = new JLabel("🕒  " + n.getFechaHora());
            lblFec.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
            lblFec.setForeground(textoMuted);
            pnlCentro.add(lblFec);

            card.add(pnlCentro, BorderLayout.CENTER);

            pnlLista.add(card);
            pnlLista.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        pnlLista.revalidate();
        pnlLista.repaint();
    }

    @Override
    public void aplicarTema(boolean isDark) {
        Color bg = ThemeManager.getColorBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();

        setBackground(bg);
        if (lblTitulo != null) lblTitulo.setForeground(textoTit);
        if (lblSub != null) lblSub.setForeground(textoMuted);

        if (pnlLista != null) pnlLista.setBackground(bg);
        if (scroll != null) {
            scroll.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(12, 12, 12, 12)
            ));
            scroll.getViewport().setBackground(bg);
        }

        if (btnMarcarLeidas != null) {
            btnMarcarLeidas.setBackground(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
            btnMarcarLeidas.setForeground(textoTit);
            btnMarcarLeidas.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(8, 16, 8, 16)
            ));
        }

        if (ultimasNotificaciones != null) {
            renderNotificaciones(ultimasNotificaciones);
        }

        repaint();
    }

    public JButton getBtnMarcarLeidas() { return btnMarcarLeidas; }
}
