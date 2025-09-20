package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Cita;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByComprador(Usuario usuario);

    List<Cita> findByDisponibilidad_Producto_VendedorAndEstadoCita(Usuario vendedor, EstadoCitaEnum estadoCitaEnum);

    List<Cita> findByProducto_Vendedor(Usuario vendedor);
}
