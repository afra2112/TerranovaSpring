package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Producto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CitaDTO {

    private Long idCita;

    private Long idDisponibilidad;

    private Long idProducto;

    private EstadoCitaEnum estadoCita;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private int cupoMaximo;

    private List<Long> idsAsistencias;

    private String nombreProducto;


    private String nombreVendedor;

    private String ubicacion;
}
