package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(Usuario usuario);

    boolean existsByCita_ProductoAndUsuario(Producto citaProducto, Usuario usuario);
}
