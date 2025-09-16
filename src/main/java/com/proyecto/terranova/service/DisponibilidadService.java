package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;

public interface DisponibilidadService {
    Disponibilidad save(Disponibilidad disponibilidad);
    DisponibilidadDTO update(Long id, DisponibilidadDTO dto); // Actualizar
    DisponibilidadDTO findById(Long id);
    List<Disponibilidad> findAll();
    List<DisponibilidadDTO> encontrarTodasPorVendedor(Usuario vendedor);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
