package com.proyecto.terranova.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disponibilidades")
@Data
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisponibilidad;

    @Column(nullable = false)
    private LocalDate fecha;

    private LocalTime horaFin;

    private LocalTime horaInicio;

    private boolean disponible = true;

    @Nullable
    private String descripcion;

    @OneToOne(mappedBy = "disponibilidad", cascade = CascadeType.ALL)
    private Cita cita;
}