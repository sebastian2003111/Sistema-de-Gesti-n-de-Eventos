package model;

import java.util.List;

public interface IEventoRepository {
    void guardar(Evento evento);
    List<Evento> obtenerTodos();
    Evento buscarPorId(int id);
    void actualizar(Evento evento);
    void eliminar(int id);
    int obtenerSiguienteId();
}
