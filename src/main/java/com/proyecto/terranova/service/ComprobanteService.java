package com.proyecto.terranova.service;

import java.util.List;
import java.util.Map;

import com.proyecto.terranova.dto.ComprobanteDTO;
import com.proyecto.terranova.entity.Comprobante;
import com.proyecto.terranova.entity.Venta;

public interface ComprobanteService {
    Comprobante save(Comprobante comprobante);
    ComprobanteDTO update(Long id, ComprobanteDTO dto); // Actualizar
    ComprobanteDTO findById(Long id);
    List<ComprobanteDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id);
    long count();
}
