package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CitaDTO save(CitaDTO dto) {
        Cita entidadCita = modelMapper.map(dto, Cita.class);
        Cita entidadGuardada = repository.save(entidadCita);
        return modelMapper.map(entidadGuardada, CitaDTO.class);
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
    public CitaDTO findById(Long id) {
        Cita entidadCita = repository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrado"));
        return modelMapper.map(entidadCita, CitaDTO.class);
    }

    @Override
    public List<CitaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, CitaDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Cita> encontrarPorEstado(EstadoCitaEnum estado) {
        return repository.findByEstadoCita(estado);
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
}
