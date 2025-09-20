package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/enviar-correo")
    public String enviarCorreo(){
        emailService.sendEmail("afra65069@gmail.com", "Prueba correo", "PRUEBA DE CORREO cuerpo");
        return "correo enviado";
    }
}
