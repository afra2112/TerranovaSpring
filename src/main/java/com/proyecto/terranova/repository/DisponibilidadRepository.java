package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Disponibilidad;

import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByProductoAndDisponible(Producto producto, boolean disponible);

    long countByProducto(Producto producto);

    List<Disponibilidad> findByProducto_VendedorAndDisponible(Usuario vendedor, boolean disponible);
}
