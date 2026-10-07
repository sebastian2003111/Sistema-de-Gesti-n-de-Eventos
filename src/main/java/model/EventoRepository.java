package model;

import config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoRepository implements IEventoRepository {

    private static EventoRepository instance;

    private EventoRepository() {
        // Constructor privado para Singleton
    }

    public static EventoRepository getInstance() {
        if (instance == null) {
            instance = new EventoRepository();
        }
        return instance;
    }

    @Override
    public void guardar(Evento evento) {
        String sql = "INSERT INTO eventos (id, nombre, descripcion, tipo, lugar, fecha, hora, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.valueOf(evento.getIdEvento()));
            pstmt.setString(2, evento.getNombre());
            pstmt.setString(3, evento.getDescripcion());
            pstmt.setString(4, evento.getTipo());
            pstmt.setString(5, evento.getLugar());
            pstmt.setDate(6, evento.getFecha() != null ? Date.valueOf(evento.getFecha()) : null);
            pstmt.setTime(7, evento.getHora() != null ? Time.valueOf(evento.getHora()) : null);
            pstmt.setString(8, evento.getEstado() != null ? evento.getEstado() : "Programado");
            
            pstmt.executeUpdate();
            System.out.println("Evento guardado en la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al guardar evento: " + e.getMessage());
        }
    }

    @Override
    public List<Evento> obtenerTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener eventos: " + e.getMessage());
        }
        return eventos;
    }

    @Override
    public Evento buscarPorId(int id) {
        String sql = "SELECT * FROM eventos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.valueOf(id));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEvento(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar evento por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(Evento evento) {
        String sql = "UPDATE eventos SET nombre = ?, descripcion = ?, tipo = ?, lugar = ?, fecha = ?, hora = ?, estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, evento.getNombre());
            pstmt.setString(2, evento.getDescripcion());
            pstmt.setString(3, evento.getTipo());
            pstmt.setString(4, evento.getLugar());
            pstmt.setDate(5, evento.getFecha() != null ? Date.valueOf(evento.getFecha()) : null);
            pstmt.setTime(6, evento.getHora() != null ? Time.valueOf(evento.getHora()) : null);
            pstmt.setString(7, evento.getEstado());
            pstmt.setString(8, String.valueOf(evento.getIdEvento()));
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM eventos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.valueOf(id));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
        }
    }

    @Override
    public int obtenerSiguienteId() {
        String sql = "SELECT MAX(CAST(id AS INTEGER)) FROM eventos";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente ID: " + e.getMessage());
        }
        return 1;
    }

    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(Integer.parseInt(rs.getString("id")));
        evento.setNombre(rs.getString("nombre"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setTipo(rs.getString("tipo"));
        evento.setLugar(rs.getString("lugar"));
        
        Date fechaDate = rs.getDate("fecha");
        if (fechaDate != null) evento.setFecha(fechaDate.toLocalDate());
        
        Time horaTime = rs.getTime("hora");
        if (horaTime != null) evento.setHora(horaTime.toLocalTime());
        
        evento.setEstado(rs.getString("estado"));
        return evento;
    }
}