package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.FincaDTO;
import com.proyecto.terranova.service.FincaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Fincas")
public class FincaControllerRest {

    @Autowired
    private FincaService serviceFinca;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<FincaDTO>> obtenerTodosLosFincas(){
        List<FincaDTO> entidadesFinca = serviceFinca.findAll();
        return ResponseEntity.ok(entidadesFinca);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FincaDTO> obtenerFincaPorId(@PathVariable Long id){
        FincaDTO dtoFinca = serviceFinca.findById(id);
        return ResponseEntity.ok(dtoFinca);
    }

    @PostMapping("/crearFinca")
    public ResponseEntity<FincaDTO> crearFinca(@RequestBody FincaDTO dtoFinca){
        serviceFinca.save(dtoFinca);
        return ResponseEntity.ok(dtoFinca);
    }

    @DeleteMapping("/eliminarFinca/{id}")
    public ResponseEntity<Long> eliminarFinca(@PathVariable Long id){
        serviceFinca.delete(id);
        return ResponseEntity.ok(id);
    }
}
