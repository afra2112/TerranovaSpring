package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "cedula"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private List<Rol> roles;

    @ManyToMany
    @JoinTable(
            name = "usuarios_productos",
            joinColumns = @JoinColumn(name = "cedula"),
            inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    private List<Producto> favoritos;

    @OneToMany(mappedBy = "usuario")
    private List<Disponibilidad> disponibilidad;

    //estos campos son para el oauth2
    private String provider;
    private String providerId;
}
