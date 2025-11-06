package com.proyecto.terranova.repository;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(Usuario usuario);

    List<Asistencia> findByUsuarioAndEstadoOrderByCita_FechaAscCita_HoraInicioAsc(Usuario usuario, EstadoAsistenciaEnum estadoAsistenciaEnum);

    @Query("SELECT a FROM Asistencia a WHERE a.cita.idCita = :idCita AND a.estado = 'EN_ESPERA' ORDER BY a.usuario.puntuacionUsuario DESC")
    List<Asistencia> encontrarListaEsperaOrdenada(@Param("idCita") Long idCita);


    List<Asistencia> findByCita(Cita cita);

    boolean existsByCita_ProductoAndUsuarioAndEstado(Producto citaProducto, Usuario usuario, EstadoAsistenciaEnum estadoAsistenciaEnum);

    boolean existsByCita_ProductoAndUsuario(Producto citaProducto, Usuario usuario);

    Asistencia findByCita_IdCitaAndUsuario( Long idCita, Usuario usuario);
}
