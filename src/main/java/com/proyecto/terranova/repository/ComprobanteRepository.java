package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Comprobante;

import java.util.List;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
}
