package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.EstadoProductoEnum;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByVendedorOrderByFechaPublicacionDesc(Usuario vendedor);
    List<Producto> findByVendedorAndEstadoOrderByFechaPublicacionDesc(Usuario vendedor, EstadoProductoEnum estadoProductoEnum);
    List<Producto> findByVendedorNot(Usuario vendedor);
    List<Producto> findAll(Specification<Producto> spec, Sort sort);
}
