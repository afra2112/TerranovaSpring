package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "terrenos")
@Data
public class Terreno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTerreno;

    @Column(nullable = false)
    private double tamanoTerreno;

    @Column(nullable = false)
    private String tipoTerreno; //ENUM('Carretera', 'Trocha', 'Camino')

    @Column(nullable = false)
    private String topografiaTerreno;

    @Column(nullable = false)
    private String acceso; //ENUM('Carretera', 'Trocha', 'Camino')

    @Column(nullable = false)
    private String servicios;

    @Column(nullable = false)
    private String usoActual;

    @Column(nullable = false)
    private boolean cercado;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;
}
