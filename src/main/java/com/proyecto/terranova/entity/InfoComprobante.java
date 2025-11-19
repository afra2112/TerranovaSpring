package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "infos_comprobantes")
public class InfoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInfoComprobante;

    @Enumerated(EnumType.STRING)
    private NombreComprobanteEnum nombreComprobante;

    private String nombreMostrar;

    private String descripcion;

    private String icono;

    private boolean obligatorio;

    @OneToOne(mappedBy = "infoComprobante")
    private Comprobante comprobante;

    @ManyToOne
    @JoinColumn(name = "id_venta_ganado")
    private VentaGanado ventaGanado;

    @ManyToOne
    @JoinColumn(name = "id_venta_terreno")
    private VentaTerreno ventaTerreno;

    @ManyToOne
    @JoinColumn(name = "id_venta_finca")
    private VentaFinca ventaFinca;
}
