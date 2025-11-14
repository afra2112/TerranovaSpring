package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "venta_finca")
@Entity
public class VentaFinca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVentaFinca;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;
}