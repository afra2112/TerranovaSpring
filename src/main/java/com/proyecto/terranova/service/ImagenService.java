package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImagenService {
    void guardarImagen(Long IdProducto , List<MultipartFile> archivo);
    ImagenDTO save(ImagenDTO dto);
    ImagenDTO update(Long id, ImagenDTO dto); // Actualizar
    ImagenDTO findById(Long id);
    List<ImagenDTO> findAll();
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
