package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Disponibilidades")
public class DisponibilidadControllerRest {

    @Autowired
    private DisponibilidadService serviceDisponibilidad;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<Disponibilidad>> obtenerTodosLosDisponibilidads(){
        List<Disponibilidad> entidadesDisponibilidad = serviceDisponibilidad.findAll();
        return ResponseEntity.ok(entidadesDisponibilidad);
    }

    @GetMapping("/vendedor/{cedula}")
    public List<DisponibilidadDTO> obtenerPorVendedorYDisponibleParaMostrar(@PathVariable(name = "cedula") String cedula){
        return serviceDisponibilidad.encontrarTodasPorVendedorYDisponible(usuarioService.findById(cedula), true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> obtenerDisponibilidadPorId(@PathVariable Long id){
        Disponibilidad dtoDisponibilidad = serviceDisponibilidad.findById(id);
        return ResponseEntity.ok(dtoDisponibilidad);
    }

    @PostMapping("/crearDisponibilidad")
    public ResponseEntity<Disponibilidad> crearDisponibilidad(@RequestBody Disponibilidad disponibilidad){
        serviceDisponibilidad.save(disponibilidad);
        return ResponseEntity.ok(disponibilidad);
    }

    @DeleteMapping("/eliminarDisponibilidad/{id}")
    public ResponseEntity<Long> eliminarDisponibilidad(@PathVariable Long id){
        serviceDisponibilidad.delete(id);
        return ResponseEntity.ok(id);
    }
}
