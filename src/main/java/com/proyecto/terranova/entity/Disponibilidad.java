package com.proyecto.terranova.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidades")
@Data
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisponibilidad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    private boolean disponible = true;

    @Nullable
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cedula", nullable = false)
    private Usuario usuario;
}
