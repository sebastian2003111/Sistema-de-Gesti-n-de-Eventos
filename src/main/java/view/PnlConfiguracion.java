package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class PnlConfiguracion extends JPanel {
    
    // Perfil
    private JLabel lblNombre;
    private JLabel lblCorreo;
    private JPasswordField txtNuevaPass;
    private JButton btnActualizarPass;

    // Apariencia
    private JRadioButton rbDark;
    private JRadioButton rbLight;
    private JComboBox<String> cbxTamanoInterfaz;

    // Notificaciones
    private JCheckBox chkActivarNotificaciones;
    private JCheckBox chkAvisosEventos;

    // Datos
    private JButton btnRecargarDatos;
    private JButton btnLimpiarDatos;

    public PnlConfiguracion() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Configuración del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        add(lblTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(35, 35, 35));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Perfil", crearPanelPerfil());
        tabbedPane.addTab("Apariencia", crearPanelApariencia());
        tabbedPane.addTab("Notificaciones", crearPanelNotificaciones());
        tabbedPane.addTab("Eventos", crearPanelEventos());
        tabbedPane.addTab("Datos", crearPanelDatos());
        tabbedPane.addTab("Acerca de", crearPanelAcerca());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelBase() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(new Color(35, 35, 35));
        pnl.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(30, 40, 30, 40)));
        return pnl;
    }

    private JPanel crearContenedorTab(JPanel contenido) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(25, 25, 25));
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrapper.add(contenido, BorderLayout.NORTH);
        return wrapper;
    }

    private GridBagConstraints getDefaultGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 10, 0);
        return gbc;
    }

    private JPanel crearPanelPerfil() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("INFORMACIÓN DE USUARIO"), gbc);
        gbc.gridy++; lblNombre = crearLabelNormal("Nombre/Usuario: Cargando..."); pnl.add(lblNombre, gbc);
        gbc.gridy++; lblCorreo = crearLabelNormal("Correo: Cargando..."); pnl.add(lblCorreo, gbc);

        gbc.gridy++; gbc.insets = new Insets(30, 0, 5, 0);
        pnl.add(crearLabelNormal("Nueva Contraseña (Opcional):"), gbc);

        gbc.gridy++; gbc.insets = new Insets(0, 0, 15, 0); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNuevaPass = new JPasswordField();
        txtNuevaPass.setBackground(new Color(25, 25, 25));
        txtNuevaPass.setForeground(Color.WHITE);
        txtNuevaPass.setCaretColor(Color.WHITE);
        pnl.add(txtNuevaPass, gbc);

        gbc.gridy++; gbc.fill = GridBagConstraints.NONE;
        btnActualizarPass = new JButton("Actualizar Contraseña");
        btnActualizarPass.setBackground(new Color(255, 117, 31));
        btnActualizarPass.setForeground(Color.WHITE);
        pnl.add(btnActualizarPass, gbc);

        return crearContenedorTab(pnl);
    }

    private JPanel crearPanelApariencia() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("TEMA VISUAL"), gbc);
        gbc.gridy++; rbDark = new JRadioButton("Tema Oscuro (FlatDarkLaf)");
        rbDark.setForeground(Color.WHITE); rbDark.setBackground(new Color(35, 35, 35)); rbDark.setSelected(true);
        pnl.add(rbDark, gbc);

        gbc.gridy++; rbLight = new JRadioButton("Tema Claro (FlatLightLaf)");
        rbLight.setForeground(Color.WHITE); rbLight.setBackground(new Color(35, 35, 35));
        pnl.add(rbLight, gbc);

        ButtonGroup group = new ButtonGroup(); group.add(rbDark); group.add(rbLight);

        gbc.gridy++; gbc.insets = new Insets(30, 0, 10, 0);
        pnl.add(crearTituloSeccion("TAMAÑO DE INTERFAZ"), gbc);
        gbc.gridy++; gbc.insets = new Insets(0, 0, 10, 0);
        cbxTamanoInterfaz = new JComboBox<>(new String[]{"Pequeño", "Normal", "Grande"});
        cbxTamanoInterfaz.setSelectedIndex(1);
        pnl.add(cbxTamanoInterfaz, gbc);

        return crearContenedorTab(pnl);
    }

    private JPanel crearPanelNotificaciones() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("PREFERENCIAS DE AVISOS"), gbc);
        gbc.gridy++; chkActivarNotificaciones = new JCheckBox("Activar notificaciones globales del sistema");
        chkActivarNotificaciones.setForeground(Color.WHITE); chkActivarNotificaciones.setBackground(new Color(35, 35, 35)); chkActivarNotificaciones.setSelected(true);
        pnl.add(chkActivarNotificaciones, gbc);

        gbc.gridy++; chkAvisosEventos = new JCheckBox("Recibir alertas automáticas de nuevos eventos");
        chkAvisosEventos.setForeground(Color.WHITE); chkAvisosEventos.setBackground(new Color(35, 35, 35)); chkAvisosEventos.setSelected(true);
        pnl.add(chkAvisosEventos, gbc);

        return crearContenedorTab(pnl);
    }

    private JPanel crearPanelEventos() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("CONFIGURACIÓN DE EVENTOS"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Tipos permitidos: Académico, Conferencia, Social, Empresarial, Cultural, Deportivo"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Estados automáticos: Próximo (Fecha futura), Finalizado (Fecha pasada)"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Zona Horaria por defecto: Sistema Local"), gbc);

        return crearContenedorTab(pnl);
    }

    private JPanel crearPanelDatos() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("GESTIÓN DE DATOS EN MEMORIA"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("ADVERTENCIA: Limpiar datos borrará todos los eventos y usuarios en sesión."), gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBotones.setBackground(new Color(35, 35, 35));
        
        btnRecargarDatos = new JButton("Recargar Vistas");
        btnRecargarDatos.setBackground(new Color(50, 50, 50)); btnRecargarDatos.setForeground(Color.WHITE);
        pnlBotones.add(btnRecargarDatos);

        btnLimpiarDatos = new JButton("Limpiar Todos Los Datos");
        btnLimpiarDatos.setBackground(new Color(200, 50, 50)); btnLimpiarDatos.setForeground(Color.WHITE);
        pnlBotones.add(btnLimpiarDatos);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 0, 0);
        pnl.add(pnlBotones, gbc);

        return crearContenedorTab(pnl);
    }

    private JPanel crearPanelAcerca() {
        JPanel pnl = crearPanelBase();
        GridBagConstraints gbc = getDefaultGBC();

        pnl.add(crearTituloSeccion("SISTEMA DE GESTIÓN DE EVENTOS"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Versión: 1.6 (6° Incremento)"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Arquitectura: Java 17 / MVC / FlatLaf"), gbc);
        gbc.gridy++; pnl.add(crearLabelNormal("Módulos activos: 6/6 de la Arquitectura Conceptual"), gbc);

        return crearContenedorTab(pnl);
    }

    private JLabel crearTituloSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(255, 117, 31));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private JLabel crearLabelNormal(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    // Getters para el Controlador
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblCorreo() { return lblCorreo; }
    public JPasswordField getTxtNuevaPass() { return txtNuevaPass; }
    public JButton getBtnActualizarPass() { return btnActualizarPass; }
    public JRadioButton getRbDark() { return rbDark; }
    public JRadioButton getRbLight() { return rbLight; }
    public JComboBox<String> getCbxTamanoInterfaz() { return cbxTamanoInterfaz; }
    public JCheckBox getChkActivarNotificaciones() { return chkActivarNotificaciones; }
    public JCheckBox getChkAvisosEventos() { return chkAvisosEventos; }
    public JButton getBtnRecargarDatos() { return btnRecargarDatos; }
    public JButton getBtnLimpiarDatos() { return btnLimpiarDatos; }
}
