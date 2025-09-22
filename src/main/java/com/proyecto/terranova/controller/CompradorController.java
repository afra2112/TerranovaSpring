package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comprador")
public class CompradorController {

    @Autowired
    CompradorService compradorService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    CitaService citaService;

    @Autowired
    DisponibilidadService disponibilidadService;

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    VentaService ventaService;

    @ModelAttribute("usuario")
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
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

    @GetMapping("/explorar")
    public String index(Model model, Authentication authentication){
        model.addAttribute("explorar", true);

        Map<String, Integer> estadisticas = compradorService.prepararIndex(usuario(authentication).getCedula());
        model.addAllAttributes(estadisticas);
        return "comprador/principalComprador";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        model.addAttribute("posicionCitas", true);
        model.addAttribute("citas", citaService.encontrarPorComprador(usuarioService.findByEmail(authentication.getName())));
        return "comprador/citas";
    }

    @GetMapping("/compras")
    public String compras(Model model, Authentication authentication){
        model.addAttribute("posicionCompras", true);
        model.addAttribute("compras", ventaService.encontrarPorComprador(usuario(authentication)));
        return "comprador/compras";
    }

    @PostMapping("/compras/actualizar-compra")
    public String actualizarCompra(RedirectAttributes redirectAttributes, @RequestParam(name = "idVenta") Long idVenta, @RequestParam(name = "accion") String accion){
        switch (accion){
            case "cancelar":
                Venta venta = ventaService.findById(idVenta);
                venta.setGananciaNeta(0L);
                ventaService.actualizarEstado(venta, "Cancelada");
                redirectAttributes.addFlashAttribute("modalCancelar", true);
                break;
            case "finalizar":
                ventaService.actualizarEstado(ventaService.findById(idVenta), "Finalizada");
                redirectAttributes.addFlashAttribute("modalFinalizar", true);
                break;

            case "modificar":
                ventaService.actualizarEstado(ventaService.findById(idVenta), "En Proceso");
                redirectAttributes.addFlashAttribute("modalModificar", true);

                //ENVIAR NOTIFICACIONES Y RAZON O PETICION EN LA NOTIFICACION e EMAIL
                break;
        }
        return "redirect:/comprador/compras";
    }

    @PostMapping("/citas/cancelar-cita")
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication){
        Usuario usuario = usuario(authentication);
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);

        String titulo = "Actualizacion en tu cita. Cancelacion.";
        String mensajeVendedor = "Tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Ha sido cancelada por el comprador: " + usuario.getNombres() + ".";
        String mensaje = "Has cancelado tu cita para el producto: " + cita.getProducto().getNombreProducto() + ".";

        notificacionService.crearNotificacionAutomatica(titulo, mensajeVendedor, "Citas", usuario(authentication), idCita, "/vendedor/citas");
        notificacionService.crearNotificacionAutomatica(titulo, mensaje, "Citas", cita.getComprador(), idCita, "/comprador/citas");


        return "redirect:/comprador/citas";
    }

    @PostMapping("/citas/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad, Authentication authentication, RedirectAttributes redirectAttributes){
        Cita cita = citaService.findById(idCita);
        if(disponibilidadService.validarSiPuedeReprogramar(cita)){
            Usuario usuario = usuario(authentication);
            Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
            cita.setDisponibilidad(disponibilidad);
            cita.setNumReprogramaciones(cita.getNumReprogramaciones() + 1);
            cita.setUltimaReprogramacion(LocalDateTime.now());
            citaService.save(cita);

            String titulo = "Actualizacion en tu cita. Reprogramacion.";
            String mensajeVendedor = "Tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Ha sido reprogramada por el comprador: "+ cita.getProducto().getVendedor().getNombres() + ". para la nueva fecha: " + cita.getDisponibilidad().getFecha() + ". Y hora: " + cita.getDisponibilidad().getHora() + ".";
            String mensaje = "Has reprogramado tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Para la nueva fecha: " + cita.getDisponibilidad().getFecha() + ". Y hora: " + cita.getDisponibilidad().getHora() + ". Recuerda que solo puedes reprogramar 2 veces por cita, despues de esto tendras que esperar 24 horas para poder volver a reprogramar.";

            notificacionService.crearNotificacionAutomatica(titulo, mensajeVendedor, "Citas", cita.getProducto().getVendedor(), idCita, "/vendedor/citas");
            notificacionService.crearNotificacionAutomatica(titulo, mensaje, "Citas", cita.getComprador(), idCita, "/comprador/citas");
        } else {
            redirectAttributes.addFlashAttribute("esperar24Horas", true);
        }

        return "redirect:/comprador/citas";
    }

    @PostMapping("/mi-perfil/ser-vendedor")
    public String serVendedor(Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuario(authentication);

        usuarioService.volverVendedor(usuario(authentication).getCedula());
        List<GrantedAuthority> authorities = new ArrayList<>();
        usuario.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getNombreRol().toString().toUpperCase())));
        UsernamePasswordAuthenticationToken nuevoAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(nuevoAuth);

        redirectAttributes.addFlashAttribute("vendedorExitoso", true);

        return "redirect:/usuarios/mi-perfil?id=1";
    }

    @PostMapping("/generar-venta")
    public String generarVenta(@RequestParam(name = "idCita") Long idCita, Authentication authentication, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuario(authentication);

        Cita cita = citaService.findById(idCita);

        ventaService.generarVenta(cita.getProducto().getIdProducto(), cita.getComprador());
        citaService.cambiarEstado(cita, EstadoCitaEnum.FINALIZADA);

        String titulo = "Creacion de compra";
        String mensaje = "Se ha creado una nueva compra para el producto: " + cita.getProducto().getNombreProducto() + ". Ve al panel de compras para mas detalles. Recuerda que tu debes confirmar los datos obligatorios de la venta para finalizarla.";
        String mensajeVendedor = "El comprador: " + cita.getComprador().getNombres() + ". Ha generado una venta para tu producto: " + cita.getProducto().getNombreProducto() + ". Recuerda que debes actualizar los datos obligatorios para finalziar la venta.";

        notificacionService.crearNotificacionAutomatica(titulo, mensaje, "Ventas", usuario, cita.getIdCita(), "/comprador/compras");
        notificacionService.crearNotificacionAutomatica(titulo, mensajeVendedor, "Ventas", cita.getProducto().getVendedor(), cita.getIdCita(), "/vendedor/ventas");

        redirectAttributes.addFlashAttribute("ventaGenerada", true);
        return "redirect:/comprador/compras";
    }
}
