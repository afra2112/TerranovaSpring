package com.proyecto.terranova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String para, String asunto, String cuerpo){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(para);
        message.setSubject(asunto);
        message.setText(cuerpo);
        message.setFrom("terranova.avisos@gmail.com");

        javaMailSender.send(message);
    }
}
