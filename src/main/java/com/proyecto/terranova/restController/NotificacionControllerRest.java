package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Notificaciones")
public class NotificacionControllerRest {

    @Autowired
    private NotificacionService serviceNotificacion;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<NotificacionDTO>> obtenerTodosLosNotificacions(){
        List<NotificacionDTO> entidadesNotificacion = serviceNotificacion.findAll();
        return ResponseEntity.ok(entidadesNotificacion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> obtenerNotificacionPorId(@PathVariable Long id){
        NotificacionDTO dtoNotificacion = serviceNotificacion.findById(id);
        return ResponseEntity.ok(dtoNotificacion);
    }

    @PostMapping("/crearNotificacion")
    public ResponseEntity<NotificacionDTO> crearNotificacion(@RequestBody NotificacionDTO dtoNotificacion){
        serviceNotificacion.save(dtoNotificacion);
        return ResponseEntity.ok(dtoNotificacion);
    }

    @DeleteMapping("/eliminarNotificacion/{id}")
    public ResponseEntity<Long> eliminarNotificacion(@PathVariable Long id){
        serviceNotificacion.delete(id);
        return ResponseEntity.ok(id);
    }


}
