package com.proyecto.terranova.dto;

import com.proyecto.terranova.config.enums.CiudadesEnum;
import com.proyecto.terranova.config.enums.DepartamentoEnum;
import lombok.Data;

@Data
public class UbicacionDTO {

    private Long idUbicacion;

    private DepartamentoEnum departamento;

    private CiudadesEnum ciudad;

    private String detalles;
}
