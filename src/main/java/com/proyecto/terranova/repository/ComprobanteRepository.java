package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.InfoComprobante;
import com.proyecto.terranova.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Comprobante;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    Optional<Comprobante> findByVentaAndInfoComprobante(Venta venta, InfoComprobante infoComprobante);

    List<Comprobante> findByVenta(Venta venta);
}
