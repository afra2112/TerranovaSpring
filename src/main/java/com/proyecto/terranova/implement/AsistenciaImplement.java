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
    public void crearAsistencia(Usuario usuario, Long idCita, EstadoAsistenciaEnum estadoAsistenciaEnum) {

        if(!asistenciaRepository.existsByCita_ProductoAndUsuario(citaRepository.findById(idCita).orElseThrow().getProducto(), usuario)){
            Asistencia asistencia = new Asistencia();
            asistencia.setFechaInscripcion(LocalDateTime.now());
            asistencia.setCita(citaRepository.findById(idCita).orElseThrow());
            asistencia.setUsuario(usuario);
            asistencia.setEstado(estadoAsistenciaEnum);
        }

        Asistencia asistencia = asistenciaRepository.findByCita_IdCitaAndUsuario(idCita, usuario);
        asistencia.setEstado(estadoAsistenciaEnum);
        asistenciaRepository.save(asistencia);
    }

    /*@Override
    public Boolean cambiarEstadoAsistencia(Usuario usuario, Long idAsiatencia, EstadoAsistenciaEnum estadoAsistenciaEnum) {
        Asistencia asistenciaOptional = asistenciaRepository.findById(idAsiatencia).orElseThrow();

            asistenciaOptional.setEstado(estadoAsistenciaEnum);
            asistenciaRepository.save(asistenciaOptional);
            return true;
    }*/

    @Override
    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }
}
