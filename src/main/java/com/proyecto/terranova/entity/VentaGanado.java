package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "ventas_ganados")
public class VentaGanado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVentaGanado;

    private String condicionesEntrega;

    private String observacionesSanitarias;

    private LocalDate fechaTransporte;

    private LocalTime horaTransporte;

    private String puntoEntrega;
}
