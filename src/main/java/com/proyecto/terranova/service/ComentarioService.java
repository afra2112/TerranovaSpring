package com.proyecto.terranova.service;



import com.proyecto.terranova.entity.Comentario;
import java.util.List;

public interface ComentarioService {
    List<Comentario> listarPorProducto(Long idProducto);
    Comentario guardar(Comentario comentario);
}
