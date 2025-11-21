package com.proyecto.terranova.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import com.proyecto.terranova.config.enums.*;
import com.proyecto.terranova.dto.ComprobanteDTO;
import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.*;
import com.proyecto.terranova.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    VentaService ventaService;

    @Autowired
    ProductoService productoService;

    @Autowired
    CitaService citaService;

    @Autowired
    AsistenciaService asistenciaService;

    @Autowired
    AsistenciaRepository asistenciaRepository;

    @Autowired
    CiudadRepository ciudadRepository;

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    ComprobanteRepository comprobanteRepository;

    @Autowired
    ModelMapper modelMapper;

    @ModelAttribute("esVendedor")
    public boolean esVendedor(Authentication authentication){
        List<RolEnum> rolesUsuario = usuarioService.obtenerNombresRoles(usuario(authentication));

        boolean esVendedor = false;
        if(rolesUsuario.contains(RolEnum.VENDEDOR)){
            esVendedor = true;
        }
        return esVendedor;
    }

    @ModelAttribute
    public Usuario usuario(Authentication authentication){
        return usuarioService.findByEmail(authentication.getName());
    }

    @ModelAttribute("nombreMostrar")
    public String nombreMostrar(Authentication authentication){
        Usuario usuario = usuarioService.findByEmail(authentication.getName());
        return usuario.getNombres() + ". " + usuario.getApellidos().charAt(0);
    }

    @GetMapping("/dashboard")
    public String indexVendedor(@RequestParam(required = false) Long productoId,Model model, Authentication authentication) {
        Usuario usuario = usuario(authentication);

        if (productoId != null) {
            model.addAttribute("productoId", productoId);
        }
        model.addAttribute("ciudades", ciudadRepository.findAll());
        model.addAttribute("dashboard", true);
        model.addAttribute("totalVentas", ventaService.encontrarPorVendedor(usuario).size());
        model.addAttribute("productos", productoService.obtenerTodosPorVendedor(usuario).stream().limit(3).toList());
        model.addAttribute("totalCitas", citaService.encontrarPorVendedor(usuario, true).size());
        model.addAttribute("notificaciones", notificacionService.obtenerPorUsuarioYLeido(usuario,true));
        return "vendedor/dashboard";
    }

    @GetMapping("/mi-calendario")
    public String calendario(Model model, Authentication authentication){
        model.addAttribute("calendario", true);
        model.addAttribute( "productos", productoService.obtenerTodosPorVendedorYEstado(usuario(authentication), EstadoProductoEnum.DISPONIBLE));
        model.addAttribute("cedula", usuario(authentication).getCedula());
        return "vendedor/calendario";
    }

    @GetMapping("/citas")
    public String citas(Model model, Authentication authentication){
        Usuario vendedor = usuarioService.findByEmail(authentication.getName());
        model.addAttribute("posicionCitas", true);

        List<Cita> citas = citaService.encontrarPorVendedor(vendedor, true);
        for (Cita cita : citas){
            cita.setTieneVenta(ventaService.existePorCita(cita.getIdCita()));
        }

        model.addAttribute("citas", citas);

        return "vendedor/citas";
    }


    @GetMapping("/ventas")
    public String ventas(Model model, Authentication authentication) {

        List<Venta> ventas = ventaService.encontrarPorVendedor(
                usuarioService.findByEmail(authentication.getName())
        );

        long ingresosTotales = 0;
        long gastosTotales = 0;

        for (Venta v : ventas) {
            ingresosTotales += v.getProducto().getPrecioProducto();
            gastosTotales += v.getTotalGastos();
        }

        long balanceFinal = ingresosTotales - gastosTotales;

        String nombreCompleto =
                usuario(authentication).getNombres() + " " +
                        usuario(authentication).getApellidos();

        List<VentaDTO> ventasDTO = ventas.stream()
                .map(venta -> {

                    VentaDTO dto = modelMapper.map(venta, VentaDTO.class);

                    List<Comprobante> comprobantesVenta =
                            comprobanteRepository.findByVenta(venta);

                    List<ComprobanteDTO> comprobantesDTO = comprobantesVenta.stream()
                            .map(comp -> {
                                InfoComprobante info = comp.getInfoComprobante();

                                ComprobanteDTO c = new ComprobanteDTO();
                                c.setIdComprobante(comp.getIdComprobante());
                                c.setRutaArchivo(comp.getRutaArchivo());
                                c.setFechaSubida(comp.getFechaSubida());
                                c.setNombreComprobante(info.getNombreComprobante());
                                c.setNombreMostrar(info.getNombreMostrar());
                                return c;
                            })
                            .toList();

                    dto.setListaComprobantes(comprobantesDTO);

                    return dto;
                })
                .toList();

        model.addAttribute("posicionVentas", true);
        model.addAttribute("ingresosTotales", ingresosTotales);
        model.addAttribute("gastosTotales", gastosTotales);
        model.addAttribute("balanceFinal", balanceFinal);
        model.addAttribute("ventas", ventasDTO);
        model.addAttribute("ventasOriginales", ventas);
        model.addAttribute("nombres", nombreCompleto);

        return "vendedor/ventas";
    }


    @PostMapping("/venta/enviar-peticion-venta")
    public String enviarPeticionVenta(@ModelAttribute Venta venta, @RequestParam(name = "comprobantes") int comprobantes, RedirectAttributes redirectAttributes, Authentication authentication) throws IOException {
        if(venta.getFechaInicioVenta() == null || (venta.getMetodoPago() == null || venta.getMetodoPago().isBlank()) || comprobantes == 0){
            redirectAttributes.addFlashAttribute("ventaIncompleta", true);
            return "redirect:/vendedor/ventas";
        }
        Venta ventaActual = ventaService.actualizarEstado(venta, EstadoVentaEnum.EN_PROCESO); //AQUI ORIGINALMENTE IBA EL ESTADO "Pendiente Confirmacion"


        redirectAttributes.addFlashAttribute("peticionHecha", true);
        return "redirect:/vendedor/ventas";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(required = false, name = "idProducto") Long idProducto,Model model, Authentication authentication){
        List<Producto> productos = productoService.obtenerTodosPorVendedor(usuario(authentication));

        model.addAttribute("posicionProductos", true);
        model.addAttribute("productos", productos);
        model.addAttribute("citasCant", citaService.encontrarPorVendedor(usuario(authentication), true).size());

        if(idProducto != null){
            model.addAttribute("producto", productoService.findById(idProducto));
            model.addAttribute("mostrarModalDisponibilidades", true);
            model.addAttribute("citas", citaService.encontrarPorProducto(idProducto));
            return "vendedor/productos";
        }
        return "vendedor/productos";
    }

    @GetMapping("/citas/detalle/{id}")
    public String detalleCitas(@PathVariable Long id, Model model,Authentication authentication){
        model.addAttribute("cita", citaService.findById(id));

        Asistencia asistencia = asistenciaRepository.findByCita_IdCitaAndUsuario(id,usuario(authentication));

        Integer posicion = null;
        if(asistencia != null && asistencia.getEstado() == EstadoAsistenciaEnum.EN_ESPERA){
            posicion = asistenciaService.obtenerPosicionDeUsuarioEnListaDeEspera(id, usuario(authentication).getCedula());
        }

        model.addAttribute("esDueno", true);
        model.addAttribute("asistentesConfirmados", asistenciaService.encontrarAsistenciasPorCitaYEstadoAsistencia(id, EstadoAsistenciaEnum.INSCRITO));
        model.addAttribute("asistentesEspera", asistenciaService.encontrarAsistenciasPorCitaYEstadoAsistencia(id, EstadoAsistenciaEnum.EN_ESPERA));
        return "comprador/detalleCita";
    }

    @GetMapping("/descargar/comprobante/{id}")
    public ResponseEntity<Resource> descargarComprobante(@PathVariable Long id) {
        try {
            Comprobante comprobante = comprobanteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comprobante no encontrado"));

            Path rutaArchivo = Paths.get("uploads/documentos/").resolve(comprobante.getRutaArchivo());
            Resource resource = new UrlResource(rutaArchivo.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + comprobante.getRutaArchivo() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el archivo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo", e);
        }
    }
}