package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "gastosVentas")
@Data
public class GastoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGasto;

    @Column(nullable = false, length = 30)
    private String nombreGasto;

    @Column(nullable = false)
    private Long valorGasto;

    @ManyToOne
    @JoinColumn(name = "idVenta")
    private Venta venta;
}
