package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notificacion {
    private int id;
    private String titulo;
    private String mensaje;
    private String fechaHora;
    private boolean leida;

    public Notificacion(int id, String titulo, String mensaje) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.leida = false;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public String getFechaHora() { return fechaHora; }
    public boolean isLeida() { return leida; }
    public void marcarComoLeida() { this.leida = true; }
}
