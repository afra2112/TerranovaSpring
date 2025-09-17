
package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vendedor/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/disponibilidades/{id}")
    public String mostrarModalDisponibilidades(@PathVariable(name = "id") Long idProducto){
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/registar")
    public String mostrarModalRegistar(Model model){
        model.addAttribute("productos", new ProductoDTO());
        return "vendedor/dashboard";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO productoDTOdto, RedirectAttributes redirect) {
        Producto producto = productoService.crearProductoBase(productoDTOdto);
        redirect.addAttribute("id", producto.getIdProducto());
        return "redirect:/productos/imagenes" + producto.getIdProducto();
    }

    @GetMapping("/imagenes")
    public String mostrarFormularioImagenes(@RequestParam Long id, Model model) {
        model.addAttribute("productoId", id);
        return "productos/formulario-imagenes";
    }
}