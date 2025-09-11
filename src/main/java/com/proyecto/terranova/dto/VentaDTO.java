package com.proyecto.terranova.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VentaDTO {

    private Long idVenta;

    private LocalDateTime fechaVenta;

    private String estado;

    private String nota;

    private String metodoPago;

    private Long gananciaNeta;

    private Long idProducto;

    private String cedulaComprador;

    private String cedulaVendedor;
}
