package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import lombok.Data;

@Data
public class InfoComprobanteDTO {
    private Long idInfoComprobante;

    private NombreComprobanteEnum nombreComprobante;

    private String nombreMostrar;

    private String descripcion;

    private String icono;

    private boolean obligatorio;
}