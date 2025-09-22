package com.proyecto.terranova.service;

import java.util.List;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;

public interface CitaService {
    Cita save(Cita dto);
    CitaDTO update(Long id, CitaDTO dto); // Actualizar
    Cita findById(Long id);
    List<Cita> findAll();
    List<Cita> encontrarPorVendedor(Usuario vendedor, boolean activo);
    List<CitaDTO> encontrarPorVendedorParaCalendario(Usuario vendedor, boolean activo);
    List<Cita> encontrarPorComprador(Usuario comprador, boolean activo);
    List<Cita> encontrarPorEstado(Usuario vendedor,EstadoCitaEnum estado, boolean activo);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
    void cambiarEstado(Cita cita, EstadoCitaEnum estado);
    void borrarCita(Long idCita);
}
