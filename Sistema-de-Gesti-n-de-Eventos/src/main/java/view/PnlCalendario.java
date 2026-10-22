package view;

import config.ThemeManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class PnlCalendario extends JPanel implements ThemeManager.ThemeListener {

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

    // Referencias estructurales para re-theming
    private JPanel pnlNorteGeneral;
    private JPanel pnlFilaTop;
    private JPanel pnlTitulos;
    private JLabel lblTitulo;
    private JLabel lblSub;
    private JPanel pnlFiltros;
    private JLabel lblFiltroTit;
    private JLabel lblTipo;
    private JLabel lblEstado;
    private JPanel pnlNav;
    private JPanel pnlCentro;
    private JPanel pnlDias;
    private JPanel pnlProx;
    private JLabel lblTitProx;
    private final List<JLabel> listaDiasLabels = new ArrayList<>();
    private final List<JLabel> listaLabelsDatoProx = new ArrayList<>();
    private final List<JButton> listaBotonesSecundarios = new ArrayList<>();

    public PnlCalendario() {
        initComponents();
        ThemeManager.registrarListener(this);
        aplicarTema(ThemeManager.isDark());
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(22, 28, 22, 28));

        // ==========================================
        // 1. ZONA SUPERIOR: ENCABEZADO Y FILTROS
        // ==========================================
        pnlNorteGeneral = new JPanel();
        pnlNorteGeneral.setLayout(new BoxLayout(pnlNorteGeneral, BoxLayout.Y_AXIS));

        // Fila 1: Títulos a la izquierda y Filtros a la derecha
        pnlFilaTop = new JPanel(new BorderLayout(15, 0));
        pnlFilaTop.setOpaque(false);

        pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 3));
        pnlTitulos.setOpaque(false);

        lblTitulo = new JLabel("🗓️  Calendario Interactivo de Eventos");
        lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));

        lblSub = new JLabel("Explora la distribución mensual, consulta fechas programadas y filtra por categoría o estado.");
        lblSub.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        pnlFilaTop.add(pnlTitulos, BorderLayout.WEST);

        // Panel de Filtros Estilizado
        pnlFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));

        lblFiltroTit = new JLabel("🔍 Filtrar: ");
        lblFiltroTit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lblFiltroTit.setForeground(ThemeManager.getColorNaranja());
        pnlFiltros.add(lblFiltroTit);

        lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(lblTipo);

        cbxFiltroTipo = new JComboBox<>(new String[]{"Todos", "Académico", "Conferencia", "Social", "Empresarial", "Cultural", "Deportivo"});
        cbxFiltroTipo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(cbxFiltroTipo);

        lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(lblEstado);

        cbxFiltroEstado = new JComboBox<>(new String[]{"Todos", "Programado", "En proceso", "Finalizado", "Cancelado"});
        cbxFiltroEstado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlFiltros.add(cbxFiltroEstado);

        btnFiltrar = crearBotonPequeno("Buscar", new Color(52, 152, 219));
        btnLimpiarFiltros = crearBotonPequeno("Limpiar", new Color(110, 115, 130));

        pnlFiltros.add(btnFiltrar);
        pnlFiltros.add(btnLimpiarFiltros);

        pnlFilaTop.add(pnlFiltros, BorderLayout.EAST);
        pnlNorteGeneral.add(pnlFilaTop);
        pnlNorteGeneral.add(Box.createRigidArea(new Dimension(0, 12)));

        // Fila 2: Barra de Navegación del Mes
        pnlNav = new JPanel(new BorderLayout());

        JPanel pnlNavCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        pnlNavCentro.setOpaque(false);

        btnAnt = new JButton("◀  Anterior");
        configurarBotonNav(btnAnt);

        lblMesAnio = new JLabel("SEPTIEMBRE 2026", SwingConstants.CENTER);
        lblMesAnio.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
        lblMesAnio.setPreferredSize(new Dimension(220, 32));

        btnSig = new JButton("Siguiente  ▶");
        configurarBotonNav(btnSig);

        pnlNavCentro.add(btnAnt);
        pnlNavCentro.add(lblMesAnio);
        pnlNavCentro.add(btnSig);

        pnlNav.add(pnlNavCentro, BorderLayout.CENTER);

        // Botón "Hoy" a la derecha
        btnHoy = new JButton("📍 Hoy");
        btnHoy.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnHoy.setBackground(new Color(255, 117, 31, 28));
        btnHoy.setForeground(ThemeManager.getColorNaranja());
        btnHoy.setFocusPainted(false);
        btnHoy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHoy.setBorder(new CompoundBorder(
                new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                new EmptyBorder(6, 14, 6, 14)
        ));
        btnHoy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnHoy.setBackground(ThemeManager.getColorNaranja());
                btnHoy.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnHoy.setBackground(new Color(255, 117, 31, 28));
                btnHoy.setForeground(ThemeManager.getColorNaranja());
            }
        });
        pnlNav.add(btnHoy, BorderLayout.EAST);

        pnlNorteGeneral.add(pnlNav);
        add(pnlNorteGeneral, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTRO: CUADRÍCULA DEL CALENDARIO
        // ==========================================
        pnlCentro = new JPanel(new BorderLayout());

        // Barra con los 7 días de la semana
        pnlDias = new JPanel(new GridLayout(1, 7, 2, 0));

        String[] dias = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO"};
        for (int i = 0; i < dias.length; i++) {
            JLabel lblD = new JLabel(dias[i], SwingConstants.CENTER);
            lblD.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
            lblD.setBorder(new EmptyBorder(8, 0, 8, 0));
            pnlDias.add(lblD);
            listaDiasLabels.add(lblD);
        }
        pnlCentro.add(pnlDias, BorderLayout.NORTH);

        // Cuadrícula de celdas
        pnlGridCalendario = new JPanel(new GridLayout(0, 7, 3, 3));
        pnlCentro.add(pnlGridCalendario, BorderLayout.CENTER);

        add(pnlCentro, BorderLayout.CENTER);

        // ==========================================
        // 3. SUR: TARJETA DEL PRÓXIMO EVENTO
        // ==========================================
        pnlProx = new JPanel(new BorderLayout(15, 0));

        // Izquierda con badge "Próximo Evento"
        JPanel pnlProxTitWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        pnlProxTitWrapper.setOpaque(false);
        JLabel lblIconPin = new JLabel("📌");
        lblIconPin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblTitProx = new JLabel("PRÓXIMO EVENTO EN AGENDA");
        lblTitProx.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblTitProx.setForeground(ThemeManager.getColorNaranja());
        pnlProxTitWrapper.add(lblIconPin);
        pnlProxTitWrapper.add(lblTitProx);
        pnlProx.add(pnlProxTitWrapper, BorderLayout.WEST);

        // Centro con datos
        JPanel pnlDatosProx = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        pnlDatosProx.setOpaque(false);

        lblProxNombre = crearLabelDato("Nombre: --");
        lblProxFecha = crearLabelDato("Fecha: --");
        lblProxHora = crearLabelDato("Hora: --");
        lblProxLugar = crearLabelDato("Lugar: --");
        lblProxEstado = crearLabelDato("Estado: --");

        pnlDatosProx.add(lblProxNombre);
        pnlDatosProx.add(lblProxFecha);
        pnlDatosProx.add(lblProxHora);
        pnlDatosProx.add(lblProxLugar);
        pnlDatosProx.add(lblProxEstado);
        pnlProx.add(pnlDatosProx, BorderLayout.CENTER);

        // Derecha: Botón de detalles
        btnVerDetallesProximo = new JButton("Ver Detalles ➔");
        btnVerDetallesProximo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnVerDetallesProximo.setFocusPainted(false);
        btnVerDetallesProximo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerDetallesProximo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVerDetallesProximo.setBackground(ThemeManager.getColorNaranja());
                btnVerDetallesProximo.setForeground(Color.WHITE);
                btnVerDetallesProximo.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorNaranja(), 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVerDetallesProximo.setBackground(ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249));
                btnVerDetallesProximo.setForeground(ThemeManager.getColorTextoTitulo());
                btnVerDetallesProximo.setBorder(new CompoundBorder(
                        new LineBorder(ThemeManager.getColorBorde(), 1, true),
                        new EmptyBorder(8, 14, 8, 14)
                ));
            }
        });
        listaBotonesSecundarios.add(btnVerDetallesProximo);
        pnlProx.add(btnVerDetallesProximo, BorderLayout.EAST);

        add(pnlProx, BorderLayout.SOUTH);
    }

    private JLabel crearLabelDato(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        listaLabelsDatoProx.add(l);
        return l;
    }

    private void configurarBotonNav(JButton btn) {
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(48, 48, 62) : new Color(228, 233, 240));
                btn.setForeground(ThemeManager.getColorNaranja());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249));
                btn.setForeground(ThemeManager.getColorTextoTitulo());
            }
        });
        listaBotonesSecundarios.add(btn);
    }

    private JButton crearBotonPequeno(String texto, Color colorAcento) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorAcento);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ThemeManager.isDark() ? new Color(38, 38, 48) : new Color(241, 245, 249));
                btn.setForeground(ThemeManager.getColorTextoTitulo());
            }
        });
        listaBotonesSecundarios.add(btn);
        return btn;
    }

    @Override
    public void aplicarTema(boolean isDark) {
        Color bg = ThemeManager.getColorBg();
        Color cardBg = ThemeManager.getColorCardBg();
        Color borde = ThemeManager.getColorBorde();
        Color textoTit = ThemeManager.getColorTextoTitulo();
        Color textoMuted = ThemeManager.getColorTextoMuted();
        Color textoNorm = ThemeManager.getColorTextoNormal();
        Color inputBg = ThemeManager.getColorInputBg();

        setBackground(bg);
        if (pnlNorteGeneral != null) pnlNorteGeneral.setBackground(bg);
        if (pnlCentro != null) pnlCentro.setBackground(bg);

        if (lblTitulo != null) lblTitulo.setForeground(textoTit);
        if (lblSub != null) lblSub.setForeground(textoMuted);
        if (lblTipo != null) lblTipo.setForeground(textoMuted);
        if (lblEstado != null) lblEstado.setForeground(textoMuted);

        if (pnlFiltros != null) {
            pnlFiltros.setBackground(cardBg);
            pnlFiltros.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));
        }

        if (cbxFiltroTipo != null) {
            cbxFiltroTipo.setBackground(inputBg);
            cbxFiltroTipo.setForeground(textoNorm);
        }
        if (cbxFiltroEstado != null) {
            cbxFiltroEstado.setBackground(inputBg);
            cbxFiltroEstado.setForeground(textoNorm);
        }

        if (pnlNav != null) {
            pnlNav.setBackground(cardBg);
            pnlNav.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(8, 16, 8, 16)
            ));
        }

        if (lblMesAnio != null) lblMesAnio.setForeground(textoTit);

        if (pnlDias != null) {
            pnlDias.setBackground(isDark ? new Color(32, 32, 42) : new Color(241, 245, 249));
            pnlDias.setBorder(new MatteBorder(0, 0, 1, 0, borde));
        }

        for (int i = 0; i < listaDiasLabels.size(); i++) {
            JLabel lblD = listaDiasLabels.get(i);
            if (i >= 5) {
                lblD.setForeground(new Color(255, 140, 60));
            } else {
                lblD.setForeground(textoMuted);
            }
        }

        if (pnlGridCalendario != null) {
            pnlGridCalendario.setBackground(isDark ? new Color(18, 18, 22) : new Color(226, 232, 240));
            pnlGridCalendario.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(3, 3, 3, 3)
            ));
        }

        if (pnlProx != null) {
            pnlProx.setBackground(cardBg);
            pnlProx.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(12, 18, 12, 18)
            ));
        }

        for (JLabel l : listaLabelsDatoProx) {
            l.setForeground(textoNorm);
        }

        for (JButton b : listaBotonesSecundarios) {
            b.setBackground(isDark ? new Color(38, 38, 48) : new Color(241, 245, 249));
            b.setForeground(textoTit);
            b.setBorder(new CompoundBorder(
                    new LineBorder(borde, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));
        }

        repaint();
    }

    // --- Getters requeridos por el Controlador ---
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
