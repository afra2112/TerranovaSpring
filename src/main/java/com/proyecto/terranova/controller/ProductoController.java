
package com.proyecto.terranova.controller;


import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/vendedor/productos")
public class ProductoController {

    @Autowired
    UsuarioService  usuarioService;

    @Autowired
    ProductoService productoService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }


    @GetMapping("/disponibilidades/{id}")
    public String mostrarModalDisponibilidades(@PathVariable(name = "id") Long idProducto){
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/Detalle")
    public String mostrarModalDetalle(Model model, Authentication authentication){
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("usuario", usuario(authentication));
        return "vendedor/detalleProductoV";
    }

    @PostMapping("/guardarP")
    public String guardarProducto(@RequestParam Map<String,String> formdatos ,Authentication authentication,  @RequestParam(name = "idCiudad") Long idCiudad){
        if (!formdatos.containsKey("cercado")) {
            formdatos.put("cercado", "false");
        }
        String correo = authentication.getName(); // ahora es claro que es el correo
        Producto producto = productoService.crearProductoBase(formdatos, correo, idCiudad);

        return "redirect:/vendedor/dashboard?productoId=" + producto.getIdProducto();
    }

    /*
    public ResponseEntity<Long> guardarProducto(@RequestParam Map<String , String> datosForm , Authentication authentication) {
        String cedula = authentication.getName();
        Producto producto = productoService.crearProductoBase(datosForm,cedula);
        return ResponseEntity.ok(producto.getIdProducto());
    }*/


    /*@GetMapping("/imagenes")
    public String mostrarFormularioImagenes(@RequestParam Long id, Model model) {
        if (id != null) {
            model.addAttribute("productoId", id);
        }
        return "vendedor/dashboard";
    }*/
}