package com.proyecto.terranova.service;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AsistenciaService {
    List<Asistencia> encontrarPorComprador(Usuario comprador);
    boolean existeAsistenciaPorEstado(Usuario comprador, Long idProducto, EstadoAsistenciaEnum estadoAsistenciaEnum);

    List<Asistencia> encontrarAsistenciasPorCita(Long idCita);

    boolean existeCualquierAsistenciaPorUsuario(Usuario comprador, Long idProducto);

    Asistencia crearAsistencia(Usuario usuario, Long idCita, EstadoAsistenciaEnum estadoAsistenciaEnum);

    Boolean cambiarEstadoAsistencia(Usuario usuario,Long idCita , EstadoAsistenciaEnum estadoAsistenciaEnum);

    Asistencia save(Asistencia asistencia);
}
