
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
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
@RequestMapping("/vendedor")
public class DisponibilidadController {

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    ProductoService productoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    NotificacionService notificacionService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @PostMapping("/mi-calendario/registrar-disponibilidad")
    public String registrarDisponibilidad(
            @RequestParam(name = "fecha") LocalDate fecha,
            @RequestParam(name = "horaInicio") LocalTime horaInicio,
            @RequestParam(name = "horaFin") LocalTime horaFin,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe,
            @RequestParam(name = "cupoMaximo") int cupoMaximo,
            Authentication authentication
    ) throws MessagingException, IOException {
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(fecha);
        disponibilidad.setHoraInicio(horaInicio);
        disponibilidad.setHoraFin(horaFin);
        disponibilidad.setDescripcion(descripcion);

        Cita cita = new Cita();
        cita.setDisponibilidad(disponibilidadService.save(disponibilidad));
        cita.setEstadoCita(EstadoCitaEnum.PROGRAMADA);
        cita.setProducto(productoService.findById(idProducto));
        cita.setCupoMaximo(cupoMaximo);

        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", new Locale("es", "ES"));
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("h:mm a", new Locale("es", "ES"));

        notificacionService.notificacionDisponibilidadRegistrada(disponibilidad, fechaFormatter.format(disponibilidad.getFecha()), horaFormatter.format(disponibilidad.getHoraInicio()));

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/eliminar-disponibilidad")
    public String eliminarDisponibilidad(
            @RequestParam(name = "idDisponibilidad") Long idDisponibilidad,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe,
            Authentication authentication
    ){
        disponibilidadService.delete(idDisponibilidad);

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }
}