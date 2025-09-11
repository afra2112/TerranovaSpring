package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.RolDTO;
import com.proyecto.terranova.repository.RolRepository;
import com.proyecto.terranova.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Roles")
public class RolControllerRest {

    @Autowired
    private RolService serviceRol;

    @Autowired
    private RolRepository repositoryRol;

    @GetMapping("/listarTodo")
    public ResponseEntity<List<RolDTO>> obtenerTodosLosRols(){
        List<RolDTO> entidadesRol = serviceRol.findAll();
        return ResponseEntity.ok(entidadesRol);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerRolPorId(@PathVariable Long id){
        RolDTO dtoRol = serviceRol.findById(id);
        return ResponseEntity.ok(dtoRol);
    }

    @PostMapping("/crearRol")
    public ResponseEntity<RolDTO> crearRol(@RequestBody RolDTO dtoRol){
        serviceRol.save(dtoRol);
        return ResponseEntity.ok(dtoRol);
    }

    @DeleteMapping("/eliminarRol/{id}")
    public ResponseEntity<Long> eliminarRol(@PathVariable Long id){
        serviceRol.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<List<String>> consultarRolesPorCedula(@PathVariable String cedula){
        List<String> listaRoles = repositoryRol.findByRolesAndCedula(cedula);
        return ResponseEntity.ok(listaRoles);
    }

    @PutMapping("/editarRol/{id}")
    public  ResponseEntity<RolDTO> editarUsuarioPorId(@PathVariable Long id, @RequestBody RolDTO rolDTO){
        RolDTO rolActualizado = serviceRol.update(id, rolDTO);
        return ResponseEntity.ok(rolActualizado);
    }
}
