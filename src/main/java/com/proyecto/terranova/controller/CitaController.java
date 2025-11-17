
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

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
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, horaInicio);
        LocalDateTime fechaHoraFin = LocalDateTime.of(fecha, horaFin);

        // Validar que no sea en el pasado
        if (fechaHoraInicio.isBefore(ahora)) {
            redirectAttributes.addFlashAttribute("error", "No puedes crear una cita en el pasado");
            if (vieneDe != null && vieneDe.equals("calendario")) {
                return "redirect:/vendedor/mi-calendario";
            }
            return "redirect:/vendedor/productos?idProducto=" + idProducto;
        }

        // Validar que hora fin sea después de hora inicio
        if (!horaFin.isAfter(horaInicio)) {
            redirectAttributes.addFlashAttribute("error", "La hora de fin debe ser posterior a la hora de inicio");
            if (vieneDe != null && vieneDe.equals("calendario")) {
                return "redirect:/vendedor/mi-calendario";
            }
            return "redirect:/vendedor/productos?idProducto=" + idProducto;
        }

        // Validar duración mínima
        long duracionMinutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
        if (duracionMinutos < 30) {
            redirectAttributes.addFlashAttribute("error", "La cita debe durar al menos 30 minutos");
            if (vieneDe != null && vieneDe.equals("calendario")) {
                return "redirect:/vendedor/mi-calendario";
            }
            return "redirect:/vendedor/productos?idProducto=" + idProducto;
        }

        //CREAR CITA
        Cita cita = new Cita();
        cita.setFecha(fecha);
        cita.setHoraFin(horaFin);
        cita.setHoraInicio(horaInicio);
        cita.setEstadoCita(EstadoCitaEnum.PROGRAMADA);
        cita.setProducto(productoService.findById(idProducto));
        cita.setCupoMaximo(cupoMaximo);
        cita.setDescripcion(descripcion);

        //Inicializar asistencias si es null
        if (cita.getAsistencias() == null) {
            cita.setOcupados(0);
        } else {
            cita.setOcupados(cita.getAsistencias().size());
        }
        cita.setDisponibles(cita.getCupoMaximo() - cita.getOcupados());

        citaService.save(cita);

        redirectAttributes.addFlashAttribute("success", "✅ Cita creada exitosamente");

        if (vieneDe != null && vieneDe.equals("calendario")) {
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @PostMapping("/borrar-cita")
    public String borrarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication){
        Usuario usuario = usuario(authentication);
        citaService.borrarCita(idCita);

        return "redirect:/vendedor/citas";
    }

    @PostMapping("/reprogramar-cita/{id}")
    public String reprogramarCita(
            @PathVariable Long id,
            @RequestParam LocalDate fecha,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFin
            ) throws MessagingException, IOException {
        citaService.reprogramarCita(id, fecha, horaInicio, horaFin);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/cancelar-cita/{id}")
    public String cancelarCita(@PathVariable Long id) throws MessagingException, IOException {
        citaService.cancelarCita(id);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/finalizar-cita/{idCita}")
    public String finalizarCita(@PathVariable Long idCita, @RequestParam Map<String, String> params) throws MessagingException, IOException {
        citaService.finalizarCita(idCita, params);
        return "redirect:/vendedor/citas";
    }
}