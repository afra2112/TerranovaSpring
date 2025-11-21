package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.entity.InfoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoComprobanteRepository extends JpaRepository<InfoComprobante, Long> {
    InfoComprobante findByNombreComprobante(NombreComprobanteEnum nombreComprobanteEnum);

}
