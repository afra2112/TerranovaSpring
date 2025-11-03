package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class ControllerVariablesGlobales {

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    UsuarioService usuarioService;

    //EL ATRIUBUTO principal es el usuario logueado almacenado en el Authentication de spring security
    @ModelAttribute("totalNoLeidas")
    public Integer agregarTotalNotificacionesNoLeidas(Principal principal){
        if (principal == null){
            return 0;
        }
        return notificacionService.contarNoLeidasPorUsuario(usuarioService.findByEmail(principal.getName()), false);
    }
}
