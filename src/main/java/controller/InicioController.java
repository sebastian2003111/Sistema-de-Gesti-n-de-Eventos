package controller;

import model.Evento;
import model.EventoService;
import view.PnlInicio;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class InicioController {

    private final PnlInicio panelInicio;
    private final EventoService servicioEvento;

    public InicioController(PnlInicio panelInicio, EventoService servicioEvento) {
        this.panelInicio = panelInicio;
        this.servicioEvento = servicioEvento;
    }

    public void iniciar() {
        actualizarDatos();
    }

    public void actualizarDatos() {
        List<Evento> todosLosEventos = servicioEvento.obtenerTodosLosEventos();
        LocalDate hoy = LocalDate.now();

        int total = todosLosEventos.size();
        
        // Eventos que son hoy o en el futuro
        List<Evento> proximos = todosLosEventos.stream()
                .filter(e -> !e.getFecha().isBefore(hoy))
                .collect(Collectors.toList());
                
        // Eventos que ya pasaron
        List<Evento> finalizados = todosLosEventos.stream()
                .filter(e -> e.getFecha().isBefore(hoy))
                .collect(Collectors.toList());

        panelInicio.setTotalEventos(total);
        panelInicio.setProximosEventos(proximos.size());
        panelInicio.setEventosFinalizados(finalizados.size());

        // Llenar la tabla con los próximos eventos (máximo 5), ordenados por fecha más cercana
        List<Evento> proximosOrdenados = proximos.stream()
                .sorted(Comparator.comparing(Evento::getFecha))
                .limit(5)
                .collect(Collectors.toList());

        DefaultTableModel modelo = panelInicio.getModeloTabla();
        modelo.setRowCount(0); // Limpiar tabla
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Evento e : proximosOrdenados) {
            modelo.addRow(new Object[]{
                    e.getIdEvento(),
                    e.getNombre(),
                    e.getTipo(),
                    e.getFecha().format(formatter),
                    e.getLugar()
            });
        }
    }
}
