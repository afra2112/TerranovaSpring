package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.UbicacionDTO;

public interface UbicacionService {
    UbicacionDTO save(UbicacionDTO dto);
    UbicacionDTO update(Long id, UbicacionDTO dto); // Actualizar
    UbicacionDTO findById(Long id);
    List<UbicacionDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
