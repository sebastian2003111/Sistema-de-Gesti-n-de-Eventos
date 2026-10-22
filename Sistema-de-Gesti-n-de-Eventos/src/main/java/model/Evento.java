package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    
    // Atributos definidos
    private int idEvento;
    private String nombre;
    private String descripcion;
    private String tipo;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String estado;
    

    // Constructor vacío (útil para instanciar sin datos iniciales)
    public Evento() {
        this.estado = "Programado"; // Estado por defecto
    }

    // Constructor con todos los parámetros
    public Evento(int idEvento, String nombre, String descripcion, String tipo, LocalDate fecha, LocalTime hora, String lugar) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.estado = "Programado"; // Al crear un evento, nace como Programado
    }

    // --- Getters y Setters ---

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
    return fecha;
}

public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
}

    public LocalTime getHora() {
    return hora;
    }

    public void setHora(LocalTime hora) {
    this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}