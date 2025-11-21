package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "comprobantes")
@Data
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;

    @ManyToOne
    @JoinColumn(name = "id_info_comprobante")
    private InfoComprobante infoComprobante;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;
}
