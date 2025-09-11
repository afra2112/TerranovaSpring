package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.HistorialProductoVisto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialVistosRepository extends JpaRepository<HistorialProductoVisto, Long> {
    List<HistorialProductoVisto> findByUsuario(Usuario usuario);
}
