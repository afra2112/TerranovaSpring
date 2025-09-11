package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ganados")
@Data
public class Ganado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGanado;

    @Column(nullable = false, length = 30)
    private String razaGanado;

    @Column(nullable = false)
    private int pesoGanado;

    @Column(nullable = false)
    private int edadGanado;

    @Column(nullable = false)
    private String generoGanado; //ENUM('Macho', 'Hembra')

    @Column(nullable = false, length = 30)
    private String tipoGanado;

    @Column(nullable = false)
    private boolean prenez;

    @Column(nullable = false)
    private String estadoSanitario;

    @Column(nullable = false)
    private int cantidad;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
}
