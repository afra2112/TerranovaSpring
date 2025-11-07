package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.dto.AsistenciaDTO;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        return repository.findByProducto_VendedorAndActivo(vendedor, activo);
    }

    @Override
    public List<Cita> encontrarPorProducto(Long idProducto) {
        return repository.findByProducto(productoRepository.findById(idProducto).orElseThrow());
    }

    @Override
    public List<CitaDTO> encontrarPorVendedorParaCalendario(Usuario vendedor, boolean activo) {
        List<Cita> citas = repository.findByProducto_VendedorAndActivo(vendedor, activo);
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

            citasDto.add(citaDTO);
        }
        return citasDto;
    }

    /*@Override
    public List<Cita> encontrarPorCompradorYEstado(Usuario comprador, EstadoCitaEnum estadoCitaEnum) {
        return repository.findByCompradorAndEstadoCita(comprador, estadoCitaEnum);
    }*/

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
