package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Producto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaDTO {

    private Long idCita;

    private Long idDisponibilidad;

    private Long idProducto;

    private EstadoCitaEnum estadoCita;

    private LocalDate fecha;

    private LocalTime hora;

    private String nombreProducto;

    private String nombreComprador;

    private String nombreVendedor;

    private String ubicacion;
}
