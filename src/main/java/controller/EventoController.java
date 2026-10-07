package controller;

import model.Evento;
import model.EventoService;
import view.FrmEvento;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import controller.ConsultaEventoController;
import view.FrmConsultarEvento;

public class EventoController {

    private final FrmEvento vista;
    private final EventoService servicio;
    private model.Usuario usuarioActual;

    public EventoController(FrmEvento vista, EventoService servicio) {
        this.vista = vista;
        this.servicio = servicio;

        // Binding events directly to methods using lambdas (OCP)
        this.vista.getBtnCrear().addActionListener(e -> crearEvento());
        this.vista.getBtnConsultar().addActionListener(e -> consultarEvento());
        this.vista.btnListar.addActionListener(e -> listarEventos());
    }

    public void setUsuarioActual(model.Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void iniciar() {
        vista.setTitle("Sistema de Gestión de Eventos");
        vista.setLocationRelativeTo(null);
        
        // Configuramos la fecha y hora actual por defecto en los nuevos DatePickers
        vista.setFecha(new java.util.Date());
        vista.setHora(new java.util.Date());
        
        vista.setIdGenerado(String.valueOf(servicio.obtenerSiguienteId()));
    }

    private void crearEvento() {
        if (usuarioActual == null) {
            vista.mostrarMensajeAdvertencia("Debes iniciar sesión para registrar eventos.");
            return;
        }
        try {
            // Capturar datos usando los getters de la vista
            String idTexto = vista.getIdEvento();
            String nombre = vista.getNombre();
            String descripcion = vista.getDescripcion();
            String tipo = vista.getTipo();
            String lugar = vista.getLugar();
            java.util.Date fecha = vista.getFecha();
            java.util.Date hora = vista.getHora();

            // Delegar la lógica de creación al servicio
            servicio.crearEvento(idTexto, nombre, descripcion, tipo, lugar, fecha, hora);

            // Crear notificación
            model.NotificacionRepository.getInstance().agregar(
                "Nuevo Evento", 
                "El evento '" + nombre + "' fue creado exitosamente."
            );

            // Mostrar mensaje de éxito a través de la vista
            vista.mostrarMensajeExito("¡Evento registrado con éxito!\n\n"
                    + "ID: " + idTexto + "\n"
                    + "Nombre: " + nombre + "\n"
                    + "Tipo: " + tipo);

            vista.limpiarFormulario();
            vista.setIdGenerado(String.valueOf(servicio.obtenerSiguienteId()));

        } catch (IllegalArgumentException ex) {
            // Mostrar mensajes de error de validación
            vista.mostrarMensajeAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarMensajeError("Ocurrió un error inesperado: " + ex.getMessage());
        }
    }

    private void consultarEvento() {
    FrmConsultarEvento vistaConsulta = new FrmConsultarEvento();

    ConsultaEventoController controladorConsulta =
            new ConsultaEventoController(vistaConsulta, servicio);
    controladorConsulta.setUsuarioActual(usuarioActual);

    controladorConsulta.iniciar();

    vistaConsulta.setVisible(true);
}

    private void listarEventos() {
        view.FrmListarEventos vistaListar = new view.FrmListarEventos();
        controller.ListarEventosController controladorListar = new controller.ListarEventosController(vistaListar, servicio);
        controladorListar.iniciar();
    }
}