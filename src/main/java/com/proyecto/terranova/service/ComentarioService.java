package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Comentario;
import java.util.List;

public interface ComentarioService {

    void agregarComentario(String cedulaComprador, Long idProducto, String contenido);

    List<Comentario> obtenerComentariosPorProducto(Long idProducto);
}
