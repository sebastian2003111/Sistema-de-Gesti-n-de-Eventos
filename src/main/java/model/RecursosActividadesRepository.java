package model;

import java.util.ArrayList;
import java.util.List;

public class RecursosActividadesRepository {
    private static RecursosActividadesRepository instance;
    private final List<Actividad> actividades;
    private final List<Recurso> recursos;
    private int nextActividadId = 1;
    private int nextRecursoId = 1;

    private RecursosActividadesRepository() {
        actividades = new ArrayList<>();
        recursos = new ArrayList<>();
    }

    public static RecursosActividadesRepository getInstance() {
        if (instance == null) {
            instance = new RecursosActividadesRepository();
        }
        return instance;
    }

    public void agregarActividad(Actividad a) { actividades.add(a); }
    public List<Actividad> obtenerActividades() { return new ArrayList<>(actividades); }
    public int getNextActividadId() { return nextActividadId++; }

    public void agregarRecurso(Recurso r) { recursos.add(r); }
    public List<Recurso> obtenerRecursos() { return new ArrayList<>(recursos); }
    public int getNextRecursoId() { return nextRecursoId++; }
}
