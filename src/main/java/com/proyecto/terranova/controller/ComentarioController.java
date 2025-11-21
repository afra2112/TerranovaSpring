package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.ComentarioService;
import com.proyecto.terranova.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/comprador/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    @Autowired
    private final UsuarioService usuarioService;

    private final ComentarioService comentarioService;

    @PostMapping("/agregar")
    public String agregarComentario(@RequestParam Long idProducto,
                                    @RequestParam String contenido,
                                    Authentication authentication) {
        // Verificar si el usuario está autenticado
        if (authentication == null || authentication.getName() == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            comentarioService.agregarComentario(usuario.getCedula(), idProducto, contenido);
            return "redirect:/detalle-producto/" + idProducto + "?success=Comentario agregado correctamente";
        } catch (RuntimeException e) {
            // Si lanza excepción (por ejemplo: no asistió a la cita)
            return "redirect:/detalle-producto/" + idProducto + "?error=" + e.getMessage();
        }
    }
}