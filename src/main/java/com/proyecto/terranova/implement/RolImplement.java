package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.RolService;
import com.proyecto.terranova.repository.RolRepository;
import com.proyecto.terranova.dto.RolDTO;
import com.proyecto.terranova.entity.Rol;

@Service
public class RolImplement implements RolService {

    @Autowired
    private RolRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RolDTO save(RolDTO dto) {
        Rol entidadRol = modelMapper.map(dto, Rol.class);
        Rol entidadGuardada = repository.save(entidadRol);
        return modelMapper.map(entidadGuardada, RolDTO.class);
    }

    @Override
    public RolDTO update(Long id, RolDTO dto) {
        Rol entidadRol = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

    	modelMapper.map(dto, entidadRol);

    	Rol entidadActualizada = repository.save(entidadRol);
    	return modelMapper.map(entidadActualizada, RolDTO.class);
    }

    @Override
    public RolDTO findById(Long id) {
        Rol entidadRol = repository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return modelMapper.map(entidadRol, RolDTO.class);
    }

    @Override
    public List<RolDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, RolDTO.class))
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
