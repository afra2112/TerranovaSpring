
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Notificacion;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/usuarios")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
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

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @GetMapping("/notificaciones")
    public String verNotificacionesUsuarioLogeado(@RequestParam(name = "id") Long id, Model model, Authentication auth) {
        Usuario usuario = usuario(auth);
        String lugar = null;
        if(id == 1){
            lugar = "comprador";
        } else if (id == 2) {
            lugar = "vendedor";
        }

        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuario);

        model.addAttribute("totalDisponibilidades", notificacionService.contarPorUsuarioYTipo(usuario, "Disponibilidades"));
        model.addAttribute("totalProducto", notificacionService.contarPorUsuarioYTipo(usuario, "Productos"));
        model.addAttribute("totalVenta", notificacionService.contarPorUsuarioYTipo(usuario, "Ventas"));
        model.addAttribute("totalCita", notificacionService.contarPorUsuarioYTipo(usuario, "Citas"));
        model.addAttribute("totalSistema", notificacionService.contarPorUsuarioYTipo(usuario, "Sistema"));
        model.addAttribute("totalNoLeidas", notificacionService.contarNoLeidasPorUsuario(usuario, false));
        model.addAttribute("lugar", lugar);
        model.addAttribute("notificaciones", notificaciones);

        return "notificaciones";
    }

    @PostMapping("/marcar-leida/{id}")
    public String marcarComoLeida(@PathVariable Long idNotificacion, @RequestParam String cedula ){
        notificacionService.marcarComoLeida(idNotificacion);
        return "redirect:/notificaciones/usuario/" + cedula;
    }

    @PostMapping("/marcar-todas-leidas/{cedula}")
    public String marcarTodasComoLeidas(@PathVariable String cedula){
        Usuario usuario = usuarioService.findById(cedula);
        notificacionService.marcarTodasComoLeidas(usuario);
        return "redirect:/notificaciones/usuario/" + cedula;
    }


}