package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.ResenaDTO;

public interface ResenaService {
    ResenaDTO save(ResenaDTO dto);
    ResenaDTO update(Long id, ResenaDTO dto); // Actualizar
    ResenaDTO findById(Long id);
    List<ResenaDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
