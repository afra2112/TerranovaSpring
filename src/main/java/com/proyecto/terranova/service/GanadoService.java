package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.GanadoDTO;

public interface GanadoService {
    GanadoDTO save(GanadoDTO dto);
    GanadoDTO update(Long id, GanadoDTO dto); // Actualizar
    GanadoDTO findById(Long id);
    List<GanadoDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
