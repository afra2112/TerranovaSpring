package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula_comprador", nullable = false)
    private Usuario comprador;

    @Column(nullable = false, length = 500)
    private String contenido;

    private LocalDateTime fechaComentario = LocalDateTime.now();
}
