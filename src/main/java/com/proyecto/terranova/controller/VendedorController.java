package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.ProductoService;
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

    @Autowired
    ProductoService productoService;

    @Autowired
    CitaService citaService;

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
    public String indexVendedor(@RequestParam(required = false) Long productoId,Model model) {
        if (productoId != null) {
            model.addAttribute("productoId", productoId);
        }
        model.addAttribute("dashboard", true);
        return "vendedor/dashboard";
    }

    @GetMapping("/mi-calendario")
    public String calendario(Model model, Authentication authentication){
        model.addAttribute("calendario", true);
        return "vendedor/calendario";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        model.addAttribute("posicionCitas", true);
        model.addAttribute("foto", usuarioService.findByEmail(authentication.getName()).getFoto());
        model.addAttribute("numReservadas", citaService.encontrarPorEstado(EstadoCitaEnum.RESERVADA).size());
        model.addAttribute("numFinalizadas", citaService.encontrarPorEstado(EstadoCitaEnum.FINALIZADA).size());
        model.addAttribute("numCanceladas", citaService.encontrarPorEstado(EstadoCitaEnum.CANCELADA).size());

        model.addAttribute("citas", citaService.findAll());

        return "vendedor/citas";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(required = false, name = "idProducto") Long idProducto,Model model, Authentication authentication){
        model.addAttribute("posicionProductos", true);
        model.addAttribute("productos", productoService.findAll());
        if(idProducto != null){
            model.addAttribute("producto", productoService.findById(idProducto));
            model.addAttribute("mostrarModalDisponibilidades", true);
            return "vendedor/productos";
        }
        return "vendedor/productos";
    }

    @PostMapping("/mi-calendario/registrar-disponibilidad")
    public String registrarDisponibilidad(@RequestParam(name = "fecha") LocalDate fecha, @RequestParam(name = "hora") LocalTime hora, @RequestParam(name = "descripcion", required = false) String descripcion, @RequestParam(name = "idProducto") Long idProducto, Authentication authentication){
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(fecha);
        disponibilidad.setHora(hora);
        disponibilidad.setDescripcion(descripcion);
        disponibilidad.setProducto(productoService.findById(idProducto));
        disponibilidadService.save(disponibilidad);
        return "redirect:/vendedor/mi-calendario";
    }
}
