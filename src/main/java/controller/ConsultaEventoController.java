package controller;

import model.Evento;
import model.EventoService;
import view.FrmConsultarEvento;
import java.time.format.DateTimeFormatter;

public class ConsultaEventoController {

    private final FrmConsultarEvento vistaConsulta;
    private final EventoService servicio;

    public ConsultaEventoController(FrmConsultarEvento vistaConsulta, EventoService servicio) {
        this.vistaConsulta = vistaConsulta;
        this.servicio = servicio;

        // Listener existente para el botón buscar
        this.vistaConsulta.btnBuscar.addActionListener(e -> buscarEvento());
        
        // NUEVO: Listener para el botón volver
        // (Asegúrate de que en NetBeans el botón se llame "btnVolver" y sea "public")
        this.vistaConsulta.btnVolver.addActionListener(e -> volver());
    }

    public void iniciar() {
        vistaConsulta.setTitle("Consultar Evento");
        vistaConsulta.setLocationRelativeTo(null);
        vistaConsulta.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }

    private void buscarEvento() {
        try {
            String idBusqueda = vistaConsulta.getIdConsulta();
            Evento evento = servicio.consultarEvento(idBusqueda);

            // Poblar la vista con los detalles del evento consultado
            vistaConsulta.setResultadoId(String.valueOf(evento.getIdEvento()));
            vistaConsulta.setResultadoNombre(evento.getNombre());
            vistaConsulta.setResultadoTipo(evento.getTipo());
            
            // Formatear la fecha y hora de forma correcta
            if (evento.getFecha() != null) {
                vistaConsulta.setResultadoFecha(evento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                vistaConsulta.setResultadoFecha("");
            }

            if (evento.getHora() != null) {
                vistaConsulta.setResultadoHora(evento.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                vistaConsulta.setResultadoHora("");
            }
            
            vistaConsulta.setResultadoLugar(evento.getLugar());
            vistaConsulta.setResultadoDescripcion(evento.getDescripcion());
            vistaConsulta.setResultadoEstado(evento.getEstado());

        } catch (IllegalArgumentException ex) {
            // Mostrar errores de validación (por ejemplo, no encontrado)
            vistaConsulta.mostrarMensajeAdvertencia(ex.getMessage());
            limpiarResultados();
        } catch (Exception ex) {
            vistaConsulta.mostrarMensajeError("Ocurrió un error inesperado: " + ex.getMessage());
            limpiarResultados();
        }
    }
    
    private void limpiarResultados() {
        vistaConsulta.setResultadoId("");
        vistaConsulta.setResultadoNombre("");
        vistaConsulta.setResultadoTipo("");
        vistaConsulta.setResultadoFecha("");
        vistaConsulta.setResultadoHora("");
        vistaConsulta.setResultadoLugar("");
        vistaConsulta.setResultadoDescripcion("");
        vistaConsulta.setResultadoEstado("");
    }
    
    // NUEVO: Método para cerrar la ventana y volver a la principal
    private void volver() {
        vistaConsulta.dispose();
    }
}