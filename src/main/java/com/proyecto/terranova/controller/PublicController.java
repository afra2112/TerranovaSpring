package com.proyecto.terranova.controller;

import com.proyecto.terranova.dto.UsuarioDTO;
import com.proyecto.terranova.entity.Finca;
import com.proyecto.terranova.entity.Ganado;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(){
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



    @GetMapping("/403")
    public String error403(){
        return "403";
    }
}
