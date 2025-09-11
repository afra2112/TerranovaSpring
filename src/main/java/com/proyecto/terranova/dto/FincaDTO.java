package com.proyecto.terranova.dto;

import lombok.Data;

@Data
public class FincaDTO {

    private Long idFinca;

    private String espacioTotal;

    private String espacioConstruido;

    private int estratoFinca;

    private int numeroHabitaciones;

    private int numeroBanos;

    private Long idProducto;
}
