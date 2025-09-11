package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
}
