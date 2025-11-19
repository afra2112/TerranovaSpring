package com.proyecto.terranova.controller;

import com.proyecto.terranova.config.enums.EstadoAsistenciaEnum;
import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.*;
import com.proyecto.terranova.specification.ProductoSpecification;
import jakarta.mail.MessagingException;
import com.proyecto.terranova.service.CompradorService;
import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.*;

@Controller
@RequestMapping("/comprador")
public class CompradorController {

    @Autowired
    CompradorService compradorService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AsistenciaService asistenciaService;

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    ProductoService productoService;

    @Autowired
    CitaService citaService;

    @Autowired
    VentaService ventaService;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    FavoritoService favoritoService;
    @Autowired
    private AsistenciaRepository asistenciaRepository;

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
        List<Asistencia> asistencias = asistenciaService.encontrarPorCompradorYEstado(usuario(authentication), EstadoAsistenciaEnum.INSCRITO);
        List<Long> favoritosIds = favoritoService.obtenerIdsFavoritosPorUsuario(usuario(authentication));

        model.addAttribute("favoritosIds", favoritosIds);
        model.addAllAttributes(estadisticas);
        model.addAttribute("asistenciasCant", asistencias.size());
        model.addAttribute("notificacionesCant", notificacionService.contarNoLeidasPorUsuario(usuario(authentication), false));
        model.addAttribute("asistencias", asistencias.stream().limit(2).toList());
        model.addAttribute("productos", productoService.obtenerTodasMenosVendedor(usuario(authentication)).stream().limit(3).toList());
        return "comprador/principalComprador";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        Usuario usuario = usuario(authentication);

        List<Asistencia> asistenciasProximas = asistenciaService.encontrarPorCompradorYEstado(usuario, EstadoAsistenciaEnum.INSCRITO);
        asistenciasProximas.removeIf(asistencia -> asistencia.getCita().getEstadoCita() == EstadoCitaEnum.FINALIZADA);
        List<Asistencia> asistenciasEnEspera = asistenciaService.encontrarPorCompradorYEstado(usuario, EstadoAsistenciaEnum.EN_ESPERA);
        List<Asistencia> asistenciasPasadasAsistidas = asistenciaService.encontrarPorCompradorYEstado(usuario, EstadoAsistenciaEnum.ASISTIO);
        List<Asistencia> asistenciasPasadasNoAsistidas = asistenciaService.encontrarPorCompradorYEstado(usuario, EstadoAsistenciaEnum.NO_ASISTIO);

