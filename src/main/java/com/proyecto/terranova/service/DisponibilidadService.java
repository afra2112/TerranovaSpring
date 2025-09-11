package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.DisponibilidadDTO;

public interface DisponibilidadService {
    DisponibilidadDTO save(DisponibilidadDTO dto);
    DisponibilidadDTO update(Long id, DisponibilidadDTO dto); // Actualizar
    DisponibilidadDTO findById(Long id);
    List<DisponibilidadDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
