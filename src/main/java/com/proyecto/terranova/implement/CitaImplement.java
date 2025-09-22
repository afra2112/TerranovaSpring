package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CitaDTO> encontrarPorVendedorParaCalendario(Usuario vendedor, boolean activo) {
        List<Cita> citas = repository.findByProducto_VendedorAndActivo(vendedor, activo);
        List<CitaDTO> citasDto = new ArrayList<>();
        for (Cita cita : citas){
            CitaDTO citaDTO = new CitaDTO();
            citaDTO.setEstadoCita(cita.getEstadoCita());
            citaDTO.setNombreVendedor(cita.getProducto().getVendedor().getNombres());
            citaDTO.setNombreComprador(cita.getComprador().getNombres());
            citaDTO.setIdCita(cita.getIdCita());
            citaDTO.setFecha(cita.getDisponibilidad().getFecha());
            citaDTO.setHora(cita.getDisponibilidad().getHora());
            citaDTO.setNombreProducto(cita.getProducto().getNombreProducto());
            citaDTO.setUbicacion(cita.getProducto().getCiudad().getNombreCiudad());
            citaDTO.setIdProducto(cita.getProducto().getIdProducto());
            citaDTO.setIdDisponibilidad(cita.getDisponibilidad().getIdDisponibilidad());

            citasDto.add(citaDTO);
        }
        return citasDto;
    }

    @Override
    public List<Cita> encontrarPorComprador(Usuario comprador, boolean activo) {
        return repository.findByCompradorAndActivo(comprador, activo);
    }

    @Override
    public List<Cita> encontrarPorEstado(Usuario vendedor, EstadoCitaEnum estado, boolean activo) {

        return repository.findByDisponibilidad_Producto_VendedorAndEstadoCitaAndActivo(vendedor,estado, activo);
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
