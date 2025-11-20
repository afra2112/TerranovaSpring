package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.entity.Transporte;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.entity.VentaGanado;
import com.proyecto.terranova.repository.VentaGanadoRepository;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.TransporteService;
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
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class VentaGanadoController {

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    VentaGanadoRepository ventaGanadoRepository;

    @Autowired
    TransporteService transporteService;

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
            @RequestParam(required = false) MultipartFile certificadoSanitario,
            @RequestParam(required = false) MultipartFile facturaPropiedad,
            @RequestParam(required = false) MultipartFile inventarioLote,
            @RequestParam(required = false) MultipartFile certificadoSinigan,
            @RequestParam(required = false) MultipartFile certificadoHierro,
            @RequestParam(required = false) MultipartFile certificadoPesaje,
            @RequestParam(required = false) String accion, // "guardar" o "continuar"
            RedirectAttributes redirectAttributes
    ) {
        try {
            ventaService.actualizarVentaPaso3Ganado(
                    idVenta,
                    gsmi,
                    certificadoSanitario,
                    facturaPropiedad,
                    inventarioLote,
                    certificadoSinigan,
                    certificadoHierro,
                    certificadoPesaje,
                    observacionesSanitarias
            );

            if ("continuar".equals(accion)) {
                Venta venta = ventaService.findById(idVenta);
                VentaGanado ventaGanado = ventaGanadoRepository.findByVenta(venta);

                //aqui valido que esten todos los documentos obligatorios en el mapa
                boolean tieneGsmi = ventaGanado.getComprobantesInfo().containsKey(NombreComprobanteEnum.GSMI)
                        && ventaGanado.getComprobantesInfo().get(NombreComprobanteEnum.GSMI).getComprobante() != null;
                boolean tieneCertificado = ventaGanado.getComprobantesInfo().containsKey(NombreComprobanteEnum.CERTIFICADO_SANITARIO)
                        && ventaGanado.getComprobantesInfo().get(NombreComprobanteEnum.CERTIFICADO_SANITARIO).getComprobante() != null;
                boolean tieneFactura = ventaGanado.getComprobantesInfo().containsKey(NombreComprobanteEnum.FACTURA_PROPIEDAD)
                        && ventaGanado.getComprobantesInfo().get(NombreComprobanteEnum.FACTURA_PROPIEDAD).getComprobante() != null;

                if (!tieneGsmi || !tieneCertificado || !tieneFactura) {
                    redirectAttributes.addFlashAttribute("error", "Debes subir todos los documentos obligatorios antes de continuar");
                    return "redirect:/vendedor/ventas/detalle-venta/" + idVenta;
                }

                venta.setPasoActual(4);
                ventaService.save(venta);
                redirectAttributes.addFlashAttribute("success", "Documentación completada. Avanzando al siguiente paso");
            } else {
                redirectAttributes.addFlashAttribute("success", "Documentos actualizados correctamente");
            }

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

    @PostMapping("/comprador/compras/programar-transporte")
    public String programarTransporte(
            @RequestParam Long idVenta,
            @RequestParam LocalDate fechaTransporte,
            @RequestParam LocalTime horaTransporte,
            @RequestParam String empresaTransporte,
            @RequestParam String placaVehiculo,
            @RequestParam String nombreConductor,
            @RequestParam String telefonoConductor,
            @RequestParam String cedulaConductor,
            @RequestParam String puntoEntrega,
            @RequestParam(required = false) String observacionesComprador,
            RedirectAttributes redirectAttributes
    ) {
        try {
            transporteService.programarTransporte(
                    idVenta,
                    fechaTransporte,
                    horaTransporte,
                    empresaTransporte,
                    placaVehiculo,
                    nombreConductor,
                    telefonoConductor,
                    cedulaConductor,
                    puntoEntrega,
                    observacionesComprador
            );

            redirectAttributes.addFlashAttribute("success", "Transporte programado exitosamente. El vendedor ha sido notificado.");
            return "redirect:/comprador/compras/detalle/" + idVenta;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al programar transporte: " + e.getMessage());
            return "redirect:/comprador/compras/detalle/" + idVenta;
        }
    }

    @PostMapping("/comprador/compras/confirmar-recepcion")
    public String confirmarRecepcion(
            @RequestParam Long idTransporte,
            @RequestParam MultipartFile fotoGanadoDescargado,
            @RequestParam MultipartFile fotoGanadoNuevoLote,
            RedirectAttributes redirectAttributes
    ) {
        try {
            transporteService.confirmarRecepcion(idTransporte, fotoGanadoDescargado, fotoGanadoNuevoLote);

            Transporte transporte = transporteService.encontrarPorId(idTransporte);
            redirectAttributes.addFlashAttribute("success", "¡Recepción confirmada! La venta ha finalizado exitosamente.");
            return "redirect:/comprador/compras/detalle/" + transporte.getVenta().getIdVenta();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar recepción: " + e.getMessage());
            return "redirect:/comprador/compras";
        }
    }

    @PostMapping("/vendedor/ventas/confirmar-carga")
    public String confirmarCarga(
            @RequestParam Long idTransporte,
            @RequestParam MultipartFile fotoCamionVacio,
            @RequestParam MultipartFile fotoCargaGanado,
            @RequestParam MultipartFile fotoGsmiConductor,
            @RequestParam(required = false) String observacionesVendedor,
            RedirectAttributes redirectAttributes
    ) {
        try {
            transporteService.confirmarCarga(
                    idTransporte,
                    fotoCamionVacio,
                    fotoCargaGanado,
                    fotoGsmiConductor,
                    observacionesVendedor
            );

            Transporte transporte = transporteService.encontrarPorId(idTransporte);
            redirectAttributes.addFlashAttribute("success", "Carga confirmada. El ganado está en camino.");
            return "redirect:/vendedor/ventas/detalle-venta/" + transporte.getVenta().getIdVenta();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar carga: " + e.getMessage());
            return "redirect:/vendedor/ventas";
        }
    }

    @GetMapping("/transporte/{nombreArchivo}")
    public ResponseEntity<Resource> descargarFotoTransporte(@PathVariable String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get("uploads/transporte/").resolve(nombreArchivo);
            Resource resource = new UrlResource(rutaArchivo.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el archivo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo", e);
        }
    }
}
