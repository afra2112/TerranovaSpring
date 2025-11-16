package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Favoritos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cedula", "id_producto"})
})
@Data
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula_usuario", referencedColumnName = "cedula", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", referencedColumnName = "idProducto", nullable = false)
    private Producto producto;

    private LocalDateTime fechaFavorito ;

}
