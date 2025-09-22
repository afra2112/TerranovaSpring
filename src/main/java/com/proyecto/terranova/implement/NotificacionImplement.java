package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.repository.NotificacionRepository;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Notificacion;

@Service
public class NotificacionImplement implements NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NotificacionDTO save(NotificacionDTO dto) {
        Notificacion entidadNotificacion = modelMapper.map(dto, Notificacion.class);
        Notificacion entidadGuardada = repository.save(entidadNotificacion);
        return modelMapper.map(entidadGuardada, NotificacionDTO.class);
    }

    @Override
    public NotificacionDTO update(Long id, NotificacionDTO dto) {
        Notificacion entidadNotificacion = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Notificacion no encontrado"));

    	modelMapper.map(dto, entidadNotificacion);

    	Notificacion entidadActualizada = repository.save(entidadNotificacion);
    	return modelMapper.map(entidadActualizada, NotificacionDTO.class);
    }

    @Override
    public NotificacionDTO findById(Long id) {
        Notificacion entidadNotificacion = repository.findById(id).orElseThrow(() -> new RuntimeException("Notificacion no encontrado"));
        return modelMapper.map(entidadNotificacion, NotificacionDTO.class);
    }

    @Override
    public List<NotificacionDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, NotificacionDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Long id) {
        if(!repository.existsById(id)){
               return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void crearNotificacionAutomatica(String titulo, String mensaje, String tipo, Usuario usuario, Long idReferencia, String urlAccion) {
        if (validarSiEnviarNotificacionONo(usuario, tipo)){
            Notificacion notificacion = new Notificacion();
            notificacion.setTitulo(titulo);
            notificacion.setMensajeNotificacion(mensaje);
            notificacion.setTipo(tipo);
            notificacion.setLeido(false);
            notificacion.setFechaNotificacion(LocalDateTime.now());
            notificacion.setUsuario(usuario);
            notificacion.setReferenciaId(idReferencia);
            notificacion.setUrlAccion(urlAccion);
            repository.save(notificacion);
        }
    }

    @Override
    public boolean validarSiEnviarNotificacionONo(Usuario usuario, String tipo) {
        boolean enviar = true;

        switch (tipo){
            case "Disponibilidades":
                enviar = usuario.isNotificacionesDisponibilidades();
                break;
            case "Productos":
                enviar = usuario.isNotificacionesProductos();
                break;
            case "Citas":
                enviar = usuario.isNotificacionesCitas();
                break;
            case "Ventas":
                enviar = usuario.isNotificacionesVentas();
                break;
            case "Sistema":
                enviar = usuario.isNotificacionesSistema();
                break;
        }
        return enviar;
    }

    @Override
    public List<NotificacionDTO> obtenerNoLeidasPorUsuario(Usuario usuario) {
        return repository.findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(usuario, true)
                .stream()
                .map(notificacion -> modelMapper.map(notificacion, NotificacionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = repository.findById(idNotificacion).orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));

        if(!notificacion.isLeido()){
            notificacion.setLeido(true);
        }else {
            notificacion.setLeido(false);
        }
        repository.save(notificacion);
    }

    @Override
    public void marcarTodasComoLeidas(Usuario usuario) {
        List<Notificacion> notificaciones = repository.findByUsuarioAndLeidoFalseAndActivoOrderByFechaNotificacionDesc(usuario, true);
        notificaciones.forEach(noti -> noti.setLeido(true));
        repository.saveAll(notificaciones);
    }

    @Override
    public List<Notificacion> obtenerPorUsuarioYActivo(Usuario usuario, boolean activo) {
        return repository.findByUsuarioAndActivoOrderByFechaNotificacionDesc(usuario, activo);
    }

    @Override
    public int contarPorUsuarioYTipo(Usuario usuario, String tipo) {
        return repository.countByUsuarioAndTipoAndActivo(usuario, tipo, true);
    }

    @Override
    public List<Notificacion> obtenerPorUsuarioYTipo(Usuario usuario, String tipo) {
        return repository.findByUsuarioAndTipoOrderByFechaNotificacionDesc(usuario, tipo);
    }

    @Override
    public int contarNoLeidasPorUsuario(Usuario usuario, boolean leido) {
        return repository.countByUsuarioAndLeidoAndActivo(usuario, false, true);
    }

    @Override
    public void borrarNotificacion(Long idNotificacion) {
        Notificacion notificacion = repository.findById(idNotificacion).orElseThrow();
        notificacion.setActivo(false);
        notificacion.setLeido(true);
        repository.save(notificacion);
    }

    @Override
    public void eliminarHistorial(Usuario usuario) {
        List<Notificacion> notificacionesBorradas = repository.findByUsuarioAndActivoOrderByFechaNotificacionDesc(usuario, false);
        for (Notificacion notificacion : notificacionesBorradas){
            repository.delete(notificacion);
        }
    }

}
