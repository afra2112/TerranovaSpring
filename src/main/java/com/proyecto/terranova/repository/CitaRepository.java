package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Cita;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    //List<Cita> findByCompradorAndEstadoCita(Usuario comprador, EstadoCitaEnum activo);

    List<Cita> findByProducto_VendedorAndEstadoCitaAndActivo(Usuario vendedor, EstadoCitaEnum estadoCitaEnum, boolean activo);

    List<Cita> findByProducto_VendedorAndActivo(Usuario vendedor, boolean activo);

    List<Cita> findByUltimaReprogramacionBloqueadaNotNull();}
