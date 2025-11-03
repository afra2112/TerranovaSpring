package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    private String titulo;

    @Column(nullable = false, length = 255)
    private String mensajeNotificacion;

    @Column(nullable = false)
    private LocalDateTime fechaNotificacion;

    @Column(nullable = false, length = 55)
    private String tipo;

    @Column(nullable = false)
    private boolean leido;

    private boolean activo = true;

    private String urlAccion;

    private Long referenciaId;

    @ManyToOne
    @JoinColumn(name = "cedula")
    private Usuario usuario;
}
