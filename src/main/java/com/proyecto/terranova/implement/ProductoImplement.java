package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.dto.ProductoDTO;

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

        entidadProducto.setVendedor(usuario);
        Producto entidadGuardada = repository.save(entidadProducto);
        return modelMapper.map(entidadGuardada, ProductoDTO.class);
    }


    @Override
    public Producto crearProductoBase(ProductoDTO productoDTO){
        Producto producto = null;
        
        switch (producto.getClass().getSimpleName()){
            case "terreno":
                producto = modelMapper.map(productoDTO, Terreno.class);
                modelMapper.map(productoDTO.getTerrenoDTO(),producto);
                break;
            case "ganado":
                producto = modelMapper.map(productoDTO, Ganado.class);
                modelMapper.map(productoDTO.getGanadoDTO(),producto);
                break;
            case "finca":
                producto = modelMapper.map(productoDTO, Finca.class);
                modelMapper.map(productoDTO.getFincaDTO(),producto);
                break;
            default:
                throw new IllegalArgumentException("Tipo no válido");
        }
        return repository.save(producto);

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
    public Producto findById(Long id) {
        return  repository.findById(id).orElseThrow();
    }

    @Override
    public List<ProductoDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, ProductoDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Producto> obtenerTodosPorVendedor(Usuario vendedor) {
        return repository.findByVendedor(vendedor);
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
