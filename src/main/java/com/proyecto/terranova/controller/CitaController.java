
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    @Autowired
    ProductoService productoService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @PostMapping("/mi-calendario/registrar-cita")
    public String registrarCita(
            @RequestParam(name = "fecha") LocalDate fecha,
            @RequestParam(name = "horaInicio") LocalTime horaInicio,
            @RequestParam(name = "horaFin") LocalTime horaFin,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe,
            @RequestParam(name = "cupoMaximo") int cupoMaximo,
            Authentication authentication
    ) {
        Cita cita = new Cita();
        cita.setFecha(fecha);
        cita.setHoraFin(horaFin);
        cita.setHoraInicio(horaInicio);
        cita.setEstadoCita(EstadoCitaEnum.PROGRAMADA);
        cita.setProducto(productoService.findById(idProducto));
        cita.setCupoMaximo(cupoMaximo);
        cita.setDescripcion(descripcion);

        citaService.save(cita);

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/eliminar-cita")
    public String eliminarDisponibilidad(
            @RequestParam(name = "idDisponibilidad") Long idCita,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe
    ){
        citaService.delete(idCita);

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
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
    public String reprogramarCita(
            @RequestParam(name = "idCita") Long idCita,
            @RequestParam LocalDate fecha,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFin,
            Authentication authentication
            ) throws MessagingException, IOException {
        Cita cita = citaService.findById(idCita);
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);
        cita.setFecha(fecha);
        citaService.save(cita);

        notificacionService.notificacionCitaReprogramada(cita, usuario(authentication));

        return "redirect:/vendedor/citas";
    }
}