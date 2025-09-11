package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.UbicacionService;
import com.proyecto.terranova.repository.UbicacionRepository;
import com.proyecto.terranova.dto.UbicacionDTO;
import com.proyecto.terranova.entity.Ubicacion;

@Service
public class UbicacionImplement implements UbicacionService {

    @Autowired
    private UbicacionRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UbicacionDTO save(UbicacionDTO dto) {
        Ubicacion entidadUbicacion = modelMapper.map(dto, Ubicacion.class);
        Ubicacion entidadGuardada = repository.save(entidadUbicacion);
        return modelMapper.map(entidadGuardada, UbicacionDTO.class);
    }

    @Override
    public UbicacionDTO update(Long id, UbicacionDTO dto) {
        Ubicacion entidadUbicacion = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ubicacion no encontrado"));

    	modelMapper.map(dto, entidadUbicacion);

    	Ubicacion entidadActualizada = repository.save(entidadUbicacion);
    	return modelMapper.map(entidadActualizada, UbicacionDTO.class);
    }

    @Override
    public UbicacionDTO findById(Long id) {
        Ubicacion entidadUbicacion = repository.findById(id).orElseThrow(() -> new RuntimeException("Ubicacion no encontrado"));
        return modelMapper.map(entidadUbicacion, UbicacionDTO.class);
    }

    @Override
    public List<UbicacionDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, UbicacionDTO.class))
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
