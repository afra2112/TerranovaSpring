
package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/vendedor")
public class DisponibilidadController {

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    ProductoService productoService;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/mi-calendario/registrar-disponibilidad")
    public String registrarDisponibilidad(
            @RequestParam(name = "fecha") LocalDate fecha,
            @RequestParam(name = "hora") LocalTime hora,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe") String vieneDe,
            Authentication authentication
    ){
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setFecha(fecha);
        disponibilidad.setHora(hora);
        disponibilidad.setDescripcion(descripcion);
        disponibilidad.setProducto(productoService.findById(idProducto));
        disponibilidadService.save(disponibilidad);
        if(vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/eliminar-disponibilidad")
    public String eliminarDisponibilidad(
            @RequestParam(name = "idDisponibilidad") Long idDisponibilidad,
            @RequestParam(name = "idProducto") Long idProducto,
            @RequestParam(name = "vieneDe") String vieneDe
    ){
        disponibilidadService.delete(idDisponibilidad);
        if(vieneDe.equals("calendario")){
            return "redirect:/vendedor/mi-calendario";
        }
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }
}