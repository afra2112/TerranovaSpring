package com.proyecto.terranova.dto;

import lombok.Data;

@Data
public class GanadoDTO {

    private Long idGanado;

    private String razaGanado;

    private int pesoGanado;

    private int edadGanado;

    private String generoGanado; //ENUM('Macho', 'Hembra')

    private String tipoGanado;

    private boolean prenez;

    private String estadoSanitario;

    private int cantidad;

    private Long idProducto;
}
