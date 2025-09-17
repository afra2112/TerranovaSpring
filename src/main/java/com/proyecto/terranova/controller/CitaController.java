
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.service.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/cancelar-cita")
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita){
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);
        return "redirect:/vendedor/citas";
    }

    @PostMapping("/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad){
        Cita cita = citaService.findById(idCita);
        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
        cita.setDisponibilidad(disponibilidad);
        citaService.save(cita);
        return "redirect:/vendedor/citas";
    }
}