package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Terreno;

@Repository
public interface TerrenoRepository extends JpaRepository<Terreno, Long> {
}
