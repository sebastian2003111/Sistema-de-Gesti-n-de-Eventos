package model;

import java.util.ArrayList;
import java.util.List;

public class EventoRepository implements IEventoRepository {

    private final List<Evento> eventos = new ArrayList<>();

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
}