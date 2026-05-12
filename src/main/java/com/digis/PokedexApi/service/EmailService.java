package com.digis.PokedexApi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void enviarEmailActivacion(String destinatario, String nombreUsuario, String token)
            throws MessagingException {

        String link = baseUrl + "/api/auth/activar?token=" + token;

        String cuerpo = """
            <html>
            <body style="font-family: Arial, sans-serif; background:#f4f4f4; padding:30px;">
              <div style="max-width:500px; margin:auto; background:white;
                          border-radius:10px; padding:30px; box-shadow:0 2px 8px rgba(0,0,0,0.1)">
                <h2 style="color:#2E9E6B;">¡Bienvenido a PokedexApi, %s! 🐾</h2>
                <p>Gracias por registrarte. Para activar tu cuenta haz clic en el botón:</p>
                <div style="text-align:center; margin:30px 0;">
                  <a href="%s"
                     style="background:#2E9E6B; color:white; padding:14px 28px;
                            border-radius:6px; text-decoration:none; font-weight:bold;
                            font-size:16px;">
                    ✅ Activar mi cuenta
                  </a>
                </div>
                <p style="color:#888; font-size:13px;">
                  Este enlace expira en <strong>24 horas</strong>.<br/>
                  Si no creaste esta cuenta, ignora este mensaje.
                </p>
              </div>
            </body>
            </html>
            """.formatted(nombreUsuario, link);

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setFrom(remitente);
        helper.setTo(destinatario);
        helper.setSubject("Activa tu cuenta de PokedexApi");
        helper.setText(cuerpo, true); // true = es HTML

        mailSender.send(mensaje);
    }

    public void enviarCorreoRecuperacion(String destinatario, String token) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Recuperación de contraseña — PokedexApi");

            String link = frontendUrl + "/reset-password?token=" + token;

            String html = """
                <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto;">
                    <h2 style="color: #E3350D;">Recuperación de contraseña</h2>
                    <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                    <p>Haz clic en el botón para continuar. El enlace expira en <strong>30 minutos</strong>.</p>
                    <a href="%s"
                       style="display:inline-block; background:#E3350D; color:#fff;
                              padding:12px 24px; border-radius:8px; text-decoration:none;
                              font-weight:bold;">
                        Restablecer contraseña
                    </a>
                    <p style="margin-top:20px; color:#888; font-size:12px;">
                        Si no solicitaste esto, ignora este correo. Tu cuenta está segura.
                    </p>
                </div>
                """.formatted(link);

            helper.setText(html, true); 
            mailSender.send(mensaje);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
