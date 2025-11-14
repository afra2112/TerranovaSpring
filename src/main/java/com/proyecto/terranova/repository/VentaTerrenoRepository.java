package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.entity.VentaGanado;
import com.proyecto.terranova.entity.VentaTerreno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaTerrenoRepository extends JpaRepository<VentaTerreno, Long> {
    VentaTerreno findByVenta(Venta venta);
}