package com.proyecto.terranova.service;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface AsistenciaService {
    List<Asistencia> encontrarPorComprador(Usuario comprador);

    List<Asistencia> encontrarPorCompradorYEstado(Usuario comprador, EstadoAsistenciaEnum estadoAsistenciaEnum);

    boolean existeAsistenciaPorEstado(Usuario comprador, Long idProducto, EstadoAsistenciaEnum estadoAsistenciaEnum);

    List<Asistencia> encontrarAsistenciasPorCita(Long idCita);

    List<Asistencia> encontrarAsistenciasPorCitaYEstadoAsistencia(Long idCita, EstadoAsistenciaEnum estadoAsistenciaEnum);

    boolean existeCualquierAsistenciaPorUsuario(Usuario comprador, Long idProducto);

    void crearAsistencia(Usuario usuario, Long idCita, EstadoAsistenciaEnum estadoAsistenciaEnum);

    void cancelarAsistencia(Long idAsistencia) throws MessagingException, IOException;

    Integer obtenerPosicionDeUsuarioEnListaDeEspera(Long idCita, String cedulaUsuario);

    //Boolean cambiarEstadoAsistencia(Usuario usuario,Long idCita , EstadoAsistenciaEnum estadoAsistenciaEnum);

    Asistencia save(Asistencia asistencia);
}
