package model;

import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursosActividadesRepository {
    private static RecursosActividadesRepository instance;

    private RecursosActividadesRepository() {
        // Constructor privado
    }

    public static RecursosActividadesRepository getInstance() {
        if (instance == null) {
            instance = new RecursosActividadesRepository();
        }
        return instance;
    }

    public void agregarActividad(Actividad a) {
        String sql = "INSERT INTO actividades (nombre, horario, responsable, evento_asignado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getHorario());
            pstmt.setString(3, a.getResponsable());
            pstmt.setString(4, a.getEventoAsignado());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar actividad: " + e.getMessage());
        }
    }

    public List<Actividad> obtenerActividades() {
        List<Actividad> lista = new ArrayList<>();
        String sql = "SELECT * FROM actividades";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new Actividad(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        rs.getString("responsable"),
                        rs.getString("evento_asignado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener actividades: " + e.getMessage());
        }
        return lista;
    }

    public int getNextActividadId() {
        String sql = "SELECT MAX(id) FROM actividades";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente ID de actividad: " + e.getMessage());
        }
        return 1;
    }

    public void agregarRecurso(Recurso r) {
        String sql = "INSERT INTO recursos (tipo, cantidad, evento_asignado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, r.getTipo());
            pstmt.setInt(2, r.getCantidad());
            pstmt.setString(3, r.getEventoAsignado());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar recurso: " + e.getMessage());
        }
    }

    public List<Recurso> obtenerRecursos() {
        List<Recurso> lista = new ArrayList<>();
        String sql = "SELECT * FROM recursos";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new Recurso(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getInt("cantidad"),
                        rs.getString("evento_asignado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener recursos: " + e.getMessage());
        }
        return lista;
    }

    public int getNextRecursoId() {
        String sql = "SELECT MAX(id) FROM recursos";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente ID de recurso: " + e.getMessage());
        }
        return 1;
    }
}
