
package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuario/logueado")
    public String verNotificacionesUsuarioLogeado(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        List<NotificacionDTO> notificaciones = notificacionService.obtenerNoLeidasPorUsuario(usuario);

        model.addAttribute("usuario", usuario);
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
        Usuario usuario = usuarioRepository.findById(cedula)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado "));
        notificacionService.marcarTodasComoLeidas(usuario);
        return "redirect:/notificaciones/usuario/" + cedula;
    }


}