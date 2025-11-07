package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Asistencia;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CitaDTO {

    private Long idCita;

    private Long idDisponibilidad;

    private ProductoDTO productoDTO;

    private EstadoCitaEnum estadoCita;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private int cupoMaximo;

    private List<AsistenciaDTO> asistenciasDTO;

    private String nombreProducto;

    private String nombreVendedor;

    private String ubicacion;
}
