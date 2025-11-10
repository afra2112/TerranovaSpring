package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    public String generarHtmlParaCorreo(String nombreUsuario, String nombreTipoNotificacion, String linkAccion, String nombreUsuarioContrario, String nombreTemplateHtml) throws IOException {
        Context context = new Context();
        String css = Files.readString(Paths.get("src/main/resources/templates/correos/email.css"));
        context.setVariable("css", css);
        context.setVariable("nombreUsuario", nombreUsuario);
        context.setVariable("nombreTipoNotificacion", nombreTipoNotificacion);
        context.setVariable("nombreUsuarioContrario", nombreUsuarioContrario);
        context.setVariable("linkAccion", linkAccion);

        return templateEngine.process("correos/"+nombreTemplateHtml,context);
    }

    @Async  
    public void enviarEmailConHtml(boolean enviar, String email, String asunto, String mensajeHtml) throws MessagingException {

        if(enviar){
            MimeMessage mensaje = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(asunto);
            helper.setText(mensajeHtml, true);
            helper.setFrom("terranova.avisos@gmail.com");

            javaMailSender.send(mensaje);
        }
    }
}
