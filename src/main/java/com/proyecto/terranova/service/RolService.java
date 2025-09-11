package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.RolDTO;

public interface RolService {
    RolDTO save(RolDTO dto);
    RolDTO update(Long id, RolDTO dto); // Actualizar
    RolDTO findById(Long id);
    List<RolDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
