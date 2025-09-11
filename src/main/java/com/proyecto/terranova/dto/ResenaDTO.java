package com.proyecto.terranova.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResenaDTO {

    private Long idResena;

    private String comentario;

    private LocalDateTime fecha;

    private String cedulaComprador;

    private Long idProducto;
}
