package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    private String estado; //ENUM ('En Proceso', 'Finalizada', 'Cancelada', 'Pendiente Confirmacion')

    @Column(length = 255, nullable = false)
    private String nota;

    @Column(length = 30, nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private Long gananciaNeta;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "cedula_comprador")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "cedula_vendedor")
    private Usuario vendedor;
}
