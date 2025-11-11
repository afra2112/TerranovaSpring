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
    UsuarioService usuarioService;

    private final ComentarioService comentarioService;

    @PostMapping("/agregar")
    public String agregarComentario(@RequestParam Long idProducto,
                                    @RequestParam String contenido,
                                    Authentication authentication) {
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        if (usuario == null) {
            return "redirect:/login";
        }

        comentarioService.agregarComentario(usuario.getCedula(), idProducto, contenido);
        return "redirect:/producto/detalle/" + idProducto;
    }
}
