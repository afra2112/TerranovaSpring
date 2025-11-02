package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.UsuarioDTO;
import com.proyecto.terranova.entity.Ciudad;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CiudadRepository ciudadRepository;

    @GetMapping("/login")
    public String login(Model model){
        List<Producto> productos = productoRepository.findAll();
        List<String> ciudades = ciudadRepository.findAll().stream().map(Ciudad::getNombreCiudad).toList();
        for (Producto producto : productos) {
            producto.setTipoP(producto.getClass().getSimpleName());
        }
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("productos", productos);
        return "login";
    }

        @GetMapping("/registro")
    public String registroForm(Model model){
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute UsuarioDTO usuarioDTO, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "redirect:/registro";
        }
        if(!usuarioService.save(usuarioDTO)){
            System.out.println("YAEXISTE");
            redirectAttributes.addAttribute("yaExiste", true);
            return "redirect:/registro";
        }
        redirectAttributes.addAttribute("creado", true);
        return "redirect:/login";
    }

    @GetMapping("/password-olvidada")
    public String mostrarFormulario() {
        return "passwordOlvidada";
    }

    @PostMapping("/password-olvidada")
    public String procesarFormulario(@RequestParam String email, Model model) throws MessagingException, IOException {
        usuarioService.generarTokenYEnviarCorreoRecuperarContrasena(email);
        model.addAttribute("mensaje", "Se ha enviado un enlace a tu correo.");
        return "passwordOlvidada";
    }

    @GetMapping("/recuperar-password")
    public String mostrarReset(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "recuperarContrasena";
    }

    @PostMapping("/recuperar-password")
    public String procesarReset(@RequestParam String token, @RequestParam String nuevaContrasena, Model model) {
        String mensaje = usuarioService.validarTokenYActualizarContrasena(token, nuevaContrasena);
        model.addAttribute("mensaje", mensaje);
        return "login";
    }

    @GetMapping("/403")
    public String error403(){
        return "403";
    }
}
