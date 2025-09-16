
package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendedor/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/disponibilidades/{id}")
    public String mostrarModalDisponibilidades(@PathVariable(name = "id") Long idProducto){
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }
}