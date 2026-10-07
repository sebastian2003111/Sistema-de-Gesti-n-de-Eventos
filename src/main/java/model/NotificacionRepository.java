package model;

import java.util.ArrayList;
import java.util.List;

public class NotificacionRepository {
    private static NotificacionRepository instance;
    private final List<Notificacion> notificaciones;
    private int nextId = 1;

    private NotificacionRepository() {
        notificaciones = new ArrayList<>();
        // Notificación de bienvenida quemada
        notificaciones.add(new Notificacion(nextId++, "¡Bienvenido!", "Bienvenido al Sistema de Gestión de Eventos. Revisa el Dashboard para empezar."));
    }

    public static NotificacionRepository getInstance() {
        if (instance == null) {
            instance = new NotificacionRepository();
        }
        return instance;
    }

    public void agregar(String titulo, String mensaje) {
        notificaciones.add(0, new Notificacion(nextId++, titulo, mensaje)); // Agregar al inicio (más reciente)
    }

    public List<Notificacion> obtenerTodas() {
        return new ArrayList<>(notificaciones);
    }
    
    public void marcarTodasComoLeidas() {
        for (Notificacion n : notificaciones) {
            n.marcarComoLeida();
        }
    }
    
    public long contarNoLeidas() {
        return notificaciones.stream().filter(n -> !n.isLeida()).count();
    }
}
