package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    DisponibilidadService disponibilidadService;

    @ModelAttribute("esVendedor")
    public boolean esVendedor(Authentication authentication){
        List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario(authentication));

        boolean esVendedor = false;
        if(rolesUsuario.contains(RolEnum.VENDEDOR)){
            esVendedor = true;
        }
        return esVendedor;
    }

    @ModelAttribute
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @GetMapping("/dashboard")
    public String indexVendedor(Model model) {
        model.addAttribute("dashboard", true);
        return "vendedor/dashboard";
    }

    @GetMapping("/mi-calendario")
    public String calendario(Model model){
        List<Disponibilidad> disponibilidades = disponibilidadService.findAll();

        model.addAttribute("calendario", true);
        model.addAttribute("disponibilidades", disponibilidades);
        return "vendedor/calendario";
    }

    @PostMapping("/mi-calendario/registrar-disponibilidad")
    public String registrarDisponibilidad(@RequestParam(name = "fecha") LocalDate fecha, @RequestParam(name = "hora") LocalTime hora, @RequestParam(name = "descripcion", required = false) String descripcion, Authentication authentication){
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(fecha);
        disponibilidad.setHora(hora);
        disponibilidad.setDescripcion(descripcion);
        disponibilidad.setUsuario(usuarioService.findByEmail(authentication.getName()));
        disponibilidadService.save(disponibilidad);
        return "redirect:/vendedor/mi-calendario";
    }
}
