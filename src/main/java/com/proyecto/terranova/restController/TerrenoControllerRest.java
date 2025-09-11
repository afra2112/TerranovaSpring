package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.TerrenoDTO;
import com.proyecto.terranova.service.TerrenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Terrenos")
public class TerrenoControllerRest {

    @Autowired
    private TerrenoService serviceTerreno;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<TerrenoDTO>> obtenerTodosLosTerrenos(){
        List<TerrenoDTO> entidadesTerreno = serviceTerreno.findAll();
        return ResponseEntity.ok(entidadesTerreno);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerrenoDTO> obtenerTerrenoPorId(@PathVariable Long id){
        TerrenoDTO dtoTerreno = serviceTerreno.findById(id);
        return ResponseEntity.ok(dtoTerreno);
    }

    @PostMapping("/crearTerreno")
    public ResponseEntity<TerrenoDTO> crearTerreno(@RequestBody TerrenoDTO dtoTerreno){
        serviceTerreno.save(dtoTerreno);
        return ResponseEntity.ok(dtoTerreno);
    }

    @DeleteMapping("/eliminarTerreno/{id}")
    public ResponseEntity<Long> eliminarTerreno(@PathVariable Long id){
        serviceTerreno.delete(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/editarTerreno/{id}")
    public  ResponseEntity<TerrenoDTO> editarUsuarioPorId(@PathVariable Long id, @RequestBody TerrenoDTO terrenoDTO){
        TerrenoDTO TerrenoActualizado = serviceTerreno.update(id, terrenoDTO);
        return ResponseEntity.ok(TerrenoActualizado);
    }
}
