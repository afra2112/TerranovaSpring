package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.repository.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ImagenService;
import com.proyecto.terranova.repository.ImagenRepository;
import com.proyecto.terranova.dto.ImagenDTO;
import com.proyecto.terranova.entity.Imagen;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenImplement implements ImagenService {

    @Value("${imagenes.directorio}")
    private String directorioImagenes;

    @Autowired
    private ImagenRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ImagenRepository imagenRepository;

    public void  guardarImagen(Long IdProducto , List<MultipartFile> archivo){
        Producto producto = productoRepository.findById(IdProducto).orElseThrow();

        List<Imagen> imagenes = archivo.stream().map(file -> {
            String url = guardarImagen(file);
            Imagen img = new Imagen();
            img.setNombreArchivo(url);
            img.setProducto(producto);
            return img;
        }).collect(Collectors.toList());
        imagenRepository.saveAll(imagenes);

    }
    private String guardarImagen(MultipartFile file){

        String original = file.getOriginalFilename();

        String limpio = original.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        String nombre = UUID.randomUUID() + "_" + limpio;

        // Codificar el nombre para URL
        String nombreCodificado = URLEncoder.encode(nombre, StandardCharsets.UTF_8);

        Path ruta =  Paths.get(directorioImagenes + nombreCodificado); // nombre de la tuta
        try{
            Files.copy(file.getInputStream(), ruta);
        }catch (IOException q){
            throw new RuntimeException("Error al guardar el archivo",q);
        }
        return "/imagenes/" + nombre;
    }

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
    public boolean eliminarImagen(Long id) {
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
