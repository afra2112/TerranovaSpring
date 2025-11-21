package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.TipoArchivoTransporteEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchivosTransportesDTO {

    private Long idArchivosTransporte;

    private String nombreArchivo;

    private String rutaArchivo;

    private LocalDateTime fechaSubida;

    private TipoArchivoTransporteEnum tipoArchivo;

    private String descripcion;
}
