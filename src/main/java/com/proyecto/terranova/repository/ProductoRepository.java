package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByVendedor(Usuario vendedor);
    List<Producto> findByVendedorNot(Usuario vendedor);
}
