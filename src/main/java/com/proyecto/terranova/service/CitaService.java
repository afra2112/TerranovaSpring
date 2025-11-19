package com.proyecto.terranova.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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
    List<Cita> encontrarPorProducto(Long idProducto);
    List<CitaDTO> encontrarPorVendedorParaCalendario(Usuario vendedor, boolean activo);
    //List<Cita> encontrarPorCompradorYEstado(Usuario comprador, EstadoCitaEnum estadoCitaEnum);
    List<Cita> encontrarPorEstado(Usuario vendedor,EstadoCitaEnum estado, boolean activo);
    boolean delete(Long id);
    boolean existsById(Long id);
    long contarPorVendedor(Usuario vendedor);
    void cambiarEstado(Cita cita, EstadoCitaEnum estado);
    void borrarCita(Long idCita);
    void reprogramarCita(Long idCita, LocalDate nuevaFecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) throws IOException;
    void cancelarCita(Long idCita);
    void finalizarCita(Long idCita, Map<String, String> params);
}
