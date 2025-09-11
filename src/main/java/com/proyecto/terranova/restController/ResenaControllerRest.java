package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.ResenaDTO;
import com.proyecto.terranova.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Resenas")
public class ResenaControllerRest {

    @Autowired
    private ResenaService serviceResena;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<ResenaDTO>> obtenerTodosLosResenas(){
        List<ResenaDTO> entidadesResena = serviceResena.findAll();
        return ResponseEntity.ok(entidadesResena);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaDTO> obtenerResenaPorId(@PathVariable Long id){
        ResenaDTO dtoResena = serviceResena.findById(id);
        return ResponseEntity.ok(dtoResena);
    }

    @PostMapping("/crearResena")
    public ResponseEntity<ResenaDTO> crearResena(@RequestBody ResenaDTO dtoResena){
        serviceResena.save(dtoResena);
        return ResponseEntity.ok(dtoResena);
    }

    @DeleteMapping("/eliminarResena/{id}")
    public ResponseEntity<Long> eliminarResena(@PathVariable Long id){
        serviceResena.delete(id);
        return ResponseEntity.ok(id);
    }
}
