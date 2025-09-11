package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.FincaService;
import com.proyecto.terranova.repository.FincaRepository;
import com.proyecto.terranova.dto.FincaDTO;
import com.proyecto.terranova.entity.Finca;

@Service
public class FincaImplement implements FincaService {

    @Autowired
    private FincaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FincaDTO save(FincaDTO dto) {
        Finca entidadFinca = modelMapper.map(dto, Finca.class);
        Finca entidadGuardada = repository.save(entidadFinca);
        return modelMapper.map(entidadGuardada, FincaDTO.class);
    }

    @Override
    public FincaDTO update(Long id, FincaDTO dto) {
        Finca entidadFinca = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Finca no encontrado"));

    	modelMapper.map(dto, entidadFinca);

    	Finca entidadActualizada = repository.save(entidadFinca);
    	return modelMapper.map(entidadActualizada, FincaDTO.class);
    }

    @Override
    public FincaDTO findById(Long id) {
        Finca entidadFinca = repository.findById(id).orElseThrow(() -> new RuntimeException("Finca no encontrado"));
        return modelMapper.map(entidadFinca, FincaDTO.class);
    }

    @Override
    public List<FincaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, FincaDTO.class))
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
