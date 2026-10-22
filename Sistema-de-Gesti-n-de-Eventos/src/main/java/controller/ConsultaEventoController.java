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
        
        // Listener para el botón actualizar
        this.vistaConsulta.getBtnActualizar().addActionListener(e -> actualizarEvento());
        
        // Listener para el botón eliminar
        this.vistaConsulta.getBtnEliminar().addActionListener(e -> eliminarEvento());
    }

    private model.Usuario usuarioActual;

    public void setUsuarioActual(model.Usuario usuario) {
        this.usuarioActual = usuario;
        
        // A petición del cliente, los usuarios comunes ahora pueden actualizar y eliminar eventos.
        // Los permisos especiales de administrador se reservarán para otros módulos (ej. Configuración, Informes).
        this.vistaConsulta.getBtnEliminar().setEnabled(true);
        this.vistaConsulta.getBtnActualizar().setEnabled(true);
        
        // SOLO el Administrador puede cambiar el ESTADO del evento
        if (usuario != null && usuario.getRol().equals("Administrador")) {
            this.vistaConsulta.getCbxResultadoEstado().setEnabled(true);
        } else {
            this.vistaConsulta.getCbxResultadoEstado().setEnabled(false);
        }
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
        vistaConsulta.setResultadoId("---");
        vistaConsulta.setResultadoNombre("");
        vistaConsulta.setResultadoTipo("Seleccione...");
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
    
    private void actualizarEvento() {
        if (usuarioActual == null) {
            vistaConsulta.mostrarMensajeAdvertencia("Debes iniciar sesión para actualizar eventos.");
            return;
        }
        try {
            String idTexto = vistaConsulta.getResultadoId();
            if (idTexto == null || idTexto.trim().isEmpty() || idTexto.equals("---")) {
                vistaConsulta.mostrarMensajeAdvertencia("Primero debe consultar un evento válido para actualizar.");
                return;
            }
            
            String nombre = vistaConsulta.getResultadoNombre();
            String descripcion = vistaConsulta.getResultadoDescripcion();
            String tipo = vistaConsulta.getResultadoTipo();
            String lugar = vistaConsulta.getResultadoLugar();
            
            String fechaStr = vistaConsulta.getResultadoFecha();
            String horaStr = vistaConsulta.getResultadoHora();
            
            // Parsear las fechas ingresadas en texto
            java.util.Date dateFecha = null;
            java.util.Date dateHora = null;
            
            try {
                if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                    dateFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
                }
                if (horaStr != null && !horaStr.trim().isEmpty()) {
                    dateHora = new java.text.SimpleDateFormat("HH:mm").parse(horaStr);
                }
            } catch (java.text.ParseException e) {
                vistaConsulta.mostrarMensajeAdvertencia("Formato de fecha u hora incorrecto. Use dd/MM/yyyy y HH:mm");
                return;
            }

            // Consultamos el evento actual
            Evento eventoExistente = servicio.consultarEvento(idTexto);
            String estadoNuevo = vistaConsulta.getResultadoEstado();
            
            // Confirmación antes de actualizar
            int confirm = javax.swing.JOptionPane.showConfirmDialog(vistaConsulta,
                    "¿Estás seguro de que deseas guardar los cambios realizados en el evento '" + nombre + "'?",
                    "Confirmar Actualización",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE);
                    
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                servicio.actualizarEvento(idTexto, nombre, descripcion, tipo, lugar, dateFecha, dateHora, estadoNuevo);
                javax.swing.JOptionPane.showMessageDialog(vistaConsulta, "¡Datos actualizados con éxito!");
                
                // NUEVO: Notificar si el estado cambió
                if (!eventoExistente.getEstado().equals(estadoNuevo)) {
                     model.NotificacionRepository.getInstance().agregar(
                        "Estado Actualizado", 
                        "El Administrador ha cambiado el estado del evento '" + nombre + "' a: " + estadoNuevo
                    );
                }
            }
            
        } catch (IllegalArgumentException ex) {
            vistaConsulta.mostrarMensajeAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            vistaConsulta.mostrarMensajeError("Ocurrió un error al intentar actualizar: " + ex.getMessage());
        }
    }

    private void eliminarEvento() {
        if (usuarioActual == null) {
            vistaConsulta.mostrarMensajeAdvertencia("Debes iniciar sesión para eliminar eventos.");
            return;
        }
        try {
            String idTexto = vistaConsulta.getResultadoId();
            if (idTexto == null || idTexto.trim().isEmpty() || idTexto.equals("---")) {
                vistaConsulta.mostrarMensajeAdvertencia("Primero debe consultar un evento válido para eliminar.");
                return;
            }

            String nombre = vistaConsulta.getResultadoNombre(); // Capturamos el nombre para el mensaje
            
            int confirm = javax.swing.JOptionPane.showConfirmDialog(vistaConsulta,
                    "¿Estás seguro de que deseas eliminar permanentemente el evento '" + nombre + "'?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                servicio.eliminarEvento(idTexto);
                javax.swing.JOptionPane.showMessageDialog(vistaConsulta, "¡Evento eliminado con éxito!");
                limpiarResultados();
            }

        } catch (IllegalArgumentException ex) {
            vistaConsulta.mostrarMensajeAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            vistaConsulta.mostrarMensajeError("Ocurrió un error al intentar eliminar: " + ex.getMessage());
        }
    }
}