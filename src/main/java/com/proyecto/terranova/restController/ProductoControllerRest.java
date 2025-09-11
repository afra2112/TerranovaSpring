package com.proyecto.terranova.restController;

import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.service.ProductoService;
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
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos(){
        List<ProductoDTO> entidadesProducto = serviceProducto.findAll();
        return ResponseEntity.ok(entidadesProducto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id){
        ProductoDTO dtoProducto = serviceProducto.findById(id);
        return ResponseEntity.ok(dtoProducto);
    }

    @PostMapping("/crearProducto")
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO dtoProducto){
        serviceProducto.save(dtoProducto);
        return ResponseEntity.ok(dtoProducto);
    }

    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<Long> eliminarProducto(@PathVariable Long id){
        serviceProducto.delete(id);
        return ResponseEntity.ok(id);
    }
}
