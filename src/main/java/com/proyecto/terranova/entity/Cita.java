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

    @Enumerated(EnumType.STRING)
    private EstadoCitaEnum estadoCita;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "cedula_comprador")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "id_disponibilidad")
    private Disponibilidad disponibilidad;
}
