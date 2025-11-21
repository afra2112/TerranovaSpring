package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Comentario;
import com.proyecto.terranova.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByProductoOrderByFechaComentarioDesc(Producto producto);
}