        model.addAttribute("posicionCitas", true);
        model.addAttribute("asistenciasProximas", asistenciasProximas);
        model.addAttribute("asistenciasEnEspera", asistenciasEnEspera);
        List<Asistencia> asistenciasProximasYEnEspera = new ArrayList<>();
        asistenciasProximasYEnEspera.addAll(asistenciasProximas);
        asistenciasProximasYEnEspera.addAll(asistenciasEnEspera);
        asistenciasProximasYEnEspera.removeIf(asistencia -> asistencia.getCita().getEstadoCita() == EstadoCitaEnum.FINALIZADA);
        asistenciasProximasYEnEspera.sort(Comparator.comparing(a -> a.getCita().getFecha()));
        List<Asistencia> asistenciasPasadasAsistidasYNoAsistidas = new ArrayList<>();
        asistenciasPasadasAsistidasYNoAsistidas.addAll(asistenciasPasadasAsistidas);
        asistenciasPasadasAsistidasYNoAsistidas.addAll(asistenciasPasadasNoAsistidas);
        asistenciasPasadasAsistidasYNoAsistidas.sort(Comparator.comparing(a -> a.getCita().getFecha()));
        model.addAttribute("asistenciasPasadasAsistidasYNoAsistidas", asistenciasPasadasAsistidasYNoAsistidas);
        model.addAttribute("asistenciasProximasYEnEspera", asistenciasProximasYEnEspera);
        return "comprador/citas";
    }

    @GetMapping("/citas/detalle/{id}")
    public String detalleCitas(@PathVariable Long id, Model model,Authentication authentication){
        model.addAttribute("cita", citaService.findById(id));

        List<Asistencia> listaAsistencia = asistenciaService.encontrarAsistenciasPorCita(id);
        Asistencia asistencia = asistenciaRepository.findByCita_IdCitaAndUsuario(id,usuario(authentication));

        Integer posicion = null;
        if(asistencia != null && asistencia.getEstado() == EstadoAsistenciaEnum.EN_ESPERA){
            posicion = asistenciaService.obtenerPosicionDeUsuarioEnListaDeEspera(id, usuario(authentication).getCedula());
        }

        model.addAttribute("esDueno", false);
        model.addAttribute("asistencias", listaAsistencia);
        model.addAttribute("miAsistencia", asistencia);
        model.addAttribute("posicionEnEspera", posicion);
        return "vistasTemporales/detalleCita";
    }

    @PostMapping("/citas/inscribirse/{id}")
    public String inscribirseACita(@PathVariable Long id, @RequestParam EstadoAsistenciaEnum estado, Authentication authentication){
        asistenciaService.crearAsistencia(usuario(authentication), id, estado);
        return "redirect:/comprador/citas";
    }

    @PostMapping("/citas/cancerlar/asistencia/{id}")
    public String cancelarAsistencia(@PathVariable Long id) throws MessagingException, IOException {
        asistenciaService.cancelarAsistencia(id);
        return "redirect:/comprador/citas";
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
    public String reservar(@RequestParam(name = "idCita") Long idCita, Authentication authentication) throws MessagingException, IOException {
        Usuario usuario = usuario(authentication);
        Cita cita = citaService.findById(idCita);

        if (!asistenciaService.existeCualquierAsistenciaPorUsuario(usuario, cita.getProducto().getIdProducto())){

            Asistencia asistencia = new Asistencia();

            asistencia.setCita(cita);
            asistencia.setUsuario(usuario);
            asistencia.setEstado(EstadoAsistenciaEnum.INSCRITO);
            asistencia.setFechaInscripcion(LocalDateTime.now());

            asistenciaService.save(asistencia);

        }

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

    /*@PostMapping("/citas/reprogramar-cita")
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
    }*/



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

        Venta venta = ventaService.generarVenta(cita.getProducto().getIdProducto(), null/*comprador*/);
        citaService.cambiarEstado(cita, EstadoCitaEnum.FINALIZADA);

        notificacionService.notificacionVentaGenerada(venta);

        redirectAttributes.addFlashAttribute("ventaGenerada", true);
        return "redirect:/comprador/compras";
    }

    @PostMapping("/agregarF/{id}")
    public String agregarFavoritos(@PathVariable Long id, @RequestParam(name = "vieneDe") String vieneDe, Authentication auth){
        String correo = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(correo);
        System.out.println("------------------usuario autenticado en el controlador-------"+correo);
        Producto producto = productoRepository.findById(id).orElseThrow();
        favoritoService.agregarFavorito(usuario,producto);

        switch (vieneDe) {
            case "detalle":
                return "redirect:/detalle-producto/"+id;
            case "dashboard":
                return "redirect:/comprador/explorar";
            case "productos":
                return "redirect:/productos";
        }
        return "";
    }

    @PostMapping("/favoritos/eliminar/{id}")
    public String eliminarFavorito(@PathVariable Long id,@RequestParam(name = "vieneDe") String vieneDe ,Authentication auth) {
        Usuario usuario = usuarioService.findByEmail(auth.getName());
        Producto producto = productoRepository.findById(id).orElseThrow();
        favoritoService.eliminarFavorito(usuario, producto);

        switch (vieneDe) {
            case "detalle":
                return "redirect:/detalle-producto/"+id;
            case "dashboard":
                return "redirect:/comprador/explorar";
            case "productos":
                return "redirect:/productos";
        }
        return "";
    }

    @GetMapping("/Favorito")
    public String mostrarFavorito(Model model,  Authentication auth){
        String correo = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(correo);

        List<Favorito> favoritos = favoritoService.obtenerFavoritos(usuario);
        List<Producto> productos = favoritos.stream()
                .map(Favorito::getProducto)
                .toList();

        model.addAttribute("productos", productos);
        return "comprador/Favoritos";
    }

    @PostMapping("/codigo-Verificacion")
    public String codigoVerificacion(RedirectAttributes redirectAttributes, Authentication authentication) throws MessagingException, IOException{
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        String email = usuario.getEmail();
        usuarioService.generarCodigoVerificacionYEnviarCorreo(email);
        redirectAttributes.addFlashAttribute("mostrarModal", true);
        return "redirect:/usuarios/mi-perfil?id=1";

    }

    @PostMapping("/Vcodigo")
    @ResponseBody
    public Map<String, Object> vcodigo(@RequestParam String codigo, Authentication authentication){
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = usuarioService.findByEmail(authentication.getName());

        if (usuario == null || usuario.getCodigoVerificacion() == null){
            response.put("success", false);
            response.put("error", "Usuario no encontrado o código no generado.");
            return response;

        }
        if(usuario.getCodigoVerificacionExpiracion().isBefore(LocalDateTime.now())){
            response.put("success", false);
            response.put("error", "Código de verificación expirado.");
            return response;
        }
        if (!usuario.getCodigoVerificacion().equals(codigo)) {
            response.put("success", false);
            response.put("error", "Código incorrecto.");
            return response;
        }

        // Limpieza del código
        usuario.setCodigoVerificacion(null);
        usuario.setCodigoVerificacionExpiracion(null);
        usuarioRepository.save(usuario);
        response.put("success", true);
        response.put("redirect", "/mi-perfil/ser-vendedor");
        return response;
    }

}
