package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.TipoArchivoTransporteEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "archivos_transportes")
public class ArchivosTransportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArchivosTransporte;

    @ManyToOne
    @JoinColumn(name = "id_transporte")
    private Transporte transporte;

    private String nombreArchivo;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;

    @Enumerated(EnumType.STRING)
    private TipoArchivoTransporteEnum tipoArchivo;

    @Column(length = 200)
    private String descripcion;
}