package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.*;
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
    ComprobanteRepository comprobanteRepository;

    @Autowired
    InfoComprobanteRepository infoComprobanteRepository;

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

        List<Comprobante> comprobantesVenta =
                comprobanteRepository.findByVenta(ventaGeneral);

        Map<String, Comprobante> comprobantes = new HashMap<>();
        Map<NombreComprobanteEnum, InfoComprobante> infosComprobantes = new HashMap<>();

        List<InfoComprobante> infoCatalogo = infoComprobanteRepository.findAll();

        for (InfoComprobante info : infoCatalogo) {
            comprobantes.put(info.getNombreComprobante().name(), null);
        }

        for (Comprobante comp : comprobantesVenta) {
            comprobantes.put(comp.getInfoComprobante().getNombreComprobante().name(), comp);
        }

        model.addAttribute("comprobantes", comprobantes);
        model.addAttribute("infosComprobantes", infosComprobantes);

        switch (tipoProducto) {
            case "GANADO" -> {
                VentaGanado detalle = ventaGanadoRepository.findByVenta(ventaGeneral);
                model.addAttribute("venta", detalle);
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
        model.addAttribute("asistencias",
                asistenciaService.encontrarAsistenciasPorCitaYEstadoAsistencia(
                        ventaGeneral.getCita().getIdCita(),
                        EstadoAsistenciaEnum.ASISTIO
                )
        );
        return "vendedor/procesoVenta";
    }

    @PostMapping("/seleccionar-comprador")
    public String actualizarVentaPasos(@RequestParam Long idVenta, @RequestParam String cedula) {
        ventaService.seleccionarComprador(idVenta, cedula);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }
}
