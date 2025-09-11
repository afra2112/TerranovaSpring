package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;

@Service
public class ProductoImplement implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductoDTO save(ProductoDTO dto) {
        Producto entidadProducto = modelMapper.map(dto, Producto.class);
        Usuario usuario = usuarioRepository.findById(dto.getCedulaVendedor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        entidadProducto.setUsuario(usuario);
        Producto entidadGuardada = repository.save(entidadProducto);
        return modelMapper.map(entidadGuardada, ProductoDTO.class);
    }

    @Override
    public ProductoDTO update(Long id, ProductoDTO dto) {
        Producto entidadProducto = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    	modelMapper.map(dto, entidadProducto);

    	Producto entidadActualizada = repository.save(entidadProducto);
    	return modelMapper.map(entidadActualizada, ProductoDTO.class);
    }

    @Override
    public ProductoDTO findById(Long id) {
        Producto entidadProducto = repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return modelMapper.map(entidadProducto, ProductoDTO.class);
    }

    @Override
    public List<ProductoDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, ProductoDTO.class))
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
