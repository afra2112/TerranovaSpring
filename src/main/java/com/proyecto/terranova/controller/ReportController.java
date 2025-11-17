package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.repository.VentaRepository;
import com.proyecto.terranova.service.ReportService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    VentaRepository ventaRepository;

    @GetMapping("/ventas/{nombre}")
    public ResponseEntity<byte[]> generarReporteVentas(@PathVariable(name = "nombre") String nombre) {
        try {
            List<Venta> ventas = ventaRepository.findAll();

            byte[] pdfBytes = reportService.generarReporteVentas(ventas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte-ventas-"+nombre+".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
