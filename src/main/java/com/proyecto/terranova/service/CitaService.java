package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.CitaDTO;

public interface CitaService {
    CitaDTO save(CitaDTO dto);
    CitaDTO update(Long id, CitaDTO dto); // Actualizar
    CitaDTO findById(Long id);
    List<CitaDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
