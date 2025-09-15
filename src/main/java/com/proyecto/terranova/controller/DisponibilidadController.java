
package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.UsuarioService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/vendedor/mi-calendario")
public class DisponibilidadController {

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    UsuarioService usuarioService;


}