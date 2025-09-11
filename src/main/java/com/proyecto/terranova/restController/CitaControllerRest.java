package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Citas")
public class CitaControllerRest {

    @Autowired
    private CitaService serviceCita;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<CitaDTO>> obtenerTodosLosCitas(){
        List<CitaDTO> entidadesCita = serviceCita.findAll();
        return ResponseEntity.ok(entidadesCita);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerCitaPorId(@PathVariable Long id){
        CitaDTO dtoCita = serviceCita.findById(id);
        return ResponseEntity.ok(dtoCita);
    }

    @PostMapping("/crearCita")
    public ResponseEntity<CitaDTO> crearCita(@RequestBody CitaDTO dtoCita){
        serviceCita.save(dtoCita);
        return ResponseEntity.ok(dtoCita);
    }

    @DeleteMapping("/eliminarCita/{id}")
    public ResponseEntity<Long> eliminarCita(@PathVariable Long id){
        serviceCita.delete(id);
        return ResponseEntity.ok(id);
    }
}
