package com.proyecto.terranova.controller;


import com.proyecto.terranova.dto.ComentarioDTO;
import com.proyecto.terranova.entity.Comentario;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.ComentarioService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;
    private final ProductoRepository productoRepository;

    @Autowired
    public ComentarioController(ComentarioService comentarioService, UsuarioService usuarioService, ProductoRepository productoRepository) {
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
        this.productoRepository = productoRepository;
    }

    @PostMapping("/guardar/{idProducto}")
    public String guardarComentario(@PathVariable Long idProducto,
                                    @RequestParam("contenido") String contenido,
                                    Authentication auth) {

        Usuario usuario = usuarioService.findByEmail(auth.getName());
        Producto producto = productoRepository.findById(idProducto).orElseThrow();

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setProducto(producto);
        comentario.setContenido(contenido);
        comentario.setFechaComentario(LocalDateTime.now());

        comentarioService.guardar(comentario);

        return "redirect:/vendedor/productos/Detalle/" + idProducto;
    }

    @GetMapping("/listar/{idProducto}")
    @ResponseBody
    public List<ComentarioDTO> listarComentarios(@PathVariable Long idProducto) {
        List<Comentario> comentarios = comentarioService.listarPorProducto(idProducto);

        return comentarios.stream().map(c -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setIdComentario(c.getIdComentario());
            dto.setIdProducto(c.getProducto().getIdProducto());
            dto.setCedulaUsuario(c.getUsuario().getCedula());
            dto.setNombreUsuario(c.getUsuario().getNombres() + " " + c.getUsuario().getApellidos());
            dto.setContenido(c.getContenido());
            dto.setFechaComentario(c.getFechaComentario());
            return dto;
        }).collect(Collectors.toList());
    }
}
