package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "venta_terreno")
@Entity
public class VentaTerreno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVentaTerreno;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;
}
