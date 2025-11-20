package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.EstadoTransporteEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "transportes")
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransporte;

    private LocalDate fechaTransporte;

    private LocalTime horaTransporte;

    private String puntoEntrega;

    private String empresaTransporte;

    @Column(length = 20)
    private String placaVehiculo;

    private String nombreConductor;

    private String telefonoConductor;

    private String cedulaConductor;

    @Column(length = 500)
    private String observacionesComprador;

    @Column(length = 500)
    private String observacionesVendedor;

    @Enumerated(EnumType.STRING)
    private EstadoTransporteEnum estadoTransporte;

    private LocalDateTime fechaHoraCarga;

    private LocalDateTime fechaHoraEntrega;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @OneToMany(mappedBy = "transporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArchivosTransportes> archivosTransporte = new ArrayList<>();
}