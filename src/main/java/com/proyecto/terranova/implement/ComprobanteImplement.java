package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.repository.VentaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ComprobanteService;
import com.proyecto.terranova.repository.ComprobanteRepository;
import com.proyecto.terranova.dto.ComprobanteDTO;
import com.proyecto.terranova.entity.Comprobante;

@Service
public class ComprobanteImplement implements ComprobanteService {

    @Autowired
    private ComprobanteRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public Comprobante save(Comprobante comprobante) {
        return repository.save(comprobante);
    }

    @Override
    public ComprobanteDTO update(Long id, ComprobanteDTO dto) {
        Comprobante entidadComprobante = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));

    	modelMapper.map(dto, entidadComprobante);

    	Comprobante entidadActualizada = repository.save(entidadComprobante);
    	return modelMapper.map(entidadActualizada, ComprobanteDTO.class);
    }

    @Override
    public ComprobanteDTO findById(Long id) {
        Comprobante entidadComprobante = repository.findById(id).orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));
        return modelMapper.map(entidadComprobante, ComprobanteDTO.class);
    }

    @Override
    public List<ComprobanteDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, ComprobanteDTO.class))
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
