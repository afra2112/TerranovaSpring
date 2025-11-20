package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Transporte;
import com.proyecto.terranova.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Long> {
    Optional<Transporte> findByVenta(Venta venta);
}
