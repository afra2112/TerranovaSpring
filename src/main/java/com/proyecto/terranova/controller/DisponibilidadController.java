
package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/vendedor")
public class DisponibilidadController {

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    ProductoService productoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    NotificacionService notificacionService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @PostMapping("/mi-calendario/registrar-disponibilidad")
    public String registrarDisponibilidad(
            @RequestParam(name = "fecha") LocalDate fecha,
            @RequestParam(name = "hora") LocalTime hora,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe,
            Authentication authentication
    ){
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(fecha);
        disponibilidad.setHora(hora);
        disponibilidad.setDescripcion(descripcion);
        disponibilidad.setProducto(productoService.findById(idProducto));
        disponibilidadService.save(disponibilidad);

        if(usuario(authentication).isNotificacionesDisponibilidades()){
            DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", new Locale("es", "ES"));
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("h:mm a", new Locale("es", "ES"));

            String mensaje = "Haz creado una disponibilidad para el producto: "+disponibilidad.getProducto().getNombreProducto()+ ". Con la siguiente fecha: " + fechaFormatter.format(disponibilidad.getFecha()) + " a las "+ horaFormatter.format(disponibilidad.getHora());
            notificacionService.crearNotificacionAutomatica(mensaje, "Disponibilidad", usuario(authentication), idProducto, "/vendedor/mi-calendario");
        }

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/eliminar-disponibilidad")
    public String eliminarDisponibilidad(
            @RequestParam(name = "idDisponibilidad") Long idDisponibilidad,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe", required = false) String vieneDe,
            Authentication authentication
    ){
        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);

        if(usuario(authentication).isNotificacionesDisponibilidades()){
            DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", new Locale("es", "ES"));
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("h:mm a", new Locale("es", "ES"));

            String mensaje = "Elimiaste la disponibilidad con fecha: " + fechaFormatter.format(disponibilidad.getFecha()) + ". A las: " + horaFormatter.format(disponibilidad.getHora()) + " Para el producto: " + disponibilidad.getProducto().getNombreProducto();
            notificacionService.crearNotificacionAutomatica(mensaje, "Disponibilidad", usuario(authentication), idProducto, "/vendedor/mi-calendario");
        }

        disponibilidadService.delete(idDisponibilidad);

        if(vieneDe != null && vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }
}