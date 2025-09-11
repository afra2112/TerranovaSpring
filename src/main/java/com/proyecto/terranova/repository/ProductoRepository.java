package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
