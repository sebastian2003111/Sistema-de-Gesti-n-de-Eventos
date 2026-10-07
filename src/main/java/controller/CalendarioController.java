package controller;

import model.Evento;
import model.EventoRepository;
import view.PnlCalendario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.border.LineBorder;

public class CalendarioController {
    private PnlCalendario vista;
    private YearMonth mesActual;
    private Evento proximoEvento;

    public CalendarioController(PnlCalendario vista) {
        this.vista = vista;
        this.mesActual = YearMonth.now();

        vista.getBtnAnt().addActionListener(e -> cambiarMes(-1));
        vista.getBtnSig().addActionListener(e -> cambiarMes(1));
        vista.getBtnHoy().addActionListener(e -> {
            mesActual = YearMonth.now();
            actualizarCalendario();
        });

        vista.getBtnFiltrar().addActionListener(e -> actualizarCalendario());
        vista.getBtnLimpiarFiltros().addActionListener(e -> {
            vista.getCbxFiltroTipo().setSelectedIndex(0);
            vista.getCbxFiltroEstado().setSelectedIndex(0);
            actualizarCalendario();
        });

        vista.getBtnVerDetallesProximo().addActionListener(e -> mostrarDetalles(proximoEvento));
    }

    public void iniciar() {
        mesActual = YearMonth.now();
        actualizarCalendario();
    }

    private void cambiarMes(int offset) {
        mesActual = mesActual.plusMonths(offset);
        actualizarCalendario();
    }

    private void actualizarCalendario() {
        // Actualizar título de mes y año
        String mesStr = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        vista.getLblMesAnio().setText((mesStr + " " + mesActual.getYear()).toUpperCase());

        // Obtener eventos filtrados
        List<Evento> todos = EventoRepository.getInstance().obtenerTodos();
        String tipoFiltro = vista.getCbxFiltroTipo().getSelectedItem().toString();
        String estadoFiltro = vista.getCbxFiltroEstado().getSelectedItem().toString();

        List<Evento> filtrados = todos.stream().filter(e -> {
            boolean cTipo = tipoFiltro.equals("Todos") || (e.getTipo() != null && e.getTipo().equals(tipoFiltro));
            boolean cEst = estadoFiltro.equals("Todos") || (e.getEstado() != null && e.getEstado().equalsIgnoreCase(estadoFiltro));
            return cTipo && cEst;
        }).collect(Collectors.toList());

        // Limpiar Grid
        JPanel pnlGrid = vista.getPnlGridCalendario();
        pnlGrid.removeAll();

        // Calcular celdas
        LocalDate primerDiaMes = mesActual.atDay(1);
        int diaSemanaPrimerDia = primerDiaMes.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo

        int diasEnMes = mesActual.lengthOfMonth();
        int totalCeldas = 42; // 6 filas de 7 dias

        for (int i = 1; i <= totalCeldas; i++) {
            JPanel celda = new JPanel(new BorderLayout());
            celda.setBackground(new Color(30, 30, 30));
            celda.setBorder(new LineBorder(new Color(60, 60, 60)));

            int diaDelMes = i - diaSemanaPrimerDia + 1;
            
            if (diaDelMes > 0 && diaDelMes <= diasEnMes) {
                LocalDate fechaCelda = mesActual.atDay(diaDelMes);
                
                // Pintar de un color levemente distinto si es HOY
                if (fechaCelda.equals(LocalDate.now())) {
                    celda.setBackground(new Color(45, 45, 55));
                }

                // Header de la celda (Numero de dia)
                JLabel lblNumero = new JLabel(String.valueOf(diaDelMes));
                lblNumero.setForeground(Color.LIGHT_GRAY);
                lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblNumero.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
                celda.add(lblNumero, BorderLayout.NORTH);

                // Contenedor de eventos
                JPanel pnlEventos = new JPanel();
                pnlEventos.setLayout(new BoxLayout(pnlEventos, BoxLayout.Y_AXIS));
                pnlEventos.setOpaque(false);
                pnlEventos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                // Buscar eventos para este dia
                List<Evento> eventosDia = filtrados.stream()
                        .filter(e -> e.getFecha() != null && e.getFecha().equals(fechaCelda))
                        .collect(Collectors.toList());

                for (Evento ev : eventosDia) {
                    JLabel lblEvt = new JLabel(obtenerIconoEstado(ev.getEstado()) + " " + ev.getNombre());
                    lblEvt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    lblEvt.setForeground(Color.WHITE);
                    lblEvt.setToolTipText("Clic para ver detalles");
                    lblEvt.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    // Al hacer clic, ver detalles
                    lblEvt.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            mostrarDetalles(ev);
                        }
                    });
                    
                    pnlEventos.add(lblEvt);
                    pnlEventos.add(Box.createRigidArea(new Dimension(0, 3)));
                }

