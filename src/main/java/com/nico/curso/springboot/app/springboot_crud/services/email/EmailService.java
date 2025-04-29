package com.nico.curso.springboot.app.springboot_crud.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nico.curso.springboot.app.springboot_crud.constants.AppConstants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${front.url}")
    private String URL_FRONT;

    public void sendHtmlEmail(String to, String name, String token, String subject, String cuerpo, String url)
            throws MessagingException {

        String urlFull = URL_FRONT + url;
        String htmlContent = "";

        htmlContent = String.format(
                """
                            <html>
                            <body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;'>
                                <div style='max-width: 500px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); padding: 24px;'>
                                    <h2 style='color: #2e6da4;'>%s</h2>
                                    <p>%s</p>
                                    <a href='%s'>Confirmar codigo</a>
                                    <p>Ingresa el siguiente codigo: %s</p>
                                    <p>Este codigo expira en %s minutos.</p>
                                    <p style='font-size: 13px; color: #888;'>Este es un mensaje automático, por favor no respondas a este correo.</p>
                                </div>
                            </body>
                            </html>
                        """,
                name, cuerpo, urlFull, token, AppConstants.TOKEN_EXPIRATION_MINUTES);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("jorgenicomartin854@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true para HTML
        mailSender.send(message);
    }
}