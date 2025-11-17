package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.VentaService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class VentaGanadoController {

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    VentaService ventaService;

    @PostMapping("/vendedor/ventas/oferta")
    public String actualizarVentaPaso2Ganado(
            @RequestParam Long idVenta,
            @RequestParam Long precioTotal,
            @RequestParam int cantidad,
            @RequestParam(required = false) String observacionesSanitarias
    ) throws MessagingException, IOException {
        ventaService.actualizarVentaPaso2Ganado(idVenta, precioTotal, cantidad, observacionesSanitarias);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }

    @PostMapping("/vendedor/ventas/documentacion-sanitaria")
    public String actualizarVentaPaso3Ganado(
            @RequestParam Long idVenta,
            @RequestParam MultipartFile certificadosSanitarios,
            @RequestParam MultipartFile registroProcedencia,
            @RequestParam MultipartFile inventarioLote
    ) throws IOException {
        ventaService.actualizarVentaPaso3Ganado(idVenta, certificadosSanitarios, registroProcedencia, inventarioLote);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }

    @PostMapping("/comprador/compras/confirmar-negociacion")
    public String actializarPaso2Comprador(
            @RequestParam Long idVenta,
            @RequestParam String respuesta,
            @RequestParam(required = false) String razonRechazo,
            @RequestParam int cantidad,
            @RequestParam Long precio
    ) {
        ventaService.aceptarNegociacion(idVenta, respuesta, razonRechazo, cantidad, precio);
        return "redirect:/comprador/compras/detalle/" + idVenta;
    }
}
