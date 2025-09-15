package com.proyecto.terranova.service;

import java.util.List;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.entity.Cita;

public interface CitaService {
    CitaDTO save(CitaDTO dto);
    CitaDTO update(Long id, CitaDTO dto); // Actualizar
    CitaDTO findById(Long id);
    List<CitaDTO> findAll();
    List<Cita> encontrarPorEstado(EstadoCitaEnum estado);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
