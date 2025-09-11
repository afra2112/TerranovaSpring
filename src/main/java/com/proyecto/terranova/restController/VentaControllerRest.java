package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Ventas")
public class VentaControllerRest {

    @Autowired
    private VentaService serviceVenta;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<VentaDTO>> obtenerTodosLosVentas(){
        List<VentaDTO> entidadesVenta = serviceVenta.findAll();
        return ResponseEntity.ok(entidadesVenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id){
        VentaDTO dtoVenta = serviceVenta.findById(id);
        return ResponseEntity.ok(dtoVenta);
    }

    @PostMapping("/crearVenta")
    public ResponseEntity<VentaDTO> crearVenta(@RequestBody VentaDTO dtoVenta){
        serviceVenta.save(dtoVenta);
        return ResponseEntity.ok(dtoVenta);
    }

    @DeleteMapping("/eliminarVenta/{id}")
    public ResponseEntity<Long> eliminarVenta(@PathVariable Long id){
        serviceVenta.delete(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/editarVenta/{id}")
    public  ResponseEntity<VentaDTO> editarUsuarioPorId(@PathVariable Long id, @RequestBody VentaDTO ventaDTO){
        VentaDTO ventaActualizada = serviceVenta.update(id, ventaDTO);
        return ResponseEntity.ok(ventaActualizada);
    }
}
