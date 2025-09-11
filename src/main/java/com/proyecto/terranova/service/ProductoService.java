package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.ProductoDTO;

public interface ProductoService {
    ProductoDTO save(ProductoDTO dto);
    ProductoDTO update(Long id, ProductoDTO dto); // Actualizar
    ProductoDTO findById(Long id);
    List<ProductoDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
