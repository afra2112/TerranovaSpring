package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.dto.UsuarioDTO;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.List;

@Controller
public class PublicController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ProductoService productoService;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CiudadRepository ciudadRepository;

    @Autowired
    AsistenciaService asistenciaService;

    @Autowired
    FavoritoService favoritoService;

    @Autowired
    CitaRepository citaRepository;

    @Autowired
    ComentarioService comentarioService;

    @GetMapping("/login")
    public String login(Model model){
        List<Producto> productos = productoRepository.findAll();
        List<String> ciudades = ciudadRepository.findAll().stream().map(Ciudad::getNombreCiudad).toList();
        for (Producto producto : productos) {
            producto.setTipoP(producto.getClass().getSimpleName());
        }
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("productos", productos);
        return "login";
    }

    @GetMapping("/productos")
    public String productos(
            Model model,
            Authentication authentication,
            @RequestParam(required = false) String busquedaTexto,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String orden){

        Usuario usuario;
        List<Producto> productos = productoService.filtrarConSpecification(busquedaTexto, tipo, orden);

        if (authentication != null){
            usuario = usuarioService.findByEmail(authentication.getName());

            List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario);

            boolean esVendedor = false;
            if(rolesUsuario.contains(RolEnum.VENDEDOR)){
                esVendedor = true;
            }

            List<Long> favoritosIds = favoritoService.obtenerIdsFavoritosPorUsuario(usuario);
            productos = productos.stream().filter(producto -> !producto.getVendedor().equals(usuario)).toList();
            productos.forEach(producto -> producto.setCitasDisponibles(citaRepository.countByProductoAndEstadoCita(producto, EstadoCitaEnum.PROGRAMADA)));
            model.addAttribute("favoritosIds", favoritosIds);
            model.addAttribute("nombreMostrar", usuario.getNombres() + ". " + usuario.getApellidos().charAt(0));
            model.addAttribute("esVendedor", esVendedor);
        } else {
            usuario = null;
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", productos);
        return "productos";
    }

    @GetMapping("/detalle-producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model, Authentication authentication){
        Usuario usuario = null;
        boolean yaTieneCita = false;
        boolean estaEnListaDeEspera = false;

        if(authentication != null){
            usuario = usuarioService.findByEmail(authentication.getName());

            List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario);

            boolean esVendedor = false;
            if(rolesUsuario.contains(RolEnum.VENDEDOR)){
                esVendedor = true;
            }

            List<Long> favoritosIds = favoritoService.obtenerIdsFavoritosPorUsuario(usuario);
            List<Comentario> listaComentario = comentarioService.obtenerComentariosPorProducto(id);

            model.addAttribute("usuarioInscrito", asistenciaService.existeAsistenciaPorCitaEnEstadoProgramada(usuario, id, EstadoCitaEnum.PROGRAMADA));
            model.addAttribute("usuarioEnEspera", asistenciaService.existeAsistenciaPorEstado(usuario, id, EstadoAsistenciaEnum.EN_ESPERA));
            model.addAttribute("favoritosIds", favoritosIds);
            model.addAttribute("nombreMostrar", usuario.getNombres() + ". " + usuario.getApellidos().charAt(0));
            model.addAttribute("esVendedor", esVendedor);
            model.addAttribute("comentariosL", listaComentario);
            yaTieneCita = asistenciaService.existeAsistenciaPorEstado(usuario, id, EstadoAsistenciaEnum.INSCRITO);
            estaEnListaDeEspera = asistenciaService.existeAsistenciaPorEstado(usuario, id, EstadoAsistenciaEnum.EN_ESPERA);
        }

        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("producto no encontrado"));
        producto.setTipoP(producto.getClass().getSimpleName());

        model.addAttribute("estaEnListaDeEspera", estaEnListaDeEspera);
        model.addAttribute("yaTieneCita", yaTieneCita);
        model.addAttribute("usuario", usuario);
        model.addAttribute("producto", producto);
        model.addAttribute("citasDisponibles", citaRepository.findByProductoAndEstadoCita(producto, EstadoCitaEnum.PROGRAMADA));

        return "detalleProducto";
    }

    @GetMapping("/registro")
    public String registroForm(Model model){
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute UsuarioDTO usuarioDTO, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "redirect:/registro";
        }
        if(!usuarioService.save(usuarioDTO)){
            System.out.println("YAEXISTE");
            redirectAttributes.addAttribute("yaExiste", true);
            return "redirect:/registro";
        }
        redirectAttributes.addAttribute("creado", true);
        return "redirect:/login";
    }

    @GetMapping("/password-olvidada")
    public String mostrarFormulario() {
        return "passwordOlvidada";
    }

    @PostMapping("/password-olvidada")
    public String procesarFormulario(@RequestParam String email, Model model) throws IOException {
        usuarioService.generarTokenYEnviarCorreoRecuperarContrasena(email);
        model.addAttribute("mensaje", "Se ha enviado un enlace a tu correo.");
        return "passwordOlvidada";
    }

    @GetMapping("/recuperar-password")
    public String mostrarReset(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "recuperarContrasena";
    }

    @PostMapping("/recuperar-password")
    public String procesarReset(@RequestParam String token, @RequestParam String nuevaContrasena, Model model) {
        String mensaje = usuarioService.validarTokenYActualizarContrasena(token, nuevaContrasena);
        model.addAttribute("mensaje", mensaje);
        return "login";
    }

    @GetMapping("/403")
    public String error403(){
        return "403";
    }
}
