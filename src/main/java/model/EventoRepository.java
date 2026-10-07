package model;

import java.util.ArrayList;
import java.util.List;

public class EventoRepository implements IEventoRepository {

    private static EventoRepository instance;
    private final List<Evento> eventos;

    private EventoRepository() {
        eventos = new ArrayList<>();
    }

    public static EventoRepository getInstance() {
        if (instance == null) {
            instance = new EventoRepository();
        }
        return instance;
    }

    public void guardar(Evento evento) {
        eventos.add(evento);
    }

    public List<Evento> obtenerTodos() {
        return new ArrayList<>(eventos);
    }

    public Evento buscarPorId(int id) {

        for (Evento evento : eventos) {
            if (evento.getIdEvento() == id) {
                return evento;
            }
        }

        return null;
    }

    @Override
    public void actualizar(Evento eventoActualizado) {
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getIdEvento() == eventoActualizado.getIdEvento()) {
                eventos.set(i, eventoActualizado);
                return;
            }
        }
    }

    @Override
    public void eliminar(int id) {
        eventos.removeIf(evento -> evento.getIdEvento() == id);
    }

    @Override
    public int obtenerSiguienteId() {
        int max = 0;
        for (Evento e : eventos) {
            if (e.getIdEvento() > max) {
                max = e.getIdEvento();
            }
        }
        return max + 1;
    }
}