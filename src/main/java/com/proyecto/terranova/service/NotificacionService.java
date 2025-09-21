package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Notificacion;
import com.proyecto.terranova.entity.Usuario;
import org.aspectj.weaver.ast.Not;

public interface NotificacionService {
    NotificacionDTO save(NotificacionDTO dto);
    NotificacionDTO update(Long id, NotificacionDTO dto); // Actualizar
    NotificacionDTO findById(Long id);
    List<NotificacionDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
    void crearNotificacionAutomatica(String titulo, String mensaje, String tipo, Usuario usuario, Long idReferencia, String urlAccion);
    boolean validarSiEnviarNotificacionONo(Usuario usuario, String tipo);
    List<NotificacionDTO> obtenerNoLeidasPorUsuario(Usuario usuario);
    void marcarComoLeida(Long idNotificacion);
    void marcarTodasComoLeidas(Usuario usuario);
    List<Notificacion> obtenerPorUsuarioYActivo(Usuario usuario, boolean activo);
    int contarPorUsuarioYTipo(Usuario usuario, String tipo);
    List<Notificacion> obtenerPorUsuarioYTipo(Usuario usuario, String tipo);
    int contarNoLeidasPorUsuario(Usuario usuario, boolean leido);
    void borrarNotificacion(Long idNotificacion);
}
