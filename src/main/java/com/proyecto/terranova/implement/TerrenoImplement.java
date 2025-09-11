package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.TerrenoService;
import com.proyecto.terranova.repository.TerrenoRepository;
import com.proyecto.terranova.dto.TerrenoDTO;
import com.proyecto.terranova.entity.Terreno;

@Service
public class TerrenoImplement implements TerrenoService {

    @Autowired
    private TerrenoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TerrenoDTO save(TerrenoDTO dto) {
        Terreno entidadTerreno = modelMapper.map(dto, Terreno.class);
        Terreno entidadGuardada = repository.save(entidadTerreno);
        return modelMapper.map(entidadGuardada, TerrenoDTO.class);
    }

    @Override
    public TerrenoDTO update(Long id, TerrenoDTO dto) {
        Terreno entidadTerreno = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Terreno no encontrado"));

    	modelMapper.map(dto, entidadTerreno);

    	Terreno entidadActualizada = repository.save(entidadTerreno);
    	return modelMapper.map(entidadActualizada, TerrenoDTO.class);
    }

    @Override
    public TerrenoDTO findById(Long id) {
        Terreno entidadTerreno = repository.findById(id).orElseThrow(() -> new RuntimeException("Terreno no encontrado"));
        return modelMapper.map(entidadTerreno, TerrenoDTO.class);
    }

    @Override
    public List<TerrenoDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, TerrenoDTO.class))
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
