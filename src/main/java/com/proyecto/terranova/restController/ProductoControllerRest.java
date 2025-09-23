package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Productos")
public class ProductoControllerRest {

    @Autowired
    private ProductoService serviceProducto;


    @GetMapping("/listarTodo")
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos(){
        List<Producto> entidadesProducto = serviceProducto.findAll();
        return ResponseEntity.ok(entidadesProducto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id){
        Producto dtoProducto = serviceProducto.findById(id);
        return ResponseEntity.ok(dtoProducto);
    }



    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<Long> eliminarProducto(@PathVariable Long id){
        serviceProducto.delete(id);
        return ResponseEntity.ok(id);
    }
}
