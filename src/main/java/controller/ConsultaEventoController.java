package controller;

import model.Evento;
import model.EventoService;
import view.FrmConsultarEvento;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class ConsultaEventoController {

    private final FrmConsultarEvento vistaConsulta;
    private final EventoService servicio;

    public ConsultaEventoController(FrmConsultarEvento vistaConsulta, EventoService servicio) {
        this.vistaConsulta = vistaConsulta;
        this.servicio = servicio;

        // Listeners existentes
        this.vistaConsulta.btnBuscar.addActionListener(e -> buscarEvento());
        this.vistaConsulta.btnVolver.addActionListener(e -> volver());
        
        // Listener para actualizar
        this.vistaConsulta.btnActualizar.addActionListener(e -> actualizarEvento());
    }

    public void iniciar() {
        vistaConsulta.setTitle("Consultar Evento");
        vistaConsulta.setLocationRelativeTo(null);
        vistaConsulta.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        
        // Configurar el modelo interno de los Spinners para que sean tipo Fecha/Hora
        vistaConsulta.spnResultadoFecha.setModel(new SpinnerDateModel());
        vistaConsulta.spnResultadoHora.setModel(new SpinnerDateModel());

        // Darle formato visual al Spinner de Fecha (Día/Mes/Año)
        JSpinner.DateEditor editorFecha = new JSpinner.DateEditor(vistaConsulta.spnResultadoFecha, "dd/MM/yyyy");
        vistaConsulta.spnResultadoFecha.setEditor(editorFecha);

        // Darle formato visual al Spinner de Hora (Formato 24 horas)
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(vistaConsulta.spnResultadoHora, "HH:mm");
        vistaConsulta.spnResultadoHora.setEditor(editorHora);
    }

    private void buscarEvento() {
        try {
            String idBusqueda = vistaConsulta.getIdConsulta();
            Evento evento = servicio.consultarEvento(idBusqueda);

            // Poblar la vista con los detalles del evento consultado
            vistaConsulta.setResultadoId(String.valueOf(evento.getIdEvento()));
            vistaConsulta.setResultadoNombre(evento.getNombre());
            vistaConsulta.setResultadoTipo(evento.getTipo());
            
            if (evento.getFecha() != null) {
                Date dateFecha = Date.from(evento.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant());
                vistaConsulta.setResultadoFecha(dateFecha);
            }

            if (evento.getHora() != null) {
                Date dateHora = Date.from(evento.getHora().atDate(evento.getFecha()).atZone(ZoneId.systemDefault()).toInstant());
                vistaConsulta.setResultadoHora(dateHora);
            }
            
            vistaConsulta.setResultadoLugar(evento.getLugar());
            vistaConsulta.setResultadoDescripcion(evento.getDescripcion());
            vistaConsulta.setResultadoEstado(evento.getEstado());

        } catch (IllegalArgumentException ex) {
            vistaConsulta.mostrarMensajeAdvertencia(ex.getMessage());
            limpiarResultados();
        } catch (Exception ex) {
            vistaConsulta.mostrarMensajeError("Ocurrió un error inesperado: " + ex.getMessage());
            limpiarResultados();
        }
    }
    
    private void actualizarEvento() {
        try {
            String idTexto = vistaConsulta.getIdConsulta();
            if (idTexto == null || idTexto.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe buscar un evento primero.");
            }
            
            // Confirmación antes de actualizar
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                    vistaConsulta, 
                    "¿Está seguro de que desea actualizar los datos de este evento?", 
                    "Confirmar Actualización", 
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
                return; // Acción cancelada por el usuario
            }
            
            String nombre = vistaConsulta.getResultadoNombre();
            String descripcion = vistaConsulta.getResultadoDescripcion();
            String tipo = vistaConsulta.getResultadoTipo();
            String lugar = vistaConsulta.getResultadoLugar();
            Date fecha = vistaConsulta.getResultadoFecha();
            Date hora = vistaConsulta.getResultadoHora();

            servicio.actualizarEvento(idTexto, nombre, descripcion, tipo, lugar, fecha, hora);

            vistaConsulta.mostrarMensajeAdvertencia("¡Evento actualizado con éxito!");

        } catch (IllegalArgumentException ex) {
            vistaConsulta.mostrarMensajeAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            vistaConsulta.mostrarMensajeError("Ocurrió un error inesperado: " + ex.getMessage());
        }
    }
    
    private void limpiarResultados() {
        vistaConsulta.setResultadoId("");
        vistaConsulta.setResultadoNombre("");
        vistaConsulta.setResultadoTipo("Seleccione...");
        vistaConsulta.setResultadoFecha(new Date());
        vistaConsulta.setResultadoHora(new Date());
        vistaConsulta.setResultadoLugar("");
        vistaConsulta.setResultadoDescripcion("");
        vistaConsulta.setResultadoEstado("");
    }
    
    private void volver() {
        vistaConsulta.dispose();
    }
}