package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(length = 30, nullable = false)
    private String nombreProducto;

    @Column(length = 30, nullable = false)
    private String tipoProducto;

    @Column(length = 20, nullable = false)
    private Long precioProducto;

    @Column(length = 255, nullable = false)
    private String descripcion;

    @Column(length = 20, nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "cedulaVendedor", nullable = false)
    private Usuario vendedor;

    @OneToOne
    @JoinColumn(name = "idUbicacion")
    private Ubicacion ubicacion;

    @OneToMany(mappedBy = "producto")
    private List<Imagen> imagenes;

    @OneToMany(mappedBy = "producto",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilidad> disponibilidades = new ArrayList<>();
}
