package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class EmailConfig {
    private static final String CONFIG_FILE = "email_config.properties";

    private String host = "smtp.gmail.com";
    private int port = 587;
    private String remitente = "";
    private String password = "";
    private boolean habilitado = false;

    private static EmailConfig instance;

    private EmailConfig() {
        cargar();
    }

    public static EmailConfig getInstance() {
        if (instance == null) {
            instance = new EmailConfig();
        }
        return instance;
    }

    public void cargar() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return;
        }

        try (FileInputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);

            this.host = props.getProperty("host", "smtp.gmail.com");
            this.port = Integer.parseInt(props.getProperty("port", "587"));
            this.remitente = props.getProperty("remitente", "");
            this.password = props.getProperty("password", "");
            this.habilitado = Boolean.parseBoolean(props.getProperty("habilitado", "false"));
        } catch (Exception e) {
            System.err.println("Aviso al cargar configuración de email: " + e.getMessage());
        }
    }

    public void guardar(String host, int port, String remitente, String password, boolean habilitado) {
        this.host = host != null ? host.trim() : "smtp.gmail.com";
        this.port = port;
        this.remitente = remitente != null ? remitente.trim() : "";
        this.password = password != null ? password.replace(" ", "").trim() : "";
        this.habilitado = habilitado;

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            Properties props = new Properties();
            props.setProperty("host", this.host);
            props.setProperty("port", String.valueOf(this.port));
            props.setProperty("remitente", this.remitente);
            props.setProperty("password", this.password);
            props.setProperty("habilitado", String.valueOf(this.habilitado));
            props.store(out, "Configuracion de Servidor SMTP para Notificaciones");
            System.out.println("Configuración SMTP guardada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al guardar configuración de email: " + e.getMessage());
        }
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getRemitente() { return remitente != null ? remitente.trim() : ""; }
    public String getPassword() { return password != null ? password.replace(" ", "").trim() : ""; }
    public boolean isHabilitado() { return habilitado; }
}
