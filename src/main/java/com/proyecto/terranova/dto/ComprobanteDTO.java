package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComprobanteDTO {

    private Long idComprobante;

    private NombreComprobanteEnum nombreComprobante;

    private InfoComprobanteDTO infoComprobante;

    private String nombreMostrar;

    private String descripcion;

    private String icono;

    private boolean obligatorio;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;
}
