package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.service.AsistenciaService;
import com.proyecto.terranova.service.UsuarioService;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/vendedor/ventas")
public class VentaController {

    @Autowired
    VentaService ventaService;

    @Autowired
    AsistenciaService asistenciaService;

    @Autowired
    UsuarioService usuarioService;

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

    @PostMapping("/iniciar")
    public String iniciarVenta(@RequestParam Long idCita) {
        Venta venta = ventaService.generarVenta(idCita);
        return "redirect:/vendedor/ventas/detalle-venta/" + venta.getIdVenta();
    }

    @GetMapping("/detalle-venta/{id}")
    public String procesoVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id);

        model.addAttribute("tipoProducto", venta.getProducto().getClass().getSimpleName().toUpperCase());
        model.addAttribute("venta", venta);
        model.addAttribute("asistencias", asistenciaService.encontrarAsistenciasPorCitaYEstadoAsistencia(venta.getCita().getIdCita(), EstadoAsistenciaEnum.ASISTIO));
        return "vistasTemporales/procesoVenta";
    }

    @PostMapping("/seleccionar-comprador")
    public String actualizarVentaPasos(@RequestParam Long idVenta, @RequestParam String cedula) {
        ventaService.seleccionarComprador(idVenta, cedula);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }

    @PostMapping("/oferta")
    public String actualizarVentaPaso2Ganado(
            @RequestParam Long idVenta,
            @RequestParam Long precioTotal,
            @RequestParam int cantidad,
            @RequestParam String condicionesEntrega,
            @RequestParam(required = false) String observacionesSanitarias
    ) {
        ventaService.actualizarVentaPaso2Ganado(idVenta, precioTotal, cantidad, condicionesEntrega, observacionesSanitarias);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }

    @PostMapping("/documentacion-sanitaria")
    public String actualizarVentaPaso3Ganado(
            @RequestParam Long idVenta,
            @RequestParam MultipartFile certificadosSanitarios,
            @RequestParam MultipartFile registroProcedencia,
            @RequestParam MultipartFile inventarioLote
    ) throws IOException {
        ventaService.actualizarVentaPaso3Ganado(idVenta, certificadosSanitarios, registroProcedencia, inventarioLote);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }
}
