package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.ComprobanteDTO;
import com.proyecto.terranova.service.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Comprobantes")
public class ComprobanteControllerRest {

    @Autowired
    private ComprobanteService serviceComprobante;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<ComprobanteDTO>> obtenerTodosLosComprobantes(){
        List<ComprobanteDTO> entidadesComprobante = serviceComprobante.findAll();
        return ResponseEntity.ok(entidadesComprobante);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteDTO> obtenerComprobantePorId(@PathVariable Long id){
        ComprobanteDTO dtoComprobante = serviceComprobante.findById(id);
        return ResponseEntity.ok(dtoComprobante);
    }

    @PostMapping("/crearComprobante")
    public ResponseEntity<ComprobanteDTO> crearComprobante(@RequestBody ComprobanteDTO dtoComprobante){
        serviceComprobante.save(dtoComprobante);
        return ResponseEntity.ok(dtoComprobante);
    }

    @DeleteMapping("/eliminarComprobante/{id}")
    public ResponseEntity<Long> eliminarComprobante(@PathVariable Long id){
        serviceComprobante.delete(id);
        return ResponseEntity.ok(id);
    }
}
