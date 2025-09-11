package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.CiudadesEnum;
import com.proyecto.terranova.config.enums.DepartamentoEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ubicaciones")
@Data
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUbicacion;

    @Column(length = 50, nullable = false)
    private DepartamentoEnum departamento;

    @Column(length = 50, nullable = false)
    private CiudadesEnum ciudad;

    @Column(length = 255, nullable = true)
    private String detalles;
}
