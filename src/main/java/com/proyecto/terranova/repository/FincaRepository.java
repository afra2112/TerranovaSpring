package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Finca;

@Repository
public interface FincaRepository extends JpaRepository<Finca, Long> {
}
