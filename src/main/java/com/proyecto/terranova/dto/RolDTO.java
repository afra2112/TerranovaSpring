package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.RolEnum;
import lombok.Data;

@Data
public class RolDTO {

    private Long idRol;

    private RolEnum nombreRol;
}
