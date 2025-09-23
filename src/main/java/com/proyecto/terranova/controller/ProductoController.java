
package com.proyecto.terranova.controller;


import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/vendedor/productos")
public class ProductoController {

    @Autowired
    UsuarioService  usuarioService;

    @Autowired
    ProductoService productoService;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CiudadRepository ciudadRepository;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }
    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }


    @GetMapping("/disponibilidades/{id}")
    public String mostrarModalDisponibilidades(@PathVariable(name = "id") Long idProducto){
        return "redirect:/vendedor/productos?idProducto=" + idProducto;
    }

    @GetMapping("/Detalle/{idProducto}")
    public String mostrarModalDetalle(@PathVariable Long idProducto, Model model, Authentication authentication){
        Producto producto = productoRepository.findById(idProducto).orElseThrow();

        String tipo = "";
        if (producto instanceof Terreno) {
            tipo = "terreno";
        } else if (producto instanceof Finca) {
            tipo = "finca";
        } else if (producto instanceof Ganado) {
            tipo = "ganado";
        }
        model.addAttribute("tipo", tipo);
        model.addAttribute("prod", producto);
        model.addAttribute("ciudades", ciudadRepository.findAll());
        model.addAttribute("usuario", usuario(authentication));
        return "vendedor/detalleProductoV";
    }

    @PostMapping("/editar/terreno")
    public String actualizarTerreno(@ModelAttribute Terreno terrenoForm, RedirectAttributes redirect) {
        productoService.actualizarProducto(terrenoForm);
        redirect.addFlashAttribute("mensaje", "Terreno actualizado correctamente");
        return "redirect:/vendedor/productos";
    }
    @PostMapping("/editar/finca")
    public String actualizarFinca(@ModelAttribute Finca fincaForm, RedirectAttributes redirect) {
        productoService.actualizarProducto(fincaForm);
        redirect.addFlashAttribute("mensaje", "Finca actualizada correctamente");
        return "redirect:/vendedor/productos";
    }
    @PostMapping("/editar/ganado")
    public String actualizarGanado(@ModelAttribute Ganado ganadoForm, RedirectAttributes redirect) {
        productoService.actualizarProducto(ganadoForm);
        redirect.addFlashAttribute("mensaje", "Ganado actualizado correctamente");
        return "redirect:/vendedor/productos";
    }

    @PostMapping("/guardarP")
    public String guardarProducto(@RequestParam Map<String,String> formdatos ,Authentication authentication,  @RequestParam(name = "idCiudad") Long idCiudad){

        String correo = authentication.getName(); // ahora es claro que es el correo
        Producto producto = productoService.crearProductoBase(formdatos, correo, idCiudad);
        if(producto instanceof Ganado){
            String cantidadGanado = formdatos.get("cantidad");
            Long precioProducto = Long.parseLong(cantidadGanado) * producto.getPrecioProducto();
            producto.setPrecioProducto(precioProducto);
            productoRepository.save(producto);
        }
        return "redirect:/vendedor/dashboard?productoId=" + producto.getIdProducto();
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, Authentication auth, RedirectAttributes redirect) {
        try {
            productoService.eliminarProducto(id, auth.getName());
            redirect.addFlashAttribute("exito", "Producto eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vendedor/productos";
    }


}