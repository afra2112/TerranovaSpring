
package com.proyecto.terranova.controller;

import com.proyecto.terranova.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping("/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    // ImagenController.java
    @PostMapping("/SubirImgs")
    public String gurdarImagenes(@RequestParam Long idProducto , @RequestParam("imagen") List<MultipartFile> arch ){
        imagenService.guardarImagen(idProducto, arch);
        // Redirección al formulario de imágenes para mostrar un mensaje o continuar
        return "redirect:/vendedor/dashboard?imagenes=" + idProducto;
    }

    @PostMapping("/EliminarImgs/{idProducto}")
    public String eliminarImagenes(@PathVariable Long idProducto , @RequestParam("idImagen") Long idImagen){
        imagenService.eliminarImagen(idImagen);
        return "redirect:/vendedor/productos/Detalle/" + idProducto;
    }

}