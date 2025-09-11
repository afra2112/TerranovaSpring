package com.proyecto.terranova.implement;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ImagenService;
import com.proyecto.terranova.repository.ImagenRepository;
import com.proyecto.terranova.dto.ImagenDTO;
import com.proyecto.terranova.entity.Imagen;

@Service
public class ImagenImplement implements ImagenService {

    @Autowired
    private ImagenRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ImagenDTO save(ImagenDTO dto) {
        Imagen entidadImagen = modelMapper.map(dto, Imagen.class);
        Imagen entidadGuardada = repository.save(entidadImagen);
        return modelMapper.map(entidadGuardada, ImagenDTO.class);
    }

    @Override
    public ImagenDTO update(Long id, ImagenDTO dto) {
        Imagen entidadImagen = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Imagen no encontrado"));

    	modelMapper.map(dto, entidadImagen);

    	Imagen entidadActualizada = repository.save(entidadImagen);
    	return modelMapper.map(entidadActualizada, ImagenDTO.class);
    }

    @Override
    public ImagenDTO findById(Long id) {
        Imagen entidadImagen = repository.findById(id).orElseThrow(() -> new RuntimeException("Imagen no encontrado"));
        return modelMapper.map(entidadImagen, ImagenDTO.class);
    }

    @Override
    public List<ImagenDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, ImagenDTO.class))
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
