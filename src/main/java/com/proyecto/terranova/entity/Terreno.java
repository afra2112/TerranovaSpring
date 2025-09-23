package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "terrenos")
@Data
@DiscriminatorValue("TERRENO")
public class Terreno extends Producto {

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

}
