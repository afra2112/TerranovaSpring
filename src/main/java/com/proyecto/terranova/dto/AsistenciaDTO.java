package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AsistenciaDTO {

    private Long idAsistencia;

    private UsuarioDTO usuario;

    private EstadoAsistenciaEnum estado;

    private LocalDateTime fechaInscripcion;
}
