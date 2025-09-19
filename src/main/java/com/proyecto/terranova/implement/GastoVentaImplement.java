package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.GastoVentaService;
import com.proyecto.terranova.repository.GastoVentaRepository;
import com.proyecto.terranova.dto.GastoVentaDTO;
import com.proyecto.terranova.entity.GastoVenta;

@Service
public class GastoVentaImplement implements GastoVentaService {

    @Autowired
    private GastoVentaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GastoVenta save(GastoVenta gastoVenta) {
        return repository.save(gastoVenta);
    }

    @Override
    public GastoVentaDTO update(Long id, GastoVentaDTO dto) {
        GastoVenta entidadGastoVenta = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("GastoVenta no encontrado"));

    	modelMapper.map(dto, entidadGastoVenta);

    	GastoVenta entidadActualizada = repository.save(entidadGastoVenta);
    	return modelMapper.map(entidadActualizada, GastoVentaDTO.class);
    }

    @Override
    public GastoVentaDTO findById(Long id) {
        GastoVenta entidadGastoVenta = repository.findById(id).orElseThrow(() -> new RuntimeException("GastoVenta no encontrado"));
        return modelMapper.map(entidadGastoVenta, GastoVentaDTO.class);
    }

    @Override
    public List<GastoVentaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, GastoVentaDTO.class))
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
