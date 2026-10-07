package model;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteRepository {
    private static ParticipanteRepository instance;
    private final List<Participante> participantes;
    private int nextId = 1;

    private ParticipanteRepository() {
        participantes = new ArrayList<>();
        // Datos quemados para mostrar en la interfaz
        participantes.add(new Participante(nextId++, "Juan Pérez", "juan@gmail.com", "Conferencia Anual"));
        participantes.add(new Participante(nextId++, "Ana Gómez", "ana@hotmail.com", "Fiesta de Cumpleaños"));
    }

    public static ParticipanteRepository getInstance() {
        if (instance == null) {
            instance = new ParticipanteRepository();
        }
        return instance;
    }

    public void agregar(Participante p) {
        participantes.add(p);
    }

    public List<Participante> obtenerTodos() {
        return new ArrayList<>(participantes);
    }
    
    public int getNextId() {
        return nextId++;
    }
}
