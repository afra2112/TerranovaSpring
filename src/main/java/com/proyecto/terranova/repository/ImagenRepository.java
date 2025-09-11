package com.proyecto.terranova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
}
