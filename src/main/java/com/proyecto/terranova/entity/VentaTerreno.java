package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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

    @OneToMany(mappedBy = "ventaTerreno")
    @MapKey(name = "nombreComprobante")
    private Map<NombreComprobanteEnum, InfoComprobante> comprobantesInfo = new HashMap<>();
}
