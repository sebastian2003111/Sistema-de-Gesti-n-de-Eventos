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

    public EventoController(FrmEvento vista, EventoService servicio) {
        this.vista = vista;
        this.servicio = servicio;

        // Binding events directly to methods using lambdas (OCP)
        this.vista.getBtnCrear().addActionListener(e -> crearEvento());
        this.vista.getBtnConsultar().addActionListener(e -> consultarEvento());
    }

    public void iniciar() {
        vista.setTitle("Sistema de Gestión de Eventos");
        vista.setLocationRelativeTo(null);

        // 1. Configurar el modelo interno de los Spinners para que sean tipo Fecha/Hora
        vista.spnFecha.setModel(new SpinnerDateModel());
        vista.spnHora.setModel(new SpinnerDateModel());

        // 2. Darle formato visual al Spinner de Fecha (Día/Mes/Año)
        JSpinner.DateEditor editorFecha =
                new JSpinner.DateEditor(vista.spnFecha, "dd/MM/yyyy");
        vista.spnFecha.setEditor(editorFecha);

        // 3. Darle formato visual al Spinner de Hora (Formato 24 horas)
        JSpinner.DateEditor editorHora =
                new JSpinner.DateEditor(vista.spnHora, "HH:mm");
        vista.spnHora.setEditor(editorHora);
    }

    private void crearEvento() {
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

            // Mostrar mensaje de éxito a través de la vista
            vista.mostrarMensajeExito("¡Evento registrado con éxito!\n\n"
                    + "ID: " + idTexto + "\n"
                    + "Nombre: " + nombre + "\n"
                    + "Tipo: " + tipo);

            vista.limpiarFormulario();

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

    controladorConsulta.iniciar();

    vistaConsulta.setVisible(true);
}
}