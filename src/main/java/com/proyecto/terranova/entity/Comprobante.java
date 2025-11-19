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

    @OneToOne
    @JoinColumn(name = "id_info_comprobante")
    private InfoComprobante infoComprobante;
}
