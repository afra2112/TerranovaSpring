package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ResenaService;
import com.proyecto.terranova.repository.ResenaRepository;
import com.proyecto.terranova.dto.ResenaDTO;
import com.proyecto.terranova.entity.Resena;

@Service
public class ResenaImplement implements ResenaService {

    @Autowired
    private ResenaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResenaDTO save(ResenaDTO dto) {
        Resena entidadResena = modelMapper.map(dto, Resena.class);
        Resena entidadGuardada = repository.save(entidadResena);
        return modelMapper.map(entidadGuardada, ResenaDTO.class);
    }

    @Override
    public ResenaDTO update(Long id, ResenaDTO dto) {
        Resena entidadResena = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Resena no encontrado"));

    	modelMapper.map(dto, entidadResena);

    	Resena entidadActualizada = repository.save(entidadResena);
    	return modelMapper.map(entidadActualizada, ResenaDTO.class);
    }

    @Override
    public ResenaDTO findById(Long id) {
        Resena entidadResena = repository.findById(id).orElseThrow(() -> new RuntimeException("Resena no encontrado"));
        return modelMapper.map(entidadResena, ResenaDTO.class);
    }

    @Override
    public List<ResenaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, ResenaDTO.class))
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
}
