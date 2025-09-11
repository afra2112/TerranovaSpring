package com.proyecto.terranova.dto;

import lombok.Data;

@Data
public class GastoVentaDTO {

    private Long idGasto;

    private String nombreGasto;

    private Long valorGasto;

    private Long idVenta;
}
