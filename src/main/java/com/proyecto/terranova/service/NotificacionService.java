package com.proyecto.terranova.service;

import java.io.IOException;
import java.util.List;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.dto.NotificacionPeticion;
import com.proyecto.terranova.entity.*;

public interface NotificacionService {
    NotificacionDTO save(NotificacionDTO dto);
    NotificacionDTO update(Long id, NotificacionDTO dto); // Actualizar
    NotificacionDTO findById(Long id);
    List<NotificacionDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count();
    void crearNotificacionAutomatica(NotificacionPeticion notificacionPeticion) throws IOException;
    boolean validarSiEnviarNotificacionONo(Usuario usuario, String tipo);
    void marcarComoLeida(Long idNotificacion);
    void marcarTodasComoLeidas(Usuario usuario);
    List<Notificacion> obtenerPorUsuarioYActivo(Usuario usuario, boolean activo);
    List<Notificacion> obtenerPorUsuarioYLeido(Usuario usuario, boolean leido);
    int contarPorUsuarioYTipo(Usuario usuario, String tipo);
    List<Notificacion> obtenerPorUsuarioYTipo(Usuario usuario, String tipo);
    int contarNoLeidasPorUsuario(Usuario usuario, boolean leido);
    void borrarNotificacion(Long idNotificacion);
    void eliminarHistorial(Usuario usuario);

    //HELPERS PARA CREAR LAS NOTIFICACIONES
    void notificacionCitaCancelada(Cita cita, Usuario compradorOVendedor) throws IOException;
    void notificacionCitaReservada(Asistencia asistencia, Usuario usuario) throws IOException;
    void notificacionCitaReprogramada(Cita cita, Usuario compradorOVendedor) throws IOException;
    void notificacionReprogramarCitaHabilitado(Cita cita) throws IOException;
    void notificacionVentaGenerada(Venta venta) throws IOException;
    void notificacionVentaModificada(Venta venta) throws IOException;
    void notificacionContraoferta(Venta venta) throws IOException;
    void notificacionFotoPerfilCambiada(Usuario usuario) throws IOException;
    void notificacionDatosPersonalesActualizados(Usuario usuario) throws IOException;
    void notificacionPedirModificarVenta(Venta venta, String razon) throws IOException;
    void notificacionCitaFinalizada(Cita cita) throws IOException ;
    void notificacionRecuperarContrasena(String email) throws IOException;
}
