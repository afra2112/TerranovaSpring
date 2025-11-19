package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.PathVariable;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    ) throws IOException {
        ventaService.actualizarVentaPaso2Ganado(idVenta, precioTotal, cantidad, observacionesSanitarias);
        return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
    }

    @PostMapping("/vendedor/ventas/documentacion-sanitaria")
    public String guardarDocumentacionSanitaria(
            @RequestParam Long idVenta,
            @RequestParam(required = false) String observacionesSanitarias,
            @RequestParam(required = false) MultipartFile gsmi,
            @RequestParam(required = false) MultipartFile certificadosSanitarios,
            @RequestParam(required = false) MultipartFile facturaPropiedad,
            @RequestParam(required = false) MultipartFile inventarioLote,
            @RequestParam(required = false) MultipartFile certificadoSinigan,
            @RequestParam(required = false) MultipartFile certificadoHierro,
            @RequestParam(required = false) MultipartFile certificadoPesaje,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ventaService.actualizarVentaPaso3Ganado(idVenta, gsmi, certificadosSanitarios, facturaPropiedad, inventarioLote, certificadoSinigan, certificadoHierro, certificadoPesaje, observacionesSanitarias);

            redirectAttributes.addFlashAttribute("success", "Documentación sanitaria guardada correctamente");
            return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la documentación: " + e.getMessage());
            return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
        }
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

    @GetMapping("/documentos/{nombreArchivo}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get("uploads/documentos/").resolve(nombreArchivo);
            Resource resource = new UrlResource(rutaArchivo.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el archivo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo", e);
        }
    }
}
