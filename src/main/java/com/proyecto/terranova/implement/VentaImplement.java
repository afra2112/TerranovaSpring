package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.VentaService;
import com.proyecto.terranova.repository.VentaRepository;
import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.Venta;

@Service
public class VentaImplement implements VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public VentaDTO save(VentaDTO dto) {
        Venta entidadVenta = modelMapper.map(dto, Venta.class);
        Venta entidadGuardada = repository.save(entidadVenta);
        return modelMapper.map(entidadGuardada, VentaDTO.class);
    }

    @Override
    public VentaDTO update(Long id, VentaDTO dto) {
        Venta entidadVenta = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrado"));

    	modelMapper.map(dto, entidadVenta);

    	Venta entidadActualizada = repository.save(entidadVenta);
    	return modelMapper.map(entidadActualizada, VentaDTO.class);
    }

    @Override
    public VentaDTO findById(Long id) {
        Venta entidadVenta = repository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrado"));
        return modelMapper.map(entidadVenta, VentaDTO.class);
    }

    @Override
    public List<VentaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, VentaDTO.class))
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
