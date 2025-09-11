package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "fincas")
@Data
public class Finca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFinca;

    @Column(nullable = false, length = 30)
    private String espacioTotal;

    @Column(nullable = false, length = 30)
    private String espacioConstruido;

    @Column(nullable = false)
    private int estratoFinca;

    @Column(nullable = false)
    private int numeroHabitaciones;

    @Column(nullable = false)
    private int numeroBanos;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
}
