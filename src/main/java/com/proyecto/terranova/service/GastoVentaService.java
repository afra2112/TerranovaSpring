package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.GastoVentaDTO;
import com.proyecto.terranova.entity.GastoVenta;

public interface GastoVentaService {
    GastoVenta save(Long idVenta, String nombreGasto, Long valorGasto);
    GastoVentaDTO update(Long id, GastoVentaDTO dto); // Actualizar
    GastoVentaDTO findById(Long id);
    List<GastoVentaDTO> findAll();
    void delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
