package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(Usuario usuario);

    List<Asistencia> findByCita(Cita cita);

    boolean existsByCita_ProductoAndUsuarioAndEstado(Producto citaProducto, Usuario usuario, EstadoAsistenciaEnum estadoAsistenciaEnum);

    boolean existsByCita_ProductoAndUsuario(Producto citaProducto, Usuario usuario);
}
