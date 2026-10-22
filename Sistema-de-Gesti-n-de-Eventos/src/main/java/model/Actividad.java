package model;

public class Actividad {
    private int id;
    private String nombre;
    private String horario;
    private String responsable;
    private String eventoAsignado;

    public Actividad(int id, String nombre, String horario, String responsable, String eventoAsignado) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.responsable = responsable;
        this.eventoAsignado = eventoAsignado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getHorario() { return horario; }
    public String getResponsable() { return responsable; }
    public String getEventoAsignado() { return eventoAsignado; }
}