                JScrollPane scroll = new JScrollPane(pnlEventos);
                scroll.setBorder(null);
                scroll.setOpaque(false);
                scroll.getViewport().setOpaque(false);
                celda.add(scroll, BorderLayout.CENTER);
            } else {
                // Celdas vacías (días de otros meses)
                celda.setBackground(new Color(20, 20, 20));
            }
            pnlGrid.add(celda);
        }

        pnlGrid.revalidate();
        pnlGrid.repaint();

        // Actualizar panel de Próximo Evento
        actualizarProximoEvento(todos);
    }

    private String obtenerIconoEstado(String estado) {
        if (estado == null) return "⚪";
        if (estado.equalsIgnoreCase("Programado")) return "🟢";
        if (estado.equalsIgnoreCase("En proceso")) return "🟡";
        if (estado.equalsIgnoreCase("Finalizado")) return "🔵";
        if (estado.equalsIgnoreCase("Cancelado")) return "🔴";
        return "⚪";
    }

    private void actualizarProximoEvento(List<Evento> todos) {
        proximoEvento = todos.stream()
                .filter(e -> e.getFecha() != null && !e.getFecha().isBefore(LocalDate.now()))
                .filter(e -> "Programado".equalsIgnoreCase(e.getEstado()) || "En proceso".equalsIgnoreCase(e.getEstado()))
                .sorted((e1, e2) -> e1.getFecha().compareTo(e2.getFecha()))
                .findFirst()
                .orElse(null);

        if (proximoEvento != null) {
            vista.getLblProxNombre().setText("Nombre: " + proximoEvento.getNombre());
            vista.getLblProxFecha().setText("Fecha: " + proximoEvento.getFecha().toString());
            vista.getLblProxHora().setText("Hora: " + (proximoEvento.getHora() != null ? proximoEvento.getHora().toString() : "--"));
            vista.getLblProxLugar().setText("Lugar: " + proximoEvento.getLugar());
            vista.getLblProxEstado().setText("Estado: " + obtenerIconoEstado(proximoEvento.getEstado()) + " " + proximoEvento.getEstado());
            vista.getBtnVerDetallesProximo().setEnabled(true);
        } else {
            vista.getLblProxNombre().setText("Nombre: --");
            vista.getLblProxFecha().setText("Fecha: --");
            vista.getLblProxHora().setText("Hora: --");
            vista.getLblProxLugar().setText("Lugar: --");
            vista.getLblProxEstado().setText("Estado: --");
            vista.getBtnVerDetallesProximo().setEnabled(false);
        }
    }

    private void mostrarDetalles(Evento ev) {
        if (ev == null) return;
        
        String detalles = "ID: " + ev.getIdEvento() + "\n"
                + "Nombre: " + ev.getNombre() + "\n"
                + "Tipo: " + ev.getTipo() + "\n"
                + "Fecha: " + (ev.getFecha() != null ? ev.getFecha().toString() : "N/A") + "\n"
                + "Hora: " + (ev.getHora() != null ? ev.getHora().toString() : "N/A") + "\n"
                + "Lugar: " + ev.getLugar() + "\n"
                + "Estado: " + ev.getEstado() + "\n\n"
                + "Descripción:\n" + ev.getDescripcion();

        JTextArea textArea = new JTextArea(detalles);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setOpaque(false);
        textArea.setForeground(Color.WHITE);

        JOptionPane.showMessageDialog(vista, textArea, "Detalles del Evento", JOptionPane.INFORMATION_MESSAGE);
    }
}
