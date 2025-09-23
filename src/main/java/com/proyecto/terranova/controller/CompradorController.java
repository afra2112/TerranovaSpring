package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Cita;
import com.proyecto.terranova.entity.Disponibilidad;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.service.*;
import jakarta.mail.MessagingException;
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

import java.io.IOException;
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
    ProductoService productoService;

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
        model.addAttribute("productos", productoService.obtenerTodasMenosVendedor(usuario(authentication)));
        return "comprador/principalComprador";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        model.addAttribute("posicionCitas", true);
        model.addAttribute("citas", citaService.encontrarPorComprador(usuarioService.findByEmail(authentication.getName()), true));
        return "comprador/citas";
    }

    @GetMapping("/compras")
    public String compras(Model model, Authentication authentication){
        model.addAttribute("posicionCompras", true);
        model.addAttribute("compras", ventaService.encontrarPorComprador(usuario(authentication)));
        return "comprador/compras";
    }

    @PostMapping("/compras/actualizar-compra")
    public String actualizarCompra(RedirectAttributes redirectAttributes, @RequestParam(name = "idVenta") Long idVenta, @RequestParam(name = "accion") String accion, @RequestParam(name = "razon", required = false) String razon) throws MessagingException, IOException {
        Venta venta = ventaService.findById(idVenta);

        switch (accion){
            case "cancelar":
                venta.setGananciaNeta(0L);
                ventaService.actualizarEstado(venta, "Cancelada");
                redirectAttributes.addFlashAttribute("modalCancelar", true);
                break;
            case "finalizar":
                ventaService.actualizarEstado(venta, "Finalizada");
                redirectAttributes.addFlashAttribute("modalFinalizar", true);
                break;

            case "modificar":
                ventaService.actualizarEstado(venta, "En Proceso");
                redirectAttributes.addFlashAttribute("modalModificar", true);

                notificacionService.notificacionPedirModificarVenta(venta, razon);

                break;
        }
        return "redirect:/comprador/compras";
    }

    @PostMapping("/citas/reservar-cita")
    public String reservar(@RequestParam(name = "idDisponibilidad") Long idDisponibilidad, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);

        Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
        Cita cita = new Cita();
        cita.setEstadoCita(EstadoCitaEnum.RESERVADA);
        cita.setDisponibilidad(disponibilidad);
        cita.setNumReprogramaciones(0);
        cita.setComprador(usuario);
        cita.setProducto(disponibilidad.getProducto());
        citaService.save(cita);

        notificacionService.notificacionCitaReservada(cita);

        return "redirect:/comprador/citas";
    }

    @PostMapping("/citas/cancelar-cita")
    public String cancelarCita(@RequestParam(name = "idCita") Long idCita, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);
        Cita cita = citaService.findById(idCita);
        cita.setEstadoCita(EstadoCitaEnum.CANCELADA);
        citaService.save(cita);

        String titulo = "Actualizacion en tu cita. Cancelacion.";
        String mensajeVendedor = "Tu cita para el producto: " + cita.getProducto().getNombreProducto() + ". Ha sido cancelada por el comprador: " + usuario.getNombres() + ".";
        String mensaje = "Has cancelado tu cita para el producto: " + cita.getProducto().getNombreProducto() + ".";

        notificacionService.notificacionCitaCancelada(cita, usuario);

        return "redirect:/comprador/citas";
    }

    @PostMapping("/citas/reprogramar-cita")
    public String reprogramarCita(@RequestParam(name = "idCita") Long idCita, @RequestParam(name = "idDisponibilidad") Long idDisponibilidad, Authentication authentication, RedirectAttributes redirectAttributes) throws MessagingException, IOException {
        Cita cita = citaService.findById(idCita);
        if(disponibilidadService.validarSiPuedeReprogramar(cita)){
            Usuario usuario = usuario(authentication);
            Disponibilidad disponibilidad = disponibilidadService.findById(idDisponibilidad);
            cita.setDisponibilidad(disponibilidad);
            cita.setNumReprogramaciones(cita.getNumReprogramaciones() + 1);
            cita.setUltimaReprogramacion(LocalDateTime.now());
            citaService.save(cita);

            notificacionService.notificacionCitaReprogramada(cita,usuario);
        } else {
            if(cita.getUltimaReprogramacionBloqueada() == null){
                cita.setUltimaReprogramacionBloqueada(LocalDateTime.now());
                cita.setFechaHabilitarReprogramacion(LocalDateTime.now().plusSeconds(15));
                citaService.save(cita);
            }
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
    public String generarVenta(@RequestParam(name = "idCita") Long idCita, Authentication authentication, RedirectAttributes redirectAttributes) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);

        Cita cita = citaService.findById(idCita);

        Venta venta = ventaService.generarVenta(cita.getProducto().getIdProducto(), cita.getComprador());
        citaService.cambiarEstado(cita, EstadoCitaEnum.VENTAGENERADA);

        notificacionService.notificacionVentaGenerada(venta);

        redirectAttributes.addFlashAttribute("ventaGenerada", true);
        return "redirect:/comprador/compras";
    }
}
