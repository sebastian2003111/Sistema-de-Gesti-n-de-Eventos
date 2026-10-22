package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static Connection connection = null;

    private DatabaseConnection() {
        // Constructor privado para patrón Singleton
    }

    private static String obtenerUrlConexion() {
        try {
            // 1. Si el archivo ya existe en el directorio de trabajo local, usar su ruta absoluta
            java.io.File localDb = new java.io.File("gestion_eventos.db");
            if (localDb.exists()) {
                return "jdbc:sqlite:" + localDb.getAbsolutePath();
            }

            // 2. Si se ejecuta desde el JAR empaquetado, garantizar que la BD se guarde junto al JAR
            java.io.File jarPath = new java.io.File(DatabaseConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            java.io.File appDir = jarPath.isDirectory() ? jarPath : jarPath.getParentFile();
            if (appDir != null && appDir.exists()) {
                java.io.File targetDb = new java.io.File(appDir, "gestion_eventos.db");
                return "jdbc:sqlite:" + targetDb.getAbsolutePath();
            }
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo resolver la ruta absoluta del JAR, usando ruta por defecto.");
        }
        return "jdbc:sqlite:gestion_eventos.db";
    }

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                // Registrar el driver
                Class.forName("org.sqlite.JDBC");
                // Establecer la conexión con ruta protegida
                connection = DriverManager.getConnection(obtenerUrlConexion());
                
                // Inicializar las tablas si no existen
                inicializarTablas(connection);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de SQLite no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos SQLite. " + e.getMessage());
        }
        return connection;
    }

    private static void inicializarTablas(Connection conn) {
        String sqlEventos = "CREATE TABLE IF NOT EXISTS eventos (" +
                            "id VARCHAR(50) PRIMARY KEY, " +
                            "nombre VARCHAR(100) NOT NULL, " +
                            "descripcion TEXT, " +
                            "tipo VARCHAR(50), " +
                            "lugar VARCHAR(100), " +
                            "fecha DATE, " +
                            "hora TIME, " +
                            "estado VARCHAR(50)" +
                            ")";

        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                             "correo VARCHAR(100) PRIMARY KEY, " +
                             "nombre VARCHAR(100) NOT NULL, " +
                             "contrasena VARCHAR(100) NOT NULL, " +
                             "rol VARCHAR(50) NOT NULL" +
                             ")";

        String sqlParticipantes = "CREATE TABLE IF NOT EXISTS participantes (" +
                                  "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                  "nombre VARCHAR(100) NOT NULL, " +
                                  "correo VARCHAR(100) NOT NULL, " +
                                  "evento_asignado VARCHAR(100)" +
                                  ")";

        String sqlActividades = "CREATE TABLE IF NOT EXISTS actividades (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "nombre VARCHAR(100) NOT NULL, " +
                                "horario VARCHAR(100), " +
                                "responsable VARCHAR(100), " +
                                "evento_asignado VARCHAR(100)" +
                                ")";

        String sqlRecursos = "CREATE TABLE IF NOT EXISTS recursos (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "tipo VARCHAR(50) NOT NULL, " +
                             "cantidad INT, " +
                             "evento_asignado VARCHAR(100)" +
                             ")";

        String sqlNotificaciones = "CREATE TABLE IF NOT EXISTS notificaciones (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "titulo VARCHAR(100), " +
                                   "mensaje TEXT NOT NULL, " +
                                   "fechaHora VARCHAR(50), " +
                                   "leida BOOLEAN DEFAULT 0" +
                                   ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlEventos);
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlParticipantes);
            stmt.execute(sqlActividades);
            stmt.execute(sqlRecursos);
            stmt.execute(sqlNotificaciones);
            System.out.println("Tablas de la base de datos inicializadas/verificadas correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}
