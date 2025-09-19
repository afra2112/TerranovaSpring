package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    private LocalDate fechaVenta;

    @Column(nullable = false)
    private LocalDateTime fechaInicioVenta;

    @Column(nullable = false)
    private String estado; //ENUM ('En Proceso', 'Finalizada', 'Cancelada', 'Pendiente Confirmacion')

    private String nota;

    @Column(length = 30, nullable = true)
    private String metodoPago;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @Column(nullable = false)
    private Long gananciaNeta;

    @ManyToOne
    @JoinColumn(name = "cedula_comprador")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "cedula_vendedor")
    private Usuario vendedor;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoVenta> listaGastos = new ArrayList<>();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comprobante> listaComprobantes = new ArrayList<>();

    @Transient
    private Long totalGastos;
}
