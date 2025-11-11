package com.proyecto.terranova.implement;


import com.proyecto.terranova.entity.Comentario;
import com.proyecto.terranova.repository.ComentarioRepository;
import com.proyecto.terranova.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public List<Comentario> listarPorProducto(Long idProducto) {
        return comentarioRepository.findByProductoIdProductoOrderByFechaComentarioDesc(idProducto);
    }

    @Override
    public Comentario guardar(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }
}
