package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.TipoArchivoTransporteEnum;
import com.proyecto.terranova.entity.ArchivosTransportes;
import com.proyecto.terranova.entity.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivosTransporteRepository extends JpaRepository<ArchivosTransportes, Long> {
    List<ArchivosTransportes> findByTransporteAndTipoArchivo(Transporte transporte, TipoArchivoTransporteEnum tipoArchivo);
}