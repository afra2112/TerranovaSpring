package com.proyecto.terranova.implement;

import com.proyecto.terranova.repository.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.proyecto.terranova.service.DisponibilidadService;

@Service
public class DisponibilidadImplement implements DisponibilidadService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*@Override
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
    public Disponibilidad findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Disponibilidad no encontrado"));
    }

    @Override
    public List<Disponibilidad> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Disponibilidad> encontrarPorProducto(Long idProducto, boolean dispobile) {
        return repository.findByProductoAndDisponible(productoRepository.findById(idProducto).orElseThrow(), true);
    }

    @Override
    public List<DisponibilidadDTO> encontrarTodasPorVendedorYDisponible(Usuario vendedor, boolean disponible) {
        List<Disponibilidad> disponibilidades = repository.findByProducto_VendedorAndDisponible(vendedor, disponible);
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

    @Override
    public boolean validarSiPuedeReprogramar(Cita cita) {
        int maxReprogramaciones = 2;
        int totalReprogramaciones = cita.getNumReprogramaciones();

        if(totalReprogramaciones < maxReprogramaciones){
            return true;
        }

        if (cita.getUltimaReprogramacion() != null){
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime limite = cita.getUltimaReprogramacion().plusHours(24);

            return ahora.isAfter(limite);
        }

        return false;
    }*/
}
