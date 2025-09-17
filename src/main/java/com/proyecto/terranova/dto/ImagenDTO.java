package com.proyecto.terranova.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImagenDTO {

    private Long idImagen;

    private List<MultipartFile> nombreArchivo;

    private Long idProducto;
}
