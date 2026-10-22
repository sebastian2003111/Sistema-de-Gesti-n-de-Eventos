package controller;

import model.Evento;
import model.EventoService;
import view.FrmListarEventos;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

public class ListarEventosController {

    private final FrmListarEventos vistaListar;
    private final EventoService servicio;

    public ListarEventosController(FrmListarEventos vistaListar, EventoService servicio) {
        this.vistaListar = vistaListar;
        this.servicio = servicio;

        this.vistaListar.getBtnActualizar().addActionListener(e -> cargarDatos());
        this.vistaListar.getBtnCerrar().addActionListener(e -> this.vistaListar.dispose());
    }

    public void iniciar() {
        cargarDatos();
        vistaListar.setVisible(true);
    }

    private void cargarDatos() {
        DefaultTableModel modelo = vistaListar.getModeloTabla();
        modelo.setRowCount(0); // Limpiar la tabla

        List<Evento> eventos = servicio.obtenerTodosLosEventos();
        
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Evento e : eventos) {
            String fecha = e.getFecha() != null ? e.getFecha().format(fechaFormatter) : "";
            String hora = e.getHora() != null ? e.getHora().format(horaFormatter) : "";
            
            Object[] fila = {
                e.getIdEvento(),
                e.getNombre(),
                e.getTipo(),
                fecha,
                hora,
                e.getLugar(),
                e.getEstado()
            };
            modelo.addRow(fila);
        }
    }
}
