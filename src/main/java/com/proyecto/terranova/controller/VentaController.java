
package com.proyecto.terranova.controller;

import com.proyecto.terranova.entity.GastoVenta;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.service.UsuarioService;
import com.proyecto.terranova.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/vendedor/ventas")
public class VentaController {

    @Autowired
    VentaService ventaService;

}