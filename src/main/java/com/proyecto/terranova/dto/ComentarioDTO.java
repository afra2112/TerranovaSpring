package com.proyecto.terranova.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioDTO {

    private Long idComentario;

    private Long idProducto;

    private String cedulaUsuario;

    private String nombreUsuario;

    private String contenido;

    private LocalDateTime fechaComentario;
}

