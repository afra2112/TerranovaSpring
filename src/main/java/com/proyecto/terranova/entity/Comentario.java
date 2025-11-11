package com.proyecto.terranova.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 500)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaComentario = LocalDateTime.now();
}

