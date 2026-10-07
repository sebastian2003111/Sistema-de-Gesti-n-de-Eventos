package model;

import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements IUsuarioRepository {
    private static UsuarioRepository instance;

    private UsuarioRepository() {
        // Constructor privado para Singleton
        crearUsuariosQuemadosSiNoExisten();
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    private void crearUsuariosQuemadosSiNoExisten() {
        String sqlCheck = "SELECT COUNT(*) FROM usuarios";
        String sqlInsert = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // Insertar admin
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin@admin.com");
                    pstmt.setString(3, "admin123");
                    pstmt.setString(4, "Administrador");
                    pstmt.executeUpdate();
                    
                    pstmt.setString(1, "operador");
                    pstmt.setString(2, "operador@gmail.com");
                    pstmt.setString(3, "operador123");
                    pstmt.setString(4, "Operador");
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar/insertar usuarios quemados: " + e.getMessage());
        }
    }

    @Override
    public Usuario autenticar(String correo, String password) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("contrasena"),
                            rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getPassword());
            pstmt.setString(4, usuario.getRol());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean existeUsuario(String username) {
        // En tu logica antigua verificaba por nombre (username), a pesar de que el ID es correo
        String sql = "SELECT 1 FROM usuarios WHERE nombre = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar si usuario existe: " + e.getMessage());
        }
        return false;
    }
}
