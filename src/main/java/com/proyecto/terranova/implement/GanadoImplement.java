package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.GanadoService;
import com.proyecto.terranova.repository.GanadoRepository;
import com.proyecto.terranova.dto.GanadoDTO;
import com.proyecto.terranova.entity.Ganado;

@Service
public class GanadoImplement implements GanadoService {

    @Autowired
    private GanadoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GanadoDTO save(GanadoDTO dto) {
        Ganado entidadGanado = modelMapper.map(dto, Ganado.class);
        Ganado entidadGuardada = repository.save(entidadGanado);
        return modelMapper.map(entidadGuardada, GanadoDTO.class);
    }

    @Override
    public GanadoDTO update(Long id, GanadoDTO dto) {
        Ganado entidadGanado = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ganado no encontrado"));

    	modelMapper.map(dto, entidadGanado);

    	Ganado entidadActualizada = repository.save(entidadGanado);
    	return modelMapper.map(entidadActualizada, GanadoDTO.class);
    }

    @Override
    public GanadoDTO findById(Long id) {
        Ganado entidadGanado = repository.findById(id).orElseThrow(() -> new RuntimeException("Ganado no encontrado"));
        return modelMapper.map(entidadGanado, GanadoDTO.class);
    }

    @Override
    public List<GanadoDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, GanadoDTO.class))
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
