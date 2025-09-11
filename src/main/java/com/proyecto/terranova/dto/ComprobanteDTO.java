package com.proyecto.terranova.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComprobanteDTO {

    private Long idComprobante;

    private String nombreComprobante;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;

    private Long idVenta;
}
