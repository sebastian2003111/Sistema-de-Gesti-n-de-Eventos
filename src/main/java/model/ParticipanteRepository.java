package model;

import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteRepository {
    private static ParticipanteRepository instance;

    private ParticipanteRepository() {
        // Constructor privado
    }

    public static ParticipanteRepository getInstance() {
        if (instance == null) {
            instance = new ParticipanteRepository();
        }
        return instance;
    }

    public void agregar(Participante p) {
        String sql = "INSERT INTO participantes (nombre, correo, evento_asignado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getCorreo());
            pstmt.setString(3, p.getEventoAsignado());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar participante: " + e.getMessage());
        }
    }

    public List<Participante> obtenerTodos() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM participantes";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new Participante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("evento_asignado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener participantes: " + e.getMessage());
        }
        return lista;
    }
    
    public int getNextId() {
        String sql = "SELECT MAX(id) FROM participantes";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente ID de participante: " + e.getMessage());
        }
        return 1;
    }
}
