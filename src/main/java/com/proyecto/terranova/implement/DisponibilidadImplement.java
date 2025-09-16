package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.DisponibilidadService;
import com.proyecto.terranova.repository.DisponibilidadRepository;
import com.proyecto.terranova.dto.DisponibilidadDTO;
import com.proyecto.terranova.entity.Disponibilidad;

@Service
public class DisponibilidadImplement implements DisponibilidadService {

    @Autowired
    private DisponibilidadRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Disponibilidad save(Disponibilidad disponibilidad) {
        return repository.save(disponibilidad);
    }

    @Override
    public DisponibilidadDTO update(Long id, DisponibilidadDTO dto) {
        Disponibilidad entidadDisponibilidad = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrado"));

    	modelMapper.map(dto, entidadDisponibilidad);

    	Disponibilidad entidadActualizada = repository.save(entidadDisponibilidad);
    	return modelMapper.map(entidadActualizada, DisponibilidadDTO.class);
    }

    @Override
    public DisponibilidadDTO findById(Long id) {
        Disponibilidad entidadDisponibilidad = repository.findById(id).orElseThrow(() -> new RuntimeException("Disponibilidad no encontrado"));
        return modelMapper.map(entidadDisponibilidad, DisponibilidadDTO.class);
    }

    @Override
    public List<Disponibilidad> findAll() {
        return repository.findAll();
    }

    @Override
    public List<DisponibilidadDTO> encontrarTodasPorVendedor(Usuario vendedor) {
        List<Producto> productos = productoRepository.findByVendedor(vendedor);
        List<Disponibilidad> disponibilidades = new ArrayList<>();
        for (Producto producto : productos){
            disponibilidades.addAll(producto.getDisponibilidades());
        }

        return disponibilidades.stream().map(disponibilidad -> modelMapper.map(disponibilidad, DisponibilidadDTO.class)).toList();
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
