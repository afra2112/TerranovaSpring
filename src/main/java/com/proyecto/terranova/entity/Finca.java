package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fincas")
@Data
@NoArgsConstructor
@DiscriminatorValue("FINCA")
public class Finca extends Producto {

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

}
