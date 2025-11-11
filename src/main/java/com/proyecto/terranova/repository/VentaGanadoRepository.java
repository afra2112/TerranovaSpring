package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.VentaGanado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaGanadoRepository extends JpaRepository<VentaGanado, Long> {
}
