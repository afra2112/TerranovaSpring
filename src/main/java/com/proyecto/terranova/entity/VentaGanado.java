package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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

    @OneToMany(mappedBy = "ventaGanado")
    @MapKey(name = "nombreComprobante")
    private Map<NombreComprobanteEnum, InfoComprobante> comprobantesInfo = new HashMap<>();
}
