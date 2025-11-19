package com.proyecto.terranova.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    public String generarHtmlParaCorreo(String nombreUsuario, String nombreTipoNotificacion, String linkAccion, String nombreUsuarioContrario, String nombreTemplateHtml) throws IOException {
        Context context = new Context();
        String css = new String(
                getClass().getClassLoader().getResourceAsStream("templates/correos/email.css").readAllBytes()
        );
        context.setVariable("css", css);
        context.setVariable("nombreUsuario", nombreUsuario);
        context.setVariable("nombreTipoNotificacion", nombreTipoNotificacion);
        context.setVariable("nombreUsuarioContrario", nombreUsuarioContrario);
        context.setVariable("linkAccion", linkAccion);

        return templateEngine.process("correos/"+nombreTemplateHtml,context);
    }

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Async
    public void enviarEmailConHtml(boolean enviar, String email, String asunto, String mensajeHtml) throws IOException {
        if(enviar){
            Email from = new Email("terranova.avisos@gmail.com");
            Email to = new Email(email);
            Content content = new Content("text/html", mensajeHtml);
            Mail mail = new Mail(from, asunto, to, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("Estado: " + response.getStatusCode());
            System.out.println("Cuerpo: " + response.getBody());
            System.out.println("Cabeceras: " + response.getHeaders());

        }
    }
}
