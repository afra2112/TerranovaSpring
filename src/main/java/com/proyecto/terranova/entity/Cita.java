package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Data
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @Column(nullable = false)
    private LocalDate fechaCita;

    @Column(nullable = false, length = 30)
    private EstadoCitaEnum estadoCita;

    @Column(nullable = false)
    private LocalTime horaCita;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "cedula_comprador")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "id_disponibilidad")
    private Disponibilidad disponibilidad;

    @OneToOne
    @JoinColumn(name = "idUbicacion")
    private Ubicacion ubicacion;
}
