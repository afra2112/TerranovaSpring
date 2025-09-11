package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.GastoVentaDTO;
import com.proyecto.terranova.service.GastoVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/GastoVentas")
public class GastoVentaControllerRest {

    @Autowired
    private GastoVentaService serviceGastoVenta;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<GastoVentaDTO>> obtenerTodosLosGastoVentas(){
        List<GastoVentaDTO> entidadesGastoVenta = serviceGastoVenta.findAll();
        return ResponseEntity.ok(entidadesGastoVenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoVentaDTO> obtenerGastoVentaPorId(@PathVariable Long id){
        GastoVentaDTO dtoGastoVenta = serviceGastoVenta.findById(id);
        return ResponseEntity.ok(dtoGastoVenta);
    }

    @PostMapping("/crearGastoVenta")
    public ResponseEntity<GastoVentaDTO> crearGastoVenta(@RequestBody GastoVentaDTO dtoGastoVenta){
        serviceGastoVenta.save(dtoGastoVenta);
        return ResponseEntity.ok(dtoGastoVenta);
    }

    @DeleteMapping("/eliminarGastoVenta/{id}")
    public ResponseEntity<Long> eliminarGastoVenta(@PathVariable Long id){
        serviceGastoVenta.delete(id);
        return ResponseEntity.ok(id);
    }
}
