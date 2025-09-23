package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ganados")
@Data
@NoArgsConstructor
@DiscriminatorValue("GANADO")
public class Ganado extends Producto {

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
    private String estadoSanitario;

    @Column(nullable = false)
    private int cantidad;

}
