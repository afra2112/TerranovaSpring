package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ventas_ganados")
public class VentaGanado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVentaGanado;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    private String observacionesSanitarias;

    private Long precioNegociado;

    private int cantidadNegociada;
}
