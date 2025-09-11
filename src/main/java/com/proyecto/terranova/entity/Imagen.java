package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "imagenes")
@Data
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagen;

    @Column(nullable = false, length = 255)
    private String nombreArchivo;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
}
