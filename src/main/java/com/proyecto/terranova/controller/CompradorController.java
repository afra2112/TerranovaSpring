package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.service.CompradorService;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comprador")
public class CompradorController {

    @Autowired
    CompradorService compradorService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    CitaService citaService;

    @Autowired
    DisponibilidadService disponibilidadService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @ModelAttribute("esVendedor")
    public boolean esVendedor(Authentication authentication){
        List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario(authentication));

        boolean esVendedor = false;
        if(rolesUsuario.contains(RolEnum.VENDEDOR)){
            esVendedor = true;
        }
        return esVendedor;
    }

    @GetMapping("/explorar")
    public String index(Model model, Authentication authentication){
        model.addAttribute("explorar", true);

        Map<String, Integer> estadisticas = compradorService.prepararIndex(usuario(authentication).getCedula());
        model.addAllAttributes(estadisticas);
        return "comprador/principalComprador";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        model.addAttribute("posicionCitas", true);
        model.addAttribute("citas", citaService.encontrarPorComprador(usuarioService.findByEmail(authentication.getName())));
        return "comprador/citas";
    }

    @PostMapping("/citas/cancelar-cita")
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita){
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);
        return "redirect:/comprador/citas";
    }

    @PostMapping("/citas/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad){
        Cita cita = citaService.findById(idCita);
        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
        cita.setDisponibilidad(disponibilidad);
        citaService.save(cita);
        return "redirect:/comprador/citas";
    }

    @PostMapping("/mi-perfil/ser-vendedor")
    public String serVendedor(Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuario(authentication);

        usuarioService.volverVendedor(usuario(authentication).getCedula());
        List<GrantedAuthority> authorities = new ArrayList<>();
        usuario.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getNombreRol().toString().toUpperCase())));
        UsernamePasswordAuthenticationToken nuevoAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(nuevoAuth);

        redirectAttributes.addFlashAttribute("vendedorExitoso", true);

        return "redirect:/usuarios/mi-perfil?id=1";
    }
}
