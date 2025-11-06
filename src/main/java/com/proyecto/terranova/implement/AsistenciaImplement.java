package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsistenciaImplement implements AsistenciaService {

    @Autowired
    AsistenciaRepository asistenciaRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CitaRepository citaRepository;

    @Override
    public List<Asistencia> encontrarPorComprador(Usuario comprador) {
        return asistenciaRepository.findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(comprador);
    }

    @Override
    public boolean existeAsistenciaPorEstado(Usuario comprador, Long idProducto, EstadoAsistenciaEnum estadoAsistenciaEnum) {
        return asistenciaRepository.existsByCita_ProductoAndUsuarioAndEstado(productoRepository.findById(idProducto).orElseThrow(), comprador, estadoAsistenciaEnum);
    }

    @Override
    public List<Asistencia> encontrarAsistenciasPorCita(Long idCita) {
        return asistenciaRepository.findByCita(citaRepository.findById(idCita).orElseThrow());
    }

    @Override
    public boolean existeCualquierAsistenciaPorUsuario(Usuario comprador, Long idProducto) {
        return asistenciaRepository.existsByCita_ProductoAndUsuario(productoRepository.findById(idProducto).orElseThrow(), comprador);
    }

    @Override
    public Asistencia crearAsistencia(Usuario usuario, Long idCita, EstadoAsistenciaEnum estadoAsistenciaEnum) {
        Asistencia asistencia = new Asistencia();
        asistencia.setFechaInscripcion(LocalDateTime.now());
        asistencia.setCita(citaRepository.findById(idCita).orElseThrow());
        asistencia.setUsuario(usuario);
        asistencia.setEstado(estadoAsistenciaEnum);
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }
}
