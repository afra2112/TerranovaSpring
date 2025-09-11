package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.ComprobanteDTO;

public interface ComprobanteService {
    ComprobanteDTO save(ComprobanteDTO dto);
    ComprobanteDTO update(Long id, ComprobanteDTO dto); // Actualizar
    ComprobanteDTO findById(Long id);
    List<ComprobanteDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
