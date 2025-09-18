
package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.UsuarioService;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class VentaController {

    @Autowired
    VentaService ventaService;

    @Autowired
    UsuarioService usuarioService;

}