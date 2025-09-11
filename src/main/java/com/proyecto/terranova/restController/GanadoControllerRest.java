package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.GanadoDTO;
import com.proyecto.terranova.service.GanadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Ganados")
public class GanadoControllerRest {

    @Autowired
    private GanadoService serviceGanado;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<GanadoDTO>> obtenerTodosLosGanados(){
        List<GanadoDTO> entidadesGanado = serviceGanado.findAll();
        return ResponseEntity.ok(entidadesGanado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GanadoDTO> obtenerGanadoPorId(@PathVariable Long id){
        GanadoDTO dtoGanado = serviceGanado.findById(id);
        return ResponseEntity.ok(dtoGanado);
    }

    @PostMapping("/crearGanado")
    public ResponseEntity<GanadoDTO> crearGanado(@RequestBody GanadoDTO dtoGanado){
        serviceGanado.save(dtoGanado);
        return ResponseEntity.ok(dtoGanado);
    }

    @DeleteMapping("/eliminarGanado/{id}")
    public ResponseEntity<Long> eliminarGanado(@PathVariable Long id){
        serviceGanado.delete(id);
        return ResponseEntity.ok(id);
    }
}
