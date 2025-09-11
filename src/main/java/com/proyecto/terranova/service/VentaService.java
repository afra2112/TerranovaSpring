package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.VentaDTO;

public interface VentaService {
    VentaDTO save(VentaDTO dto);
    VentaDTO update(Long id, VentaDTO dto); // Actualizar
    VentaDTO findById(Long id);
    List<VentaDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
