package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
}
