
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita){
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad, Authentication authentication){
        Cita cita = citaService.findById(idCita);
        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
        cita.setDisponibilidad(disponibilidad);
        citaService.save(cita);

        String titulo = "Actualizacion en tu cita. Reprogramacion.";
        String mensaje = "Tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Ha sido reprogramada por el vendedor para la nueva fecha: " + cita.getDisponibilidad().getFecha() + ". Y hora: " + cita.getDisponibilidad().getHora() + ".";

        if(notificacionService.validarSiEnviarNotificacionONo(usuario(authentication), "Citas")){
            String mensajeVendedor = "Has reprogramado tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Para la nueva fecha: " + cita.getDisponibilidad().getFecha() + ". Y hora: " + cita.getDisponibilidad().getHora() + ".";
            notificacionService.crearNotificacionAutomatica(titulo, mensajeVendedor, "Citas", usuario(authentication), idCita, "Ninguna");
        }

        if(notificacionService.validarSiEnviarNotificacionONo(cita.getComprador(), "Citas")){
            notificacionService.crearNotificacionAutomatica(titulo, mensaje, "Citas", cita.getComprador(), idCita, "Ninguna");
        }
        return "redirect:/vendedor/citas";
    }
}