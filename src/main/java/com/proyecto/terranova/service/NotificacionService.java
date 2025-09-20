package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Usuario;

public interface NotificacionService {
    NotificacionDTO save(NotificacionDTO dto);
    NotificacionDTO update(Long id, NotificacionDTO dto); // Actualizar
    NotificacionDTO findById(Long id);
    List<NotificacionDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
    void crearNotificacionAutomatica(String mensaje, String tipo, Usuario usuario);
    List<NotificacionDTO> obtenerNoLeidasPorUsuario(Usuario usuario);
    void marcarComoLeida(Long idNotificacion);
    void marcarTodasComoLeidas(Usuario usuario);
}
