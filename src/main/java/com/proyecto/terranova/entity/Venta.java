package com.proyecto.terranova.entity;

import com.proyecto.terranova.config.enums.EstadoVentaEnum;
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

    private LocalDate fechaFinVenta;

    private Long precioTotal;

    private boolean pagado = false;

    private LocalDate fechaLimitePago;

    private LocalDateTime fechaInicioVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVentaEnum estado;

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
