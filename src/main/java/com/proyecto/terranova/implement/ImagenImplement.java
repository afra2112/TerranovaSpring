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
import java.nio.file.StandardCopyOption;
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

    public void guardarImagen(Long idProducto, List<MultipartFile> archivos) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        List<Imagen> imagenes = archivos.stream().map(file -> {
            String url = guardarImagenProducto(file);
            Imagen img = new Imagen();
            img.setNombreArchivo(url);
            img.setProducto(producto);
            return img;
        }).collect(Collectors.toList());

        imagenRepository.saveAll(imagenes);
    }

    private String guardarImagenProducto(MultipartFile file) {
        try {
            Path directorio = Paths.get(directorioImagenes);
            Files.createDirectories(directorio);

            String nombreOriginal = file.getOriginalFilename();
            String extension = nombreOriginal != null && nombreOriginal.contains(".")
                    ? nombreOriginal.substring(nombreOriginal.lastIndexOf("."))
                    : "";
            String nombreLimpio = nombreOriginal != null
                    ? nombreOriginal.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_")
                    : "imagen";

            String nombreArchivo = UUID.randomUUID().toString() + "_" + nombreLimpio;

            Path rutaCompleta = directorio.resolve(nombreArchivo);
            Files.copy(file.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

            return "/imagenes/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen del producto", e);
        }
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
