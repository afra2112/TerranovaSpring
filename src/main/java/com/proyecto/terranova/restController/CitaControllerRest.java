package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Citas")
public class CitaControllerRest {

    @Autowired
    private CitaService serviceCita;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/vendedor/{cedula}")
    public List<CitaDTO> obtenerPorVendedor(@PathVariable(name = "cedula") String cedula){
        return serviceCita.encontrarPorVendedorParaCalendario(usuarioService.findById(cedula), true);
    }

    @GetMapping("/listarTodo")
    public ResponseEntity<List<Cita>> obtenerTodosLosCitas(){
        List<Cita> entidadesCita = serviceCita.findAll();
        return ResponseEntity.ok(entidadesCita);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id){
        Cita dtoCita = serviceCita.findById(id);
        return ResponseEntity.ok(dtoCita);
    }

    @PostMapping("/crearCita")
    public ResponseEntity<Cita> crearCita(@RequestBody Cita dtoCita){
        serviceCita.save(dtoCita);
        return ResponseEntity.ok(dtoCita);
    }

    @DeleteMapping("/eliminarCita/{id}")
    public ResponseEntity<Long> eliminarCita(@PathVariable Long id){
        serviceCita.delete(id);
        return ResponseEntity.ok(id);
    }
}
