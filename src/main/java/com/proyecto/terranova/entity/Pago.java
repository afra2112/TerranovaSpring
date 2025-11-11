package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "pagos")
@Data
@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    private Long idPaymentMercadoPago;

    private Long montoPagado;

    private String moneda;

    private String estado;

    private String metodoPago;

    private LocalDateTime fechaRegistroPago;
}
