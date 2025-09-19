package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto crearProductoBase(Map<String, String> datosForm, String correo) {
        Producto producto;
        String tipo = datosForm.get("tipoProducto");
        System.out.println("Datos recibidos: " + datosForm);
        if (tipo == null) throw new IllegalArgumentException("Tipo de producto No espesificado");
        switch (tipo.toLowerCase()){
            case "terreno":
                producto = modelMapper.map(datosForm, Terreno.class);
                break;
            case "finca":
                producto = modelMapper.map(datosForm, Finca.class);
                break;
            case "ganado" :
                producto = modelMapper.map(datosForm, Ganado.class);
                break;
            default:
                throw new IllegalArgumentException("Tipo de Producto No valido");
        }

        producto.setFechaPublicacion(LocalDate.now());
        producto.setEstado("Disponible");

        Usuario vendedor = usuarioRepository.findByEmail(correo);
        if (vendedor == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        System.out.println("Cédula del vendedor: " + vendedor.getCedula());
        producto.setVendedor(vendedor);

        System.out.println("Producto mapeado: " + producto);

        return productoRepository.save(producto);
    }

    @Override
    public ProductoDTO save(ProductoDTO dto) {
        Producto entidadProducto = modelMapper.map(dto, Producto.class);
        Usuario usuario = usuarioRepository.findById(dto.getCedulaVendedor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        entidadProducto.setVendedor(usuario);
        Producto entidadGuardada = repository.save(entidadProducto);
        return modelMapper.map(entidadGuardada, ProductoDTO.class);
    }

/*
    @Override
    public Producto crearProductoBase(ProductoDTO productoDTO){
        Producto producto = null;
        
        switch (productoDTO.getTipoProducto().toLowerCase()){
            case "terreno":
                Terreno terreno = new Terreno();
                // Mapeas las propiedades del DTO base y del DTO específico
                modelMapper.map(productoDTO, terreno);
                modelMapper.map(productoDTO.getTerrenoDTO(), terreno);
                producto = terreno;
                break;
            case "ganado":
                Ganado ganado = new Ganado();
                modelMapper.map(productoDTO, ganado);
                modelMapper.map(productoDTO.getGanadoDTO(), ganado);
                producto = ganado;
                break;
            case "finca":
                Finca finca = new Finca();
                modelMapper.map(productoDTO, finca);
                modelMapper.map(productoDTO.getFincaDTO(), finca);
                producto = finca;
                break;
            default:
                throw new IllegalArgumentException("Tipo no válido");
        }

        producto.setEstado("Disponible");
        producto.setFechaPublicacion(LocalDate.now());

        Usuario usuario = usuarioRepository.findById(productoDTO.getCedulaVendedor())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        producto.setVendedor(usuario);

        return repository.save(producto);

    }*/

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
