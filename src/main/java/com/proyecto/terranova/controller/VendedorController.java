package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @Autowired
    CiudadRepository ciudadRepository;

    @Autowired
    NotificacionService notificacionService;

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
    public String indexVendedor(@RequestParam(required = false) Long productoId,Model model, Authentication authentication) {
        Usuario usuario = usuario(authentication);

        if (productoId != null) {
            model.addAttribute("productoId", productoId);
        }
        model.addAttribute("ciudades", ciudadRepository.findAll());
        model.addAttribute("dashboard", true);
        model.addAttribute("totalVentas", ventaService.encontrarPorVendedor(usuario).size());
        model.addAttribute("productos", productoService.obtenerTodosPorVendedor(usuario).stream().limit(3).toList());
        model.addAttribute("totalCitas", citaService.encontrarPorVendedor(usuario, true).size());
        model.addAttribute("notificaciones", notificacionService.obtenerPorUsuarioYLeido(usuario,true));
        return "vendedor/dashboard";
    }

    @GetMapping("/mi-calendario")
    public String calendario(Model model, Authentication authentication){
        model.addAttribute("calendario", true);
        model.addAttribute("productos", productoService.obtenerTodosPorVendedor(usuario(authentication)));
        model.addAttribute("cedula", usuario(authentication).getCedula());
        return "vendedor/calendario";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        Usuario vendedor = usuarioService.findByEmail(authentication.getName());
        model.addAttribute("posicionCitas", true);
        model.addAttribute("numReservadas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.PROGRAMADA, true).size());
        model.addAttribute("numFinalizadas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.FINALIZADA, true).size());
        model.addAttribute("numCanceladas", citaService.encontrarPorEstado(vendedor,EstadoCitaEnum.CANCELADA, true).size());

        model.addAttribute("citas", citaService.encontrarPorVendedor(vendedor, true));

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
            @RequestParam(required = false) List<MultipartFile> comprobantes,
            Authentication authentication
    ) {
        try {
            Venta ventaActualizada = ventaService.actualizarDatosVenta(venta, idsComprobantesEliminados, idsGastosEliminados, comprobantes);

            notificacionService.notificacionVentaModificada(ventaActualizada);

            return "ok";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/venta/enviar-peticion-venta")
    public String enviarPeticionVenta(@ModelAttribute Venta venta, @RequestParam(name = "comprobantes") int comprobantes, RedirectAttributes redirectAttributes, Authentication authentication) throws MessagingException, IOException {
        if(venta.getFechaVenta() == null || (venta.getMetodoPago() == null || venta.getMetodoPago().isBlank()) || comprobantes == 0){
            redirectAttributes.addFlashAttribute("ventaIncompleta", true);
            return "redirect:/vendedor/ventas";
        }
        Venta ventaActual = ventaService.actualizarEstado(venta, "Pendiente Confirmacion");

        notificacionService.notificacionPeticionFinalizacionVenta(ventaActual);

        redirectAttributes.addFlashAttribute("peticionHecha", true);
        return "redirect:/vendedor/ventas";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(required = false, name = "idProducto") Long idProducto,Model model, Authentication authentication){
        List<Producto> productos = productoService.obtenerTodosPorVendedor(usuario(authentication));

        model.addAttribute("posicionProductos", true);
        model.addAttribute("productos", productos);

        long numDisponibilidades = 0;

        for(Producto producto : productos){
            long citas = producto.getCitas().stream().count();
            numDisponibilidades += citas;
        }

        model.addAttribute("disponibilidades", numDisponibilidades);

        if(idProducto != null){
            model.addAttribute("producto", productoService.findById(idProducto));
            model.addAttribute("mostrarModalDisponibilidades", true);
            return "vendedor/productos";
        }
        return "vendedor/productos";
    }
}