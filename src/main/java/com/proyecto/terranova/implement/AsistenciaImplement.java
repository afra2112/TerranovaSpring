package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.AsistenciaService;
import com.proyecto.terranova.service.NotificacionService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    NotificacionService notificacionService;

    @Override
    public List<Asistencia> encontrarPorComprador(Usuario comprador) {
        return asistenciaRepository.findByUsuarioOrderByCita_FechaAscCita_HoraInicioAsc(comprador);
    }

    @Override
    public List<Asistencia> encontrarPorCompradorYEstado(Usuario comprador, EstadoAsistenciaEnum estadoAsistenciaEnum) {
        return asistenciaRepository.findByUsuarioAndEstadoOrderByCita_FechaAscCita_HoraInicioAsc(comprador, estadoAsistenciaEnum);
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
            asistenciaRepository.save(asistencia);
        }else {
            Asistencia asistencia = asistenciaRepository.findByCita_IdCitaAndUsuario(idCita, usuario);
            asistencia.setEstado(estadoAsistenciaEnum);
            asistenciaRepository.save(asistencia);
        }
    }

    @Transactional
    @Override
    public void cancelarAsistencia(Long idAsistencia) throws MessagingException, IOException {
        Asistencia asistencia = asistenciaRepository.findById(idAsistencia).orElseThrow();

        asistencia.setEstado(EstadoAsistenciaEnum.CANCELADO);
        asistenciaRepository.save(asistencia);

        Long idCita = asistencia.getCita().getIdCita();

        List<Asistencia> listaEspera = asistenciaRepository.encontrarListaEsperaOrdenada(idCita);

        if (!listaEspera.isEmpty()) {
            Asistencia siguiente = listaEspera.getFirst();
            siguiente.setEstado(EstadoAsistenciaEnum.INSCRITO);
            asistenciaRepository.save(siguiente);

            notificacionService.notificacionCitaReservada(asistencia, siguiente.getUsuario());
        }
    }

    @Override
    public Integer obtenerPosicionDeUsuarioEnListaDeEspera(Long idCita, String cedulaUsuario) {
        List<Asistencia> listaDeEspera = asistenciaRepository.encontrarListaEsperaOrdenada(idCita);

        for (int i = 0; i < listaDeEspera.size(); i++){
            if (listaDeEspera.get(i).getUsuario().getCedula().equals(cedulaUsuario)){
                return i + 1;
            }
        }
        return null;
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
