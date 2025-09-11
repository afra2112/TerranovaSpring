package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.FincaDTO;

public interface FincaService {
    FincaDTO save(FincaDTO dto);
    FincaDTO update(Long id, FincaDTO dto); // Actualizar
    FincaDTO findById(Long id);
    List<FincaDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
