package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class FrmDashboard extends JFrame {

    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    
    // Botones del Sidebar
    private JButton btnDashboard;
    private JButton btnEventos;
    private JButton btnCalendario;
    private JButton btnParticipantes;
    private JButton btnInformes;
    private JButton btnConfiguracion;
    private JButton btnActividades;
    private JButton btnNotificaciones;
    private JButton btnSalir;
    
    private JLabel lblUserRole;

    public FrmDashboard() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Eventos");
        setSize(1000, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores base (FlatLaf se encarga de mucho, pero aseguramos la marca)
        Color colorSidebar = new Color(25, 25, 25);
        
        // ==========================================
        // SIDEBAR (Izquierda)
        // ==========================================
        pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(colorSidebar);
        pnlSidebar.setPreferredSize(new Dimension(220, 0));
        pnlSidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Logo
        try {
            java.net.URL logoUrl = getClass().getResource("/img/Logo_negro2.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image img = logoIcon.getImage();
                int width = 180;
                int height = (int) (((double) width / img.getWidth(null)) * img.getHeight(null));
                JLabel lblLogo = new JLabel(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
                pnlSidebar.add(lblLogo);
            }
        } catch (Exception e) {}

        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        lblUserRole = new JLabel("Rol: Cargando...");
        lblUserRole.setForeground(Color.GRAY);
        lblUserRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlSidebar.add(lblUserRole);

        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Inicializar Botones
        btnDashboard = crearBotonMenu("🏠  Inicio");
        btnEventos = crearBotonMenu("📅  Gestión de Eventos");
        btnCalendario = crearBotonMenu("🗓️  Calendario");
        btnParticipantes = crearBotonMenu("👥  Participantes");
        btnActividades = crearBotonMenu("📋  Recursos y Actividades");
        btnInformes = crearBotonMenu("📊  Informes");
        btnNotificaciones = crearBotonMenu("🔔  Notificaciones");
        btnConfiguracion = crearBotonMenu("⚙️  Configuración");
        btnSalir = crearBotonMenu("🚪  Cerrar Sesión");
        btnSalir.setForeground(new Color(255, 99, 71)); // Color rojizo para salir

        pnlSidebar.add(btnDashboard);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnEventos);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnCalendario);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnParticipantes);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnActividades);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnInformes);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnNotificaciones);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(btnConfiguracion);
        
        pnlSidebar.add(Box.createVerticalGlue()); // Empuja el botón salir hacia abajo
        pnlSidebar.add(btnSalir);

        add(pnlSidebar, BorderLayout.WEST);

        // ==========================================
        // ÁREA CENTRAL (Header + Contenido)
        // ==========================================
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBackground(new Color(25, 25, 25));

        // HEADER PRINCIPAL
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(25, 25, 25));
        pnlHeader.setBorder(new CompoundBorder(
                new LineBorder(new Color(40, 40, 40), 1, false), 
                new EmptyBorder(15, 30, 15, 30)
        ));

        // Izquierda del Header
        JPanel pnlHeaderIzq = new JPanel(new GridLayout(2, 1));
        pnlHeaderIzq.setBackground(new Color(25, 25, 25));
        JLabel lblHeaderTit = new JLabel("Sistema de Gestión de Eventos");
        lblHeaderTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeaderTit.setForeground(Color.WHITE);
        JLabel lblHeaderSub = new JLabel("Organiza, administra y vive mejores experiencias");
        lblHeaderSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHeaderSub.setForeground(Color.LIGHT_GRAY);
        pnlHeaderIzq.add(lblHeaderTit);
        pnlHeaderIzq.add(lblHeaderSub);

        // Derecha del Header
        JPanel pnlHeaderDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderDer.setBackground(new Color(25, 25, 25));
        
        // Simulación de fecha/hora (estática o dinámica base)
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy | hh:mm a", new java.util.Locale("es", "ES"));
        JLabel lblFechaHora = new JLabel("🕒 " + dtf.format(now));
        lblFechaHora.setForeground(Color.LIGHT_GRAY);
        lblFechaHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblNotif = new JLabel("🔔");
        lblNotif.setForeground(Color.WHITE);
        
        lblUsuarioHeader = new JLabel("👤 Cargando...");
        lblUsuarioHeader.setForeground(Color.WHITE);

        pnlHeaderDer.add(lblFechaHora);
        pnlHeaderDer.add(lblNotif);
        pnlHeaderDer.add(lblUsuarioHeader);

        pnlHeader.add(pnlHeaderIzq, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderDer, BorderLayout.EAST);

        pnlCentro.add(pnlHeader, BorderLayout.NORTH);

        // ÁREA DE CONTENIDO (CardLayout)
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(new Color(30, 30, 30));
        
        pnlCentro.add(pnlContent, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);
    }
    
    private JLabel lblUsuarioHeader;

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Alinear texto a la izquierda
        btn.setFocusPainted(false);
        // Sin color de fondo fijo para que FlatLaf lo maneje, o uno sutil
        btn.setBackground(new Color(30, 30, 30));
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void agregarPanelContenido(Container panel, String nombreCard) {
        pnlContent.add(panel, nombreCard);
    }

    public void mostrarPanel(String nombreCard) {
        cardLayout.show(pnlContent, nombreCard);
    }

    public void setLblUserRole(String rol) {
        this.lblUserRole.setText("Rol: " + rol);
    }
    
    public void setLblUsuarioHeader(String nombre) {
        this.lblUsuarioHeader.setText("👤 " + nombre);
    }

    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnEventos() { return btnEventos; }
    public JButton getBtnCalendario() { return btnCalendario; }
    public JButton getBtnParticipantes() { return btnParticipantes; }
    public JButton getBtnInformes() { return btnInformes; }
    public JButton getBtnActividades() { return btnActividades; }
    public JButton getBtnNotificaciones() { return btnNotificaciones; }
    public JButton getBtnConfiguracion() { return btnConfiguracion; }
    public JButton getBtnSalir() { return btnSalir; }
}
