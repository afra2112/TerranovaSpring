package com.proyecto.terranova.repository;


import com.proyecto.terranova.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByProductoIdProductoOrderByFechaComentarioDesc(Long idProducto);
}
