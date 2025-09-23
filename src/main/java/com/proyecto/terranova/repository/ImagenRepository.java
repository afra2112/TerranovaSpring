package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Imagen;

import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByProducto(Producto producto);
}
