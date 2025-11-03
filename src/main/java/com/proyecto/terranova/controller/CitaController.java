
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Notificacion;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.UsuarioService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/vendedor/citas")
public class CitaController {

    @Autowired
    CitaService citaService;

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    UsuarioService usuarioService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @PostMapping("/cancelar-cita")
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);

        notificacionService.notificacionCitaCancelada(cita, usuario);

        return "redirect:/vendedor/citas";
    }

    @PostMapping("/finalizar-cita")
    public String finalizarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
        citaService.save(cita);

        notificacionService.notificacionCitaFinalizada(cita);

        return "redirect:/vendedor/citas";
    }

    @PostMapping("/borrar-cita")
    public String borrarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication){
        Usuario usuario = usuario(authentication);
        citaService.borrarCita(idCita);

        return "redirect:/vendedor/citas";
    }

    @PostMapping("/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad, Authentication authentication) throws MessagingException, IOException {
        Cita cita = citaService.findById(idCita);
        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
        cita.setDisponibilidad(disponibilidad);
        citaService.save(cita);

        notificacionService.notificacionCitaReprogramada(cita, usuario(authentication));

        return "redirect:/vendedor/citas";
    }
}