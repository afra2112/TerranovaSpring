package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.GastoVentaDTO;

public interface GastoVentaService {
    GastoVentaDTO save(GastoVentaDTO dto);
    GastoVentaDTO update(Long id, GastoVentaDTO dto); // Actualizar
    GastoVentaDTO findById(Long id);
    List<GastoVentaDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
