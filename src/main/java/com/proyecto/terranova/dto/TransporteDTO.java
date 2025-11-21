package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoTransporteEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransporteDTO {

    private Long idTransporte;

    private LocalDate fechaTransporte;

    private LocalTime horaTransporte;

    private String puntoEntrega;

    private String empresaTransporte;

    private String placaVehiculo;

    private String nombreConductor;

    private String telefonoConductor;

    private String cedulaConductor;

    private String observacionesComprador;

    private String observacionesVendedor;

    private EstadoTransporteEnum estadoTransporte;

    private LocalDateTime fechaHoraCarga;

    private LocalDateTime fechaHoraEntrega;

    private List<ArchivosTransportesDTO> archivosTransporte = new ArrayList<>();
}
