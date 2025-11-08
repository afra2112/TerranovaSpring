package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.dto.AsistenciaDTO;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.NotificacionService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.entity.Cita;

@Service
public class CitaImplement implements CitaService {

    @Autowired
    private CitaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Override
    public Cita save(Cita cita) {
        return repository.save(cita);
    }

    @Override
    public CitaDTO update(Long id, CitaDTO dto) {
        Cita entidadCita = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cita no encontrado"));

    	modelMapper.map(dto, entidadCita);

    	Cita entidadActualizada = repository.save(entidadCita);
    	return modelMapper.map(entidadActualizada, CitaDTO.class);
    }

    @Override
    public Cita findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrado"));
    }

    @Override
    public List<Cita> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Cita> encontrarPorVendedor(Usuario vendedor, boolean activo) {
        return repository.findByProducto_VendedorAndActivoOrderByFechaDescHoraInicioDesc(vendedor, activo);
    }

    @Override
    public List<Cita> encontrarPorProducto(Long idProducto) {
        return repository.findByProducto(productoRepository.findById(idProducto).orElseThrow());
    }

    @Override
    public List<CitaDTO> encontrarPorVendedorParaCalendario(Usuario vendedor, boolean activo) {
        List<Cita> citas = repository.findByProducto_VendedorAndActivoOrderByFechaDescHoraInicioDesc(vendedor, activo);
        List<CitaDTO> citasDto = new ArrayList<>();
        for (Cita cita : citas){
            CitaDTO citaDTO = new CitaDTO();
            citaDTO.setEstadoCita(cita.getEstadoCita());
            citaDTO.setNombreVendedor(cita.getProducto().getVendedor().getNombres());
            citaDTO.setIdCita(cita.getIdCita());
            citaDTO.setFecha(cita.getFecha());
            citaDTO.setHoraInicio(cita.getHoraInicio());
            citaDTO.setHoraFin(cita.getHoraFin());
            citaDTO.setNombreProducto(cita.getProducto().getNombreProducto());
            citaDTO.setUbicacion(cita.getProducto().getCiudad().getNombreCiudad());
            citaDTO.setProductoDTO(modelMapper.map(cita.getProducto(), ProductoDTO.class));
            citaDTO.setAsistenciasDTO(cita.getAsistencias().stream().map(asistencia -> modelMapper.map(asistencia, AsistenciaDTO.class)).toList());
            citaDTO.setCupoMaximo(cita.getCupoMaximo());

            citasDto.add(citaDTO);
        }
        return citasDto;
    }

    /*@Override
    public List<Cita> encontrarPorCompradorYEstado(Usuario comprador, EstadoCitaEnum estadoCitaEnum) {
        return repository.findByCompradorAndEstadoCita(comprador, estadoCitaEnum);
    }*/

    @Transactional
    @Override
    public void reprogramarCita(Long idCita, LocalDate nuevaFecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) throws MessagingException, IOException {
        Cita cita = repository.findById(idCita).orElseThrow();

        cita.setFecha(nuevaFecha);
        cita.setHoraInicio(nuevaHoraInicio);
        cita.setHoraFin(nuevaHoraFin);
        cita.setEstadoCita(EstadoCitaEnum.PROGRAMADA);

        repository.save(cita);

        List<Asistencia> asistencias = asistenciaRepository.findByCita(repository.findById(idCita).orElseThrow());
        for (Asistencia a : asistencias) {
            notificacionService.notificacionCitaReprogramada(a.getCita(), a.getUsuario());
        }
    }

    @Transactional
    @Override
    public void cancelarCita(Long idCita) {
        Cita cita = repository.findById(idCita).orElseThrow();
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        repository.save(cita);

        List<Asistencia> asistencias = asistenciaRepository.findByCita(repository.findById(idCita).orElseThrow());

        for (Asistencia a : asistencias) {
            a.setEstado(EstadoAsistenciaEnum.CANCELADO_AUTOMATICO);
            asistenciaRepository.save(a);

            //notificacionService.notificarCancelacion(a.getUsuario(), cita);
        }
    }

    @Transactional
    @Override
    public void finalizarCita(Long idCita, Map<String, String> params) {

        Cita cita = repository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstadoCita(EstadoCitaEnum.FINALIZADA);
        repository.save(cita);

        for (Asistencia asistencia : cita.getAsistencias()) {

            if (asistencia.getEstado() != EstadoAsistenciaEnum.INSCRITO) {
                continue;
            }

            String key = "asistencias[" + asistencia.getIdAsistencia() + "].asistio";

            boolean asistio = params.containsKey(key);

            asistencia.setAsistio(asistio);
            asistenciaRepository.save(asistencia);

            int puntuacionUsuario = asistencia.getUsuario().getPuntuacionUsuario();

            if (asistio) {
                asistencia.getUsuario().setPuntuacionUsuario(puntuacionUsuario + 5);
                asistencia.setEstado(EstadoAsistenciaEnum.ASISTIO);
                asistenciaRepository.save(asistencia);
            } else {
                asistencia.getUsuario().setPuntuacionUsuario(puntuacionUsuario - 7);
                asistencia.setEstado(EstadoAsistenciaEnum.NO_ASISTIO);
                asistenciaRepository.save(asistencia);
            }
        }

        // notificacionService.notificacionCitaFinalizada(cita);
    }


    @Override
    public List<Cita> encontrarPorEstado(Usuario vendedor, EstadoCitaEnum estado, boolean activo) {

        return repository.findByProducto_VendedorAndEstadoCitaAndActivo(vendedor,estado, activo);
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
    public long contarPorVendedor(Usuario vendedor) {
        return repository.countByProducto_Vendedor(vendedor);
    }

    @Override
    public void cambiarEstado(Cita cita, EstadoCitaEnum estado) {
        cita.setEstadoCita(estado);
        repository.save(cita);
    }

    @Override
    public void borrarCita(Long idCita) {
        Cita cita = repository.findById(idCita).orElseThrow();
        cita.setActivo(false);
        repository.save(cita);
    }
}
