package com.proyecto.terranova.service;

import java.io.IOException;
import java.util.List;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.dto.NotificacionPeticion;
import com.proyecto.terranova.entity.*;
import jakarta.mail.MessagingException;

public interface NotificacionService {
    NotificacionDTO save(NotificacionDTO dto);
    NotificacionDTO update(Long id, NotificacionDTO dto); // Actualizar
    NotificacionDTO findById(Long id);
    List<NotificacionDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count();
    void crearNotificacionAutomatica(NotificacionPeticion notificacionPeticion) throws MessagingException, IOException;
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
    void notificacionCitaCancelada(Cita cita, Usuario compradorOVendedor) throws MessagingException, IOException;
    void notificacionCitaReservada(Cita cita) throws MessagingException, IOException;
    void notificacionCitaReprogramada(Cita cita, Usuario compradorOVendedor) throws MessagingException, IOException;
    void notificacionReprogramarCitaHabilitado(Cita cita) throws MessagingException, IOException;
    void notificacionVentaGenerada(Venta venta) throws MessagingException, IOException;
    void notificacionVentaModificada(Venta venta) throws MessagingException, IOException;
    void notificacionPeticionFinalizacionVenta(Venta venta) throws MessagingException, IOException;
    void notificacionDisponibilidadRegistrada(Disponibilidad disponibilidad, String fecha, String hora) throws MessagingException, IOException;
    void notificacionFotoPerfilCambiada(Usuario usuario) throws MessagingException, IOException;
    void notificacionDatosPersonalesActualizados(Usuario usuario) throws MessagingException, IOException;
    void notificacionPedirModificarVenta(Venta venta, String razon) throws MessagingException, IOException;
    void notificacionCitaFinalizada(Cita cita) throws MessagingException, IOException ;
    void notificacionRecuperarContrasena(String email) throws MessagingException, IOException;
}
