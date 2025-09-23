package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Producto;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByVendedor(Usuario vendedor);
    List<Producto> findByVendedorNot(Usuario vendedor);

    @Query("SELECT p FROM Producto p " +
            "WHERE (:tipo IS NULL OR TYPE(p) = :tipo) " +
            "AND (:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:ciudadId IS NULL OR p.ciudad.idCiudad = :ciudadId) " +
            "AND (:precioMax IS NULL OR p.precioProducto <= :precioMax)")
    List<Producto> filtrarProductos(@Param("tipo") Class<? extends Producto> tipo,
                                    @Param("nombre") String nombre,
                                    @Param("ciudadId") Long ciudadId,
                                    @Param("precioMax") Long precioMax);
}



