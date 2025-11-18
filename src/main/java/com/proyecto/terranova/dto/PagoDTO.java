package com.proyecto.terranova.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagoDTO {

    private Long idPago;

    private VentaDTO venta;

    private Long idPaymentMercadoPago;

    private Long montoPagado;

    private String moneda;

    private String estado;

    private String metodoPago;

    private LocalDateTime fechaRegistroPago;
}
