package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaDTO {

    private Long idCita;

    private LocalDate fechaCita;

    private EstadoCitaEnum estadoCita;

    private LocalTime horaCita;

    private Long idProducto;

    private String cedulaComprador;

    private String cedulaVendedor;

    private Long idUbicacion;
}
