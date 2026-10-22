package model;

import config.EmailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class EmailService {

    public static CompletableFuture<Boolean> enviarInvitacionAsync(Participante participante, Evento evento) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                EmailConfig config = EmailConfig.getInstance();

                String destinatario = participante.getCorreo();
                String nombreParticipante = participante.getNombre();
                String nombreEvento = evento != null ? evento.getNombre() : participante.getEventoAsignado();
                String fecha = (evento != null && evento.getFecha() != null) ? evento.getFecha().toString() : "Por definir";
                String hora = (evento != null && evento.getHora() != null) ? evento.getHora().toString() : "Por definir";
                String lugar = (evento != null && evento.getLugar() != null) ? evento.getLugar() : "Lugar asignado por el organizador";
                String tipo = (evento != null && evento.getTipo() != null) ? evento.getTipo() : "Evento General";
                String descripcion = (evento != null && evento.getDescripcion() != null && !evento.getDescripcion().isEmpty()) 
                        ? evento.getDescripcion() : "Nos complace invitarte a ser parte de esta gran experiencia.";

                String asunto = "🎟️ ¡Confirmación de Registro! Tu invitación para: " + nombreEvento;
                String contenidoHtml = generarPlantillaHtml(nombreParticipante, nombreEvento, fecha, hora, lugar, tipo, descripcion);

                // Si el servidor SMTP está configurado y habilitado, enviamos por red
                if (config.isHabilitado() && !config.getRemitente().isEmpty() && !config.getPassword().isEmpty()) {
                    enviarCorreoSmtp(config, destinatario, asunto, contenidoHtml);
                    
                    NotificacionRepository.getInstance().agregar(
                            "Invitación Enviada", 
                            "Se envió la confirmación por correo a " + nombreParticipante + " (" + destinatario + ") para el evento '" + nombreEvento + "'."
                    );
                    System.out.println("Correo real enviado exitosamente a: " + destinatario);
                } else {
                    // Modo simulación / informativo cuando aún no han configurado credenciales SMTP
                    NotificacionRepository.getInstance().agregar(
                            "Invitación Generada", 
                            "Se generó la confirmación de asistencia para " + nombreParticipante + " (" + destinatario + ") al evento '" + nombreEvento + "'."
                    );
                    System.out.println("[SIMULACIÓN SMTP] Invitación generada para " + destinatario + " (" + nombreEvento + "). Para envíos reales configure SMTP en Configuración.");
                }

                return true;
            } catch (Exception ex) {
                System.err.println("Aviso al procesar envío de correo: " + ex.getMessage());
                NotificacionRepository.getInstance().agregar(
                        "Aviso de Correo", 
                        "No se pudo completar el envío de correo a " + participante.getCorreo() + ": " + ex.getMessage()
                );
                return false;
            }
        });
    }

    private static void enviarCorreoSmtp(EmailConfig config, String destinatario, String asunto, String cuerpoHtml) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", String.valueOf(config.getPort()));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getRemitente(), config.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getRemitente(), "Sistema de Gestión de Eventos"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setContent(cuerpoHtml, "text/html; charset=utf-8");

        Transport.send(message);
    }

    private static String generarPlantillaHtml(String nombre, String evento, String fecha, String hora, String lugar, String tipo, String descripcion) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #121212; color: #FFFFFF; margin: 0; padding: 20px; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #1E1E1E; border-radius: 12px; overflow: hidden; border: 1px solid #333333; box-shadow: 0 4px 15px rgba(0,0,0,0.5); }"
                + ".header { background: linear-gradient(135deg, #FF751F 0%, #E65C00 100%); padding: 30px 20px; text-align: center; }"
                + ".header h1 { margin: 0; color: #FFFFFF; font-size: 24px; text-transform: uppercase; letter-spacing: 1px; }"
                + ".content { padding: 30px 25px; }"
                + ".greeting { font-size: 18px; color: #FFFFFF; margin-bottom: 20px; }"
                + ".card { background-color: #282828; border-left: 4px solid #FF751F; padding: 20px; border-radius: 8px; margin: 20px 0; }"
                + ".card-item { margin-bottom: 12px; font-size: 15px; color: #CCCCCC; }"
                + ".card-item strong { color: #FFFFFF; }"
                + ".badge { display: inline-block; background-color: #FF751F; color: #FFFFFF; padding: 4px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; margin-bottom: 10px; }"
                + ".footer { background-color: #181818; text-align: center; padding: 20px; font-size: 12px; color: #777777; border-top: 1px solid #2B2B2B; }"
                + ".note { font-size: 13px; color: #AAAAAA; font-style: italic; margin-top: 15px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "  <div class='header'>"
                + "    <h1>🎟️ Invitación Oficial al Evento</h1>"
                + "  </div>"
                + "  <div class='content'>"
                + "    <p class='greeting'>¡Hola, <strong>" + nombre + "</strong>!</p>"
                + "    <p style='color: #DDDDDD; line-height: 1.5;'>Nos complace confirmarte que has sido registrado exitosamente para participar en el siguiente evento:</p>"
                + "    <div class='card'>"
                + "      <div class='badge'>" + tipo.toUpperCase() + "</div>"
                + "      <h2 style='margin-top: 5px; color: #FF751F; font-size: 20px;'>" + evento + "</h2>"
                + "      <div class='card-item'><strong>📅 Fecha:</strong> " + fecha + "</div>"
                + "      <div class='card-item'><strong>⏰ Hora:</strong> " + hora + "</div>"
                + "      <div class='card-item'><strong>📍 Lugar:</strong> " + lugar + "</div>"
                + "      <div class='card-item' style='margin-top: 15px;'><strong>📝 Descripción:</strong> " + descripcion + "</div>"
                + "    </div>"
                + "    <p class='note'>💡 Recuerda presentar tu nombre o documento de identidad en el punto de registro a tu llegada.</p>"
                + "  </div>"
                + "  <div class='footer'>"
                + "    <p>Sistema de Gestión de Eventos • Plataforma Integral</p>"
                + "    <p>© 2026 Todos los derechos reservados.</p>"
                + "  </div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
