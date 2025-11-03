
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.RolService;
import com.proyecto.terranova.service.UsuarioService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService serviceUsuario;

    @Autowired
    private RolService rolService;

    @Autowired
    private NotificacionService notificacionService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return serviceUsuario.findByEmail(authentication.getName());
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = serviceUsuario.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @ModelAttribute("esVendedor")
    public boolean esVendedor(Authentication authentication){
        List<RolEnum> rolesUsuario = serviceUsuario.obtenerNombresRoles(usuario(authentication));

        boolean esVendedor = false;
        if(rolesUsuario.contains(RolEnum.VENDEDOR)){
            esVendedor = true;
        }
        return esVendedor;
    }

    @GetMapping("/mi-perfil")
    public String miPerfil(@RequestParam(name = "id") Long id,Model model, Authentication authentication){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM',' yyyy",  new Locale("es", "ES"));
        String lugar = null;
        if(id == 1){
            lugar = "comprador";
        } else if (id == 2) {
            lugar = "vendedor";
        }

        model.addAttribute("miPerfil", true);
        model.addAttribute("lugar", lugar);
        model.addAttribute("fechaRegistro", usuario(authentication).getFechaRegistro().format(formatter));

        return "miPerfil";
    }

    @PostMapping("/mi-perfil/cambiar-foto")
    public String cambiarFoto(@RequestParam(name = "foto") MultipartFile foto, Authentication authentication) throws IOException, MessagingException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
        Path rutaImagen = Paths.get("imagenes").resolve(nombreArchivo);

        Files.copy(foto.getInputStream(), rutaImagen, StandardCopyOption.REPLACE_EXISTING);
        Usuario usuario = usuario(authentication);
        usuario.setFoto(nombreArchivo);
        serviceUsuario.update(usuario);

        notificacionService.notificacionFotoPerfilCambiada(usuario);

        if(esVendedor(authentication)){
            return "redirect:/usuarios/mi-perfil?id=2";
        }
        return "redirect:/usuarios/mi-perfil?id=1";
    }

    @PostMapping("/mi-perfil/editar")
    public String editar(@ModelAttribute Usuario usuarioNuevo, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);
        usuario.setNombres(usuarioNuevo.getNombres());
        usuario.setApellidos(usuarioNuevo.getApellidos());
        usuario.setTelefono(usuarioNuevo.getTelefono());
        usuario.setNacimiento(usuarioNuevo.getNacimiento());
        serviceUsuario.update(usuario);

        notificacionService.notificacionDatosPersonalesActualizados(usuario);

        if(esVendedor(authentication)){
            return "redirect:/usuarios/mi-perfil?id=2";
        }
        return "redirect:/usuarios/mi-perfil?id=1";
    }

    @PostMapping("/mi-perfil/notificaciones")
    public String cambiarConfiguracionNotificaciones(
            @RequestParam(name = "emailNotif", required = false , defaultValue = "false") boolean correos,
            @RequestParam(name = "ventasCompras", required = false , defaultValue = "false") boolean ventas,
            @RequestParam(name = "citas", required = false , defaultValue = "false") boolean citas,
            @RequestParam(name = "disponibilidades", required = false , defaultValue = "false") boolean disponibilidades,
            @RequestParam(name = "sistema", required = false , defaultValue = "false") boolean sistema,
            @RequestParam(name = "productos", required = false , defaultValue = "false") boolean productos,
            Authentication authentication
    ) {
        Usuario usuario = usuario(authentication);

        usuario.setRecibirCorreos(correos);
        usuario.setNotificacionesCitas(citas);
        usuario.setNotificacionesSistema(sistema);
        usuario.setNotificacionesVentas(ventas);
        usuario.setNotificacionesProductos(productos);
        usuario.setNotificacionesDisponibilidades(disponibilidades);
        serviceUsuario.update(usuario);


        if(esVendedor(authentication)){
            return "redirect:/usuarios/mi-perfil?id=2";
        }
        return "redirect:/usuarios/mi-perfil?id=1";
    }
}