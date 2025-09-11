package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.service.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Disponibilidades")
public class DisponibilidadControllerRest {

    @Autowired
    private DisponibilidadService serviceDisponibilidad;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<DisponibilidadDTO>> obtenerTodosLosDisponibilidads(){
        List<DisponibilidadDTO> entidadesDisponibilidad = serviceDisponibilidad.findAll();
        return ResponseEntity.ok(entidadesDisponibilidad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> obtenerDisponibilidadPorId(@PathVariable Long id){
        DisponibilidadDTO dtoDisponibilidad = serviceDisponibilidad.findById(id);
        return ResponseEntity.ok(dtoDisponibilidad);
    }

    @PostMapping("/crearDisponibilidad")
    public ResponseEntity<DisponibilidadDTO> crearDisponibilidad(@RequestBody DisponibilidadDTO dtoDisponibilidad){
        serviceDisponibilidad.save(dtoDisponibilidad);
        return ResponseEntity.ok(dtoDisponibilidad);
    }

    @DeleteMapping("/eliminarDisponibilidad/{id}")
    public ResponseEntity<Long> eliminarDisponibilidad(@PathVariable Long id){
        serviceDisponibilidad.delete(id);
        return ResponseEntity.ok(id);
    }
}
