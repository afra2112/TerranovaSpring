package com.proyecto.terranova.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    //campos de prueba para el flujo de compra/venta xd
    private Long precioTotal;
    private boolean pagado = false;
    private LocalDate fechaLimitePago;

    @Column(nullable = false)
    private LocalDateTime fechaInicioVenta;

    @Column(nullable = false)
    private String estado; //ENUM ('En Proceso', 'Finalizada', 'Cancelada', 'Pendiente Confirmacion')

    private String nota;

    private int pasoActual = 1;

    @Column(length = 30, nullable = true)
    private String metodoPago;

    @OneToOne
    @JoinColumn(name = "idCita")
    private Cita cita;

    @OneToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    private Long gananciaNeta = 0L;

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
    public Long getTotalGastos() {
        if (listaGastos == null) return 0L;
        return listaGastos.stream()
                .mapToLong(GastoVenta::getValorGasto)
                .sum();
    }
}
