package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citas")
@Data
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @Enumerated(EnumType.STRING)
    private EstadoCitaEnum estadoCita;

    private int cupoMaximo;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencias = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @Column(nullable = true)
    private LocalDateTime ultimaReprogramacion;

    @Column(nullable = true)
    private LocalDateTime ultimaReprogramacionBloqueada;

    @Column(nullable = true)
    private LocalDateTime fechaHabilitarReprogramacion;

    private boolean activo = true;

    private int numReprogramaciones = 0;

    private int ocupados;
    private int disponibles;

    @OneToOne
    @JoinColumn(name = "id_disponibilidad")
    private Disponibilidad disponibilidad;

    public boolean isFechaDisponibleParaFinalizar() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaCita = LocalDateTime.of(
                this.disponibilidad.getFecha(),
                this.disponibilidad.getHoraFin()
        );
        return !fechaCita.isAfter(ahora);
    }
}
