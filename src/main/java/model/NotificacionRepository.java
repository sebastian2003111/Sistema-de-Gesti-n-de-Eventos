package model;

import config.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificacionRepository {
    private static NotificacionRepository instance;

    private NotificacionRepository() {
        crearNotificacionBienvenidaSiNoExiste();
    }

    public static NotificacionRepository getInstance() {
        if (instance == null) {
            instance = new NotificacionRepository();
        }
        return instance;
    }

    private void crearNotificacionBienvenidaSiNoExiste() {
        String sqlCheck = "SELECT COUNT(*) FROM notificaciones";
        String sqlInsert = "INSERT INTO notificaciones (titulo, mensaje, fechaHora, leida) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                    pstmt.setString(1, "¡Bienvenido!");
                    pstmt.setString(2, "Bienvenido al Sistema de Gestión de Eventos. Revisa el Dashboard para empezar.");
                    pstmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                    pstmt.setBoolean(4, false);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar notificacion inicial: " + e.getMessage());
        }
    }

    public void agregar(String titulo, String mensaje) {
        String sql = "INSERT INTO notificaciones (titulo, mensaje, fechaHora, leida) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, titulo);
            pstmt.setString(2, mensaje);
            pstmt.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            pstmt.setBoolean(4, false);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar notificacion: " + e.getMessage());
        }
    }

    public List<Notificacion> obtenerTodas() {
        List<Notificacion> lista = new ArrayList<>();
        // Ordenamos por ID descendente para tener las mas recientes primero
        String sql = "SELECT * FROM notificaciones ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Notificacion n = new Notificacion(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("mensaje")
                );
                n.setFechaHora(rs.getString("fechaHora"));
                if (rs.getBoolean("leida")) {
                    n.marcarComoLeida();
                }
                lista.add(n);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener notificaciones: " + e.getMessage());
        }
        return lista;
    }
    
    public void marcarTodasComoLeidas() {
        String sql = "UPDATE notificaciones SET leida = true";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error al marcar notificaciones como leidas: " + e.getMessage());
        }
    }
    
    public long contarNoLeidas() {
        String sql = "SELECT COUNT(*) FROM notificaciones WHERE leida = false";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar notificaciones no leidas: " + e.getMessage());
        }
        return 0;
    }
}
