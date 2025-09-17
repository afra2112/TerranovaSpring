package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;

public interface ProductoService {
    Producto crearProductoBase(ProductoDTO productoDTO);
    ProductoDTO save(ProductoDTO dto);
    ProductoDTO update(Long id, ProductoDTO dto); // Actualizar
    Producto findById(Long id);
    List<ProductoDTO> findAll();
    List<Producto> obtenerTodosPorVendedor(Usuario vendedor);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
