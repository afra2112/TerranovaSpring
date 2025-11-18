package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoVentaEnum;
import com.proyecto.terranova.entity.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDTO {

    private Long idVenta;

    private LocalDate fechaFinVenta;

    private boolean pendienteConfirmacion = false;

    private String razonRechazo;

    private Long precioTotal;

    private boolean pagado = false;

    private LocalDate fechaLimitePago;

    private LocalDateTime fechaInicioVenta;

    private EstadoVentaEnum estado;

    private String nota;

    private int pasoActual = 1;

    private String metodoPago;

    private CitaDTO cita;

    private ProductoDTO producto;

    private PagoDTO pago;

    private Long gananciaNeta = 0L;

    private UsuarioDTO comprador;

    private UsuarioDTO vendedor;

    private List<GastoVentaDTO> listaGastos;

    private List<ComprobanteDTO> listaComprobantes;

    public Long getTotalGastos() {
        if (listaGastos == null) return 0L;
        return listaGastos.stream()
                .mapToLong(GastoVentaDTO::getValorGasto)
                .sum();
    }
}
