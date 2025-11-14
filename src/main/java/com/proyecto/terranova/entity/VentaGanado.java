package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private String condicionesEntrega;

    private String observacionesSanitarias;

    private LocalDate fechaTransporte;

    private LocalTime horaTransporte;

    private String puntoEntrega;

    private String empresaTransporte;

    private String archivoGSMI;

    private String archivoCertificadoSanitario;

    private String archivoPruebas;

    private String archivoInventario;
}
