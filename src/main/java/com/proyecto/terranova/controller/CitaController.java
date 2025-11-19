
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
        cita.setOcupados(cita.getAsistencias().size());
        cita.setDisponibles(cita.getCupoMaximo() - cita.getOcupados());

        citaService.save(cita);

        if(vieneDe != null && vieneDe.equals("calendario")){
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
            ) throws IOException {
        citaService.reprogramarCita(id, fecha, horaInicio, horaFin);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/cancelar-cita/{id}")
    public String cancelarCita(@PathVariable Long id) throws IOException {
        citaService.cancelarCita(id);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/finalizar-cita/{idCita}")
    public String finalizarCita(@PathVariable Long idCita, @RequestParam Map<String, String> params) throws IOException {
        citaService.finalizarCita(idCita, params);
        return "redirect:/vendedor/citas";
    }
}