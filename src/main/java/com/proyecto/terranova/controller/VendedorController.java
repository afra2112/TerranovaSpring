package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    VentaService ventaService;

    @Autowired
    ProductoService productoService;

    @Autowired
    CitaService citaService;

    @Autowired
    GastoVentaService gastoVentaService;

    @Autowired
    ComprobanteService comprobanteService;

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
    public String indexVendedor(Model model) {
        model.addAttribute("dashboard", true);
        return "vendedor/dashboard";
    }

    @GetMapping("/mi-calendario")
    public String calendario(Model model, Authentication authentication){
        model.addAttribute("calendario", true);
        model.addAttribute("productos", productoService.findAll());
        return "vendedor/calendario";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        Usuario vendedor = usuarioService.findByEmail(authentication.getName());
        model.addAttribute("posicionCitas", true);
        model.addAttribute("numReservadas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.RESERVADA).size());
        model.addAttribute("numFinalizadas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.FINALIZADA).size());
        model.addAttribute("numCanceladas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.CANCELADA).size());

        model.addAttribute("citas", citaService.encontrarPorVendedor(vendedor));

        return "vendedor/citas";
    }


    @GetMapping("/ventas")
    public String ventas(Model model, Authentication authentication){
        List<Venta> ventas = ventaService.encontrarPorVendedor(usuarioService.findByEmail(authentication.getName()));

        Long ingresosTotales = 0L;
        Long gastosTotales = 0L;
        Long balanceFinal = 0L;

        for (Venta venta : ventas){
            ingresosTotales += venta.getProducto().getPrecioProducto();
            gastosTotales += venta.getTotalGastos();
            balanceFinal = ingresosTotales - gastosTotales;
        }

        String nombreCompleto = usuario(authentication).getNombres() + " " + usuario(authentication).getApellidos();

        model.addAttribute("posicionVentas", true);
        model.addAttribute("ingresosTotales", ingresosTotales);
        model.addAttribute("gastosTotales", gastosTotales);
        model.addAttribute("balanceFinal", balanceFinal);
        model.addAttribute("ventas", ventas);
        model.addAttribute("nombres", nombreCompleto);
        return "vendedor/ventas";
    }

    @PostMapping("/ventas/actualizar-datos")
    @ResponseBody
    public String actualizarDatos(
            @ModelAttribute Venta venta,
            @RequestParam(required = false) List<Long> idsGastosEliminados,
            @RequestParam(required = false) List<Long> idsComprobantesEliminados,
            @RequestParam(required = false) List<MultipartFile> comprobantes
    ) {
        try {
            ventaService.actualizarDatosVenta(venta, idsComprobantesEliminados, idsGastosEliminados, comprobantes);
            return "ok";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/venta/enviar-peticion-venta")
    public String enviarPeticionVenta(@ModelAttribute Venta venta, @RequestParam(name = "comprobantes") int comprobantes, RedirectAttributes redirectAttributes) {
        if(venta.getFechaVenta() == null || (venta.getMetodoPago() == null || venta.getMetodoPago().isBlank()) || comprobantes == 0){
            redirectAttributes.addFlashAttribute("ventaIncompleta", true);
            return "redirect:/vendedor/ventas";
        }
        //ENVIAR NOTIFICACION AL COMPRADOR y luego:
        ventaService.actualizarEstado(venta, "Pendiente Confirmacion");
        redirectAttributes.addFlashAttribute("peticionHecha", true);
        return "redirect:/vendedor/ventas";
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
}
