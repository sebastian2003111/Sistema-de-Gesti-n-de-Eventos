package model;

public class Participante {
    private int id;
    private String nombre;
    private String correo;
    private String eventoAsignado; // Guardamos el nombre del evento por simplicidad

    public Participante(int id, String nombre, String correo, String eventoAsignado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.eventoAsignado = eventoAsignado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getEventoAsignado() { return eventoAsignado; }
}
