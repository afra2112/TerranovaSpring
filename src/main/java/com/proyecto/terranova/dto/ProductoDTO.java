package com.proyecto.terranova.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductoDTO {

    private Long idProducto;

    private String nombreProducto;

    private String tipoProducto;

    private Long precioProducto;

    private String descripcion;

    private String estado;

    private LocalDate fechaPublicacion;

    private String cedulaVendedor;

    private Long idUbicacion;

    private TerrenoDTO terrenoDTO;

    private FincaDTO  fincaDTO;

    private GanadoDTO ganadoDTO;

    private List<ImagenDTO> imagenes;

}
