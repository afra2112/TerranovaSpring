package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.ImagenDTO;
import com.proyecto.terranova.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Imagenes")
public class ImagenControllerRest {

    @Autowired
    private ImagenService serviceImagen;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<ImagenDTO>> obtenerTodosLosImagens(){
        List<ImagenDTO> entidadesImagen = serviceImagen.findAll();
        return ResponseEntity.ok(entidadesImagen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> obtenerImagenPorId(@PathVariable Long id){
        ImagenDTO dtoImagen = serviceImagen.findById(id);
        return ResponseEntity.ok(dtoImagen);
    }

    @PostMapping("/crearImagen")
    public ResponseEntity<ImagenDTO> crearImagen(@RequestBody ImagenDTO dtoImagen){
        serviceImagen.save(dtoImagen);
        return ResponseEntity.ok(dtoImagen);
    }

    @DeleteMapping("/eliminarImagen/{id}")
    public ResponseEntity<Long> eliminarImagen(@PathVariable Long id){
        serviceImagen.delete(id);
        return ResponseEntity.ok(id);
    }
}
