
package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.dto.NotificacionDTO;
import com.proyecto.terranova.entity.Notificacion;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.NotificacionService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/usuarios")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @ModelAttribute("esVendedor")
    public boolean esVendedor(Authentication authentication){
        List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario(authentication));
        boolean esVendedor = false;
        if(rolesUsuario.contains(RolEnum.VENDEDOR)){
            esVendedor = true;
        }
        return esVendedor;
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @GetMapping("/notificaciones")
    public String verNotificacionesUsuarioLogeado(@RequestParam(name = "id") Long id, @RequestParam(name = "filtro", required = false, defaultValue = "todas") String filtro, Model model, Authentication auth) {
        Usuario usuario = usuario(auth);
        String lugar = null;
        if(id == 1){
            lugar = "comprador";
        } else if (id == 2) {
            lugar = "vendedor";
        }

        List<Notificacion> notificaciones;

        switch (filtro) {
            case "producto":
                notificaciones = notificacionService.obtenerPorUsuarioYTipo(usuario, "Productos");
                break;
            case "venta":
                notificaciones = notificacionService.obtenerPorUsuarioYTipo(usuario, "Ventas");
                break;
            case "cita":
                notificaciones = notificacionService.obtenerPorUsuarioYTipo(usuario, "Citas");
                break;
            case "disponibilidad":
                notificaciones = notificacionService.obtenerPorUsuarioYTipo(usuario, "Disponibilidad");
                break;
            case "sistema":
                notificaciones = notificacionService.obtenerPorUsuarioYTipo(usuario, "Sistema");
                break;
            case "borradas":
                notificaciones = notificacionService.obtenerPorUsuarioYActivo(usuario, false);
                model.addAttribute("borrada", true);
                break;
            default:
                notificaciones = notificacionService.obtenerPorUsuarioYActivo(usuario, true);
                break;
        }

        model.addAttribute("totalTodas", notificacionService.obtenerPorUsuarioYActivo(usuario, true).size());
        model.addAttribute("totalDisponibilidades", notificacionService.contarPorUsuarioYTipo(usuario, "Disponibilidades"));
        model.addAttribute("totalProducto", notificacionService.contarPorUsuarioYTipo(usuario, "Productos"));
        model.addAttribute("totalVenta", notificacionService.contarPorUsuarioYTipo(usuario, "Ventas"));
        model.addAttribute("totalCita", notificacionService.contarPorUsuarioYTipo(usuario, "Citas"));
        model.addAttribute("totalSistema", notificacionService.contarPorUsuarioYTipo(usuario, "Sistema"));
        model.addAttribute("totalNoLeidas", notificacionService.contarNoLeidasPorUsuario(usuario, false));
        model.addAttribute("lugar", lugar);
        model.addAttribute("notificaciones", notificaciones);

        return "notificaciones";
    }

    @PostMapping("/notificaciones/borrar")
    public String borrarNotificacion(@RequestParam(name = "idNotificacion") Long idNotificacion, Authentication authentication){
        notificacionService.borrarNotificacion(idNotificacion);

        if(esVendedor(authentication)){
            return "redirect:/usuarios/notificaciones?id=2";
        }
        return "redirect:/usuarios/notificaciones?id=1";
    }

    @PostMapping("/notificaciones/eliminar-historial")
    public String eliminarHistorial(Authentication authentication){
        notificacionService.eliminarHistorial(usuario(authentication));

        if(esVendedor(authentication)){
            return "redirect:/usuarios/notificaciones?id=2";
        }
        return "redirect:/usuarios/notificaciones?id=1";
    }

    @PostMapping("/notificaciones/marcar-leida")
    public String marcarComoLeida(@RequestParam(name = "idNotificacion") Long idNotificacion, Authentication authentication){
        notificacionService.marcarComoLeida(idNotificacion);

        if(esVendedor(authentication)){
            return "redirect:/usuarios/notificaciones?id=2";
        }
        return "redirect:/usuarios/notificaciones?id=1";
    }

    @PostMapping("/notificaciones/marcar-todas-leidas")
    public String marcarTodasComoLeidas(Authentication authentication){
        Usuario usuario = usuario(authentication);
        notificacionService.marcarTodasComoLeidas(usuario);

        if(esVendedor(authentication)){
            return "redirect:/usuarios/notificaciones?id=2";
        }
        return "redirect:/usuarios/notificaciones?id=1";
    }

}