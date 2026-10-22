package model;

public class Recurso {
    private int id;
    private String tipo; // Ej: Proyector, Sillas, Catering
    private int cantidad;
    private String eventoAsignado;

    public Recurso(int id, String tipo, int cantidad, String eventoAsignado) {
        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.eventoAsignado = eventoAsignado;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getEventoAsignado() { return eventoAsignado; }
}
