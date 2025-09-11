package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.TerrenoDTO;

public interface TerrenoService {
    TerrenoDTO save(TerrenoDTO dto);
    TerrenoDTO update(Long id, TerrenoDTO dto); // Actualizar
    TerrenoDTO findById(Long id);
    List<TerrenoDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
