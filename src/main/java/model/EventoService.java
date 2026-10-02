package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class EventoService {

    private final IEventoRepository repository;

    public EventoService(IEventoRepository repository) {
        this.repository = repository;
    }

    public int crearEvento(String nombre, String descripcion, String tipo, String lugar, Date dateFecha, Date dateHora) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (lugar == null || lugar.trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar es obligatorio.");
        }
        if (tipo == null || tipo.equals("Seleccione...")) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de evento.");
        }

        // --- Autogenerar ID secuencial ---
        int nuevoId = 1;
        for (Evento e : repository.obtenerTodos()) {
            if (e.getIdEvento() >= nuevoId) {
                nuevoId = e.getIdEvento() + 1;
            }
        }
        // ---------------------------------

        LocalDate fecha = dateFecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalTime hora = dateHora.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
                .withSecond(0)
                .withNano(0);

        Evento nuevoEvento = new Evento(nuevoId, nombre, descripcion.trim(), tipo, fecha, hora, lugar.trim());
        repository.guardar(nuevoEvento);
        
        return nuevoId;
    }

    public Evento consultarEvento(String idTexto) throws IllegalArgumentException {
        if (idTexto == null || idTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese el ID del evento que desea consultar.");
        }

        int id;
        try {
            id = Integer.parseInt(idTexto.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser un número entero.");
        }

        Evento evento = repository.buscarPorId(id);
        if (evento == null) {
            throw new IllegalArgumentException("No se encontró un evento con el ID: " + id);
        }

        return evento;
    }

    public void actualizarEvento(String idTexto, String nombre, String descripcion, String tipo, String lugar, Date dateFecha, Date dateHora) throws IllegalArgumentException {
        if (idTexto == null || idTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (lugar == null || lugar.trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar es obligatorio.");
        }
        if (tipo == null || tipo.equals("Seleccione...")) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de evento.");
        }

        int id;
        try {
            id = Integer.parseInt(idTexto.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser un número entero.");
        }

        Evento eventoExistente = repository.buscarPorId(id);
        if (eventoExistente == null) {
            throw new IllegalArgumentException("No se encontró un evento con el ID: " + id);
        }

        LocalDate fecha = dateFecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalTime hora = dateHora.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
                .withSecond(0)
                .withNano(0);

        Evento eventoActualizado = new Evento(id, nombre, descripcion.trim(), tipo, fecha, hora, lugar.trim());
        
        // Conservar el estado que tenía
        eventoActualizado.setEstado(eventoExistente.getEstado());
        
        repository.actualizar(eventoActualizado);
    }
}
