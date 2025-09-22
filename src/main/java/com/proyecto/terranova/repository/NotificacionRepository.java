package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.terranova.entity.Notificacion;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(Usuario usuario, boolean activa);
    List<Notificacion> findByUsuarioAndActivoOrderByFechaNotificacionDesc(Usuario usuario, boolean activa);
    List<Notificacion> findByUsuarioAndTipoOrderByFechaNotificacionDesc(Usuario usuario, String activa);
    int countByUsuarioAndTipoAndActivo(Usuario usuario, String tipo, boolean activa);
    int countByUsuarioAndLeidoAndActivo(Usuario usuario, boolean leido, boolean activa);
}
