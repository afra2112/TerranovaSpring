package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.CitaService;
import com.proyecto.terranova.repository.CitaRepository;
import com.proyecto.terranova.dto.CitaDTO;
import com.proyecto.terranova.entity.Cita;

@Service
public class CitaImplement implements CitaService {

    @Autowired
    private CitaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Cita save(Cita cita) {
        return repository.save(cita);
    }

    @Override
    public CitaDTO update(Long id, CitaDTO dto) {
        Cita entidadCita = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cita no encontrado"));

    	modelMapper.map(dto, entidadCita);

    	Cita entidadActualizada = repository.save(entidadCita);
    	return modelMapper.map(entidadActualizada, CitaDTO.class);
    }

    @Override
    public Cita findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrado"));
    }

    @Override
    public List<Cita> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Cita> encontrarPorVendedor(Usuario vendedor) {
        return repository.findByDisponibilidad_Producto_Vendedor(vendedor);
    }

    @Override
    public List<Cita> encontrarPorComprador(Usuario comprador) {
        return repository.findByComprador(comprador);
    }

    @Override
    public List<Cita> encontrarPorEstado(Usuario vendedor, EstadoCitaEnum estado) {

        return repository.findByDisponibilidad_Producto_VendedorAndEstadoCita(vendedor,estado);
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
