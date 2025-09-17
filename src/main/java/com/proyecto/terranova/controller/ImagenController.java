
package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.ImagenDTO;
import com.proyecto.terranova.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping("/SubirImgs")
    public String gurdarImagenes(@RequestParam Long idProducto , @RequestParam("imagen") List<MultipartFile> arch ){
        imagenService.guardarImagen(idProducto, arch);
        return "redirect:/imagenes" + idProducto;
    }


}