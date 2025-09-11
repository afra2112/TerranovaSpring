package com.proyecto.terranova.dto;

import lombok.Data;

@Data
public class TerrenoDTO {

    private Long idTerreno;

    private double tamanoTerreno;

    private String tipoTerreno; //ENUM('Carretera', 'Trocha', 'Camino')

    private String topografiaTerreno;

    private String acceso; //ENUM('Carretera', 'Trocha', 'Camino')

    private String servicios;

    private String usoActual;

    private boolean cercado;

    private Long idProducto;
}
