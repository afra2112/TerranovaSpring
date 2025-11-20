package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.VentaFincaRepository;
import com.proyecto.terranova.repository.VentaGanadoRepository;
import com.proyecto.terranova.repository.VentaTerrenoRepository;
import com.proyecto.terranova.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vendedor/ventas")
public class VentaController {

    @Autowired
    VentaService ventaService;

    @Autowired
    AsistenciaService asistenciaService;

    @Autowired
    VentaGanadoRepository ventaGanadoRepository;

    @Autowired
    VentaTerrenoRepository ventaTerrenoRepository;

    @Autowired
    VentaFincaRepository ventaFincaRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TransporteService transporteService;

    @Autowired
    GastoVentaService gastoVentaService;

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
        Venta ventaGeneral = ventaService.findById(id);
        String tipoProducto = ventaGeneral.getProducto().getClass().getSimpleName().toUpperCase();

        Transporte transporte = transporteService.obtenerPorVenta(ventaGeneral);
        model.addAttribute("transporte", transporte);

        switch (tipoProducto){
            case "GANADO" -> {
                VentaGanado detalle = ventaGanadoRepository.findByVenta(ventaGeneral);

                // Cargar los comprobantes existentes
                Map<String, Comprobante> comprobantes = new HashMap<>();
                Map<String, InfoComprobante> infosComprobantes = new HashMap<>();

                for (Map.Entry<NombreComprobanteEnum, InfoComprobante> entry : detalle.getComprobantesInfo().entrySet()) {
                    infosComprobantes.put(entry.getKey().name(), entry.getValue());
                    if (entry.getValue().getComprobante() != null) {
                        comprobantes.put(entry.getKey().name(), entry.getValue().getComprobante());
                    }
                }

                model.addAttribute("venta", detalle);
                model.addAttribute("comprobantes", comprobantes);
                model.addAttribute("infosComprobantes", infosComprobantes);
                model.addAttribute("fragment", "fragments/vendedor/ventaGanadoVendedor");
            }
            case "TERRENO" -> {
                VentaTerreno detalle = ventaTerrenoRepository.findByVenta(ventaGeneral);
                model.addAttribute("venta", detalle);
                model.addAttribute("fragment", "fragments/vendedor/ventaTerrenoVendedor");
            }
            case "FINCA" -> {
                VentaFinca detalle = ventaFincaRepository.findByVenta(ventaGeneral);
                model.addAttribute("venta", detalle);
                model.addAttribute("fragment", "fragments/vendedor/ventaFincaVendedor");
            }
        }

        model.addAttribute("tipoProducto", tipoProducto);
        model.addAttribute("asistencias", asistenciaService.encontrarAsistenciasPorCitaYEstadoAsistencia(ventaGeneral.getCita().getIdCita(), EstadoAsistenciaEnum.ASISTIO));
        return "vendedor/procesoVenta";
    }

    @PostMapping("/seleccionar-comprador")
    public String actualizarVentaPasos(@RequestParam Long idVenta, @RequestParam String cedula) {
        ventaService.seleccionarComprador(idVenta, cedula);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }
}
