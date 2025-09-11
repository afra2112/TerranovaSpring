package com.proyecto.terranova.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    @Column(nullable = false, length = 255)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cedula")
    private Usuario comprador;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
}
