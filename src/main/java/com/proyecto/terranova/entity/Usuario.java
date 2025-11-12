package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Usuarios")
@Data
public class Usuario {

    @Id
    @Column(length = 10, nullable = false)
    private String cedula;

    @Column(length = 45, nullable = false)
    private String nombres;

    @Column(length = 45, nullable = false)
    private String apellidos;

    @Email
    @NotBlank
    private String email;

    private String contrasena;

    @Column(length = 10, nullable = false)
    private String telefono;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nacimiento;

    private LocalDate fechaRegistro;

    private String foto;

    private boolean notificacionesDisponibilidades = true;

    private boolean notificacionesCitas = true;

    private boolean notificacionesVentas = true;

    private boolean notificacionesProductos = true;

    private boolean notificacionesSistema = true;

    private boolean recibirCorreos = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "cedula"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private List<Rol> roles;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos;

    @OneToMany(mappedBy = "vendedor")
    private List<Producto> disponibilidad;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistencia> asistencias;

    private int puntuacionUsuario;


    //estos campos son para el oauth2
    private String provider;
    private String providerId;

    private String resetToken;
    private LocalDateTime resetTokenExpiracion;

    private String codigoVerificacion;
    private LocalDateTime codigoVerificacionExpiracion;

}
