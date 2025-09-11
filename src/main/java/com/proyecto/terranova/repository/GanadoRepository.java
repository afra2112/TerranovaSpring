package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Ganado;

@Repository
public interface GanadoRepository extends JpaRepository<Ganado, Long> {
}
