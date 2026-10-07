package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class PnlCalendario extends JPanel {
    
    // Navegación
    private JButton btnAnt;
    private JButton btnSig;
    private JButton btnHoy;
    private JLabel lblMesAnio;

    // Filtros
    private JComboBox<String> cbxFiltroTipo;
    private JComboBox<String> cbxFiltroEstado;
    private JButton btnFiltrar;
    private JButton btnLimpiarFiltros;

    // Cuadrícula del Calendario
    private JPanel pnlGridCalendario;

    // Próximo Evento
    private JLabel lblProxNombre;
    private JLabel lblProxFecha;
    private JLabel lblProxHora;
    private JLabel lblProxLugar;
    private JLabel lblProxEstado;
    private JButton btnVerDetallesProximo;

    public PnlCalendario() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 25));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ==========================================
        // ENCABEZADO Y FILTROS (NORTE)
        // ==========================================
        JPanel pnlNorteGeneral = new JPanel(new BorderLayout());
        pnlNorteGeneral.setBackground(new Color(25, 25, 25));

        // Títulos
        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1));
        pnlTitulos.setBackground(new Color(25, 25, 25));
        JLabel lblTitulo = new JLabel("📅 CALENDARIO DE EVENTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Organiza y consulta tus próximos eventos");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSub.setForeground(Color.LIGHT_GRAY);
        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        
        // Filtros (Derecha)
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFiltros.setBackground(new Color(25, 25, 25));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(60, 60, 60)), "🔎 FILTRAR EVENTOS", 
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 11), Color.WHITE));

        cbxFiltroTipo = new JComboBox<>(new String[]{"Todos", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxFiltroEstado = new JComboBox<>(new String[]{"Todos", "Programado", "En proceso", "Finalizado", "Cancelado"});
        btnFiltrar = new JButton("Buscar");
        btnLimpiarFiltros = new JButton("Limpiar");
        btnFiltrar.setBackground(new Color(50, 150, 250)); btnFiltrar.setForeground(Color.WHITE);
        btnLimpiarFiltros.setBackground(new Color(100, 100, 100)); btnLimpiarFiltros.setForeground(Color.WHITE);
        
        pnlFiltros.add(crearLabelBlanco("Tipo:")); pnlFiltros.add(cbxFiltroTipo);
        pnlFiltros.add(crearLabelBlanco("Estado:")); pnlFiltros.add(cbxFiltroEstado);
        pnlFiltros.add(btnFiltrar);
        pnlFiltros.add(btnLimpiarFiltros);

        pnlNorteGeneral.add(pnlTitulos, BorderLayout.WEST);
        pnlNorteGeneral.add(pnlFiltros, BorderLayout.EAST);
        
        // Navegación (Debajo de los títulos)
        JPanel pnlNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlNav.setBackground(new Color(35, 35, 35));
        pnlNav.setBorder(new CompoundBorder(new LineBorder(new Color(60, 60, 60), 1, true), new EmptyBorder(5, 5, 5, 5)));
        
        btnAnt = new JButton("‹"); btnAnt.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMesAnio = new JLabel("SEPTIEMBRE 2026"); lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 18)); lblMesAnio.setForeground(Color.WHITE);
        btnSig = new JButton("›"); btnSig.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnHoy = new JButton("Hoy"); btnHoy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        pnlNav.add(btnAnt); pnlNav.add(lblMesAnio); pnlNav.add(btnSig); pnlNav.add(Box.createRigidArea(new Dimension(20,0))); pnlNav.add(btnHoy);
        pnlNorteGeneral.add(pnlNav, BorderLayout.SOUTH);
        
        add(pnlNorteGeneral, BorderLayout.NORTH);

        // ==========================================
        // CENTRO: GRID DEL CALENDARIO
        // ==========================================
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBackground(new Color(25, 25, 25));

        // Cabecera de días (Lunes, Martes...)
        JPanel pnlDias = new JPanel(new GridLayout(1, 7));
        pnlDias.setBackground(new Color(40, 40, 40));
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (String d : dias) {
            JLabel lblD = new JLabel(d, SwingConstants.CENTER);
            lblD.setForeground(Color.LIGHT_GRAY);
            lblD.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblD.setBorder(new EmptyBorder(10, 0, 10, 0));
            pnlDias.add(lblD);
        }
        pnlCentro.add(pnlDias, BorderLayout.NORTH);

        pnlGridCalendario = new JPanel(new GridLayout(0, 7));
        pnlGridCalendario.setBackground(new Color(60, 60, 60)); // Color de las líneas
        pnlCentro.add(pnlGridCalendario, BorderLayout.CENTER);

        add(pnlCentro, BorderLayout.CENTER);

        // ==========================================
        // SUR/DERECHA: PRÓXIMO EVENTO
        // ==========================================
        JPanel pnlProx = new JPanel(new BorderLayout(10, 10));
        pnlProx.setBackground(new Color(35, 35, 35));
        pnlProx.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitProx = new JLabel("📌 PRÓXIMO EVENTO");
        lblTitProx.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitProx.setForeground(new Color(255, 117, 31));
        pnlProx.add(lblTitProx, BorderLayout.NORTH);

        JPanel pnlDatosProx = new JPanel(new GridLayout(5, 1, 0, 5));
        pnlDatosProx.setBackground(new Color(35, 35, 35));
        lblProxNombre = crearLabelBlanco("Nombre: --");
        lblProxFecha = crearLabelBlanco("Fecha: --");
        lblProxHora = crearLabelBlanco("Hora: --");
        lblProxLugar = crearLabelBlanco("Lugar: --");
        lblProxEstado = crearLabelBlanco("Estado: --");
        
        pnlDatosProx.add(lblProxNombre);
        pnlDatosProx.add(lblProxFecha);
        pnlDatosProx.add(lblProxHora);
        pnlDatosProx.add(lblProxLugar);
        pnlDatosProx.add(lblProxEstado);
        pnlProx.add(pnlDatosProx, BorderLayout.CENTER);

        btnVerDetallesProximo = new JButton("Ver detalles");
        btnVerDetallesProximo.setBackground(new Color(25, 25, 25));
        btnVerDetallesProximo.setForeground(Color.WHITE);
        pnlProx.add(btnVerDetallesProximo, BorderLayout.EAST);

        add(pnlProx, BorderLayout.SOUTH);
    }

    private JLabel crearLabelBlanco(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    // --- Getters ---
    public JButton getBtnAnt() { return btnAnt; }
    public JButton getBtnSig() { return btnSig; }
    public JButton getBtnHoy() { return btnHoy; }
    public JLabel getLblMesAnio() { return lblMesAnio; }
    public JComboBox<String> getCbxFiltroTipo() { return cbxFiltroTipo; }
    public JComboBox<String> getCbxFiltroEstado() { return cbxFiltroEstado; }
    public JButton getBtnFiltrar() { return btnFiltrar; }
    public JButton getBtnLimpiarFiltros() { return btnLimpiarFiltros; }
    public JPanel getPnlGridCalendario() { return pnlGridCalendario; }
    public JLabel getLblProxNombre() { return lblProxNombre; }
    public JLabel getLblProxFecha() { return lblProxFecha; }
    public JLabel getLblProxHora() { return lblProxHora; }
    public JLabel getLblProxLugar() { return lblProxLugar; }
    public JLabel getLblProxEstado() { return lblProxEstado; }
    public JButton getBtnVerDetallesProximo() { return btnVerDetallesProximo; }
}
