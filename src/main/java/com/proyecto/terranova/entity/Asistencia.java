package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsistencia;

    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "cedula_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoAsistenciaEnum estado;

    private LocalDateTime fechaInscripcion;

    private boolean asistio;
}
