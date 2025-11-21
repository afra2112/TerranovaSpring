package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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