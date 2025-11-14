package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.entity.VentaFinca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaFincaRepository extends JpaRepository<VentaFinca, Long> {
    VentaFinca findByVenta(Venta venta);
}
