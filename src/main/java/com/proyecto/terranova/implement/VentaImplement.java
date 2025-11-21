package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoProductoEnum;
import com.proyecto.terranova.config.enums.EstadoVentaEnum;
import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.*;
import com.proyecto.terranova.service.NotificacionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.proyecto.terranova.service.VentaService;
import com.proyecto.terranova.dto.VentaDTO;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VentaImplement implements VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaGanadoRepository ventaGanadoRepository;

    @Autowired
    private VentaTerrenoRepository ventaTerrenoRepository;

    @Autowired
    private VentaFincaRepository ventaFincaRepository;

    @Autowired
    private GanadoRepository ganadoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private InfoComprobanteRepository infoComprobanteRepository;

    @Override
    public Venta actualizarEstado(Venta venta, EstadoVentaEnum estado) {
        Venta ventaActualizada = repository.findById(venta.getIdVenta()).orElseThrow();
        ventaActualizada.setEstado(estado);
        repository.save(ventaActualizada);
        return ventaActualizada;
    }

    @Override
    public Venta save(Venta venta) {
        return repository.save(venta);
    }

    @Override
    public VentaDTO update(Long id, VentaDTO dto) {
        Venta entidadVenta = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrado"));

    	modelMapper.map(dto, entidadVenta);

    	Venta entidadActualizada = repository.save(entidadVenta);
    	return modelMapper.map(entidadActualizada, VentaDTO.class);
    }

    @Override
    public Venta findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrado"));
    }

    @Override
    public List<VentaDTO> findAll() {
        return repository.findAll().stream()
            .map(entity -> modelMapper.map(entity, VentaDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Venta> encontrarPorVendedor(Usuario vendedor) {
        return repository.findByVendedor(vendedor);
    }

    @Override
    public List<Venta> encontrarPorComprador(Usuario comprador) {
        return repository.findByComprador(comprador);
    }

    @Override
    public boolean delete(Long id) {
        if(!repository.existsById(id)){
               return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existePorCita(Long idCita)
    {
        return repository.existsByCita(citaRepository.findById(idCita).orElseThrow());
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public Venta generarVenta(Long idCita) {
        Cita cita = citaRepository.findById(idCita).orElseThrow();
        Producto producto = cita.getProducto();
        producto.setEstado(EstadoProductoEnum.NO_DISPONIBLE);
        productoRepository.save(producto);

        Venta venta = new Venta();
        venta.setCita(cita);
        venta.setEstado(EstadoVentaEnum.EN_PROCESO);
        venta.setFechaInicioVenta(LocalDateTime.now());
        venta.setProducto(producto);
        venta.setVendedor(producto.getVendedor());

        Venta ventaGenerada = repository.save(venta);

        String tipoProducto = producto.getClass().getSimpleName();

        switch (tipoProducto) {
            case "Ganado":
                VentaGanado ventaGanado = new VentaGanado();
                ventaGanado.setVenta(ventaGenerada);
                ventaGanadoRepository.save(ventaGanado);
                break;
            case "Terreno":
                VentaTerreno ventaTerreno = new VentaTerreno();
                ventaTerreno.setVenta(ventaGenerada);
                ventaTerrenoRepository.save(ventaTerreno);
                break;
            case "Finca":
                VentaFinca ventaFinca = new VentaFinca();
                ventaFinca.setVenta(ventaGenerada);
                ventaFincaRepository.save(ventaFinca);
                break;
        }

        return ventaGenerada;
    }

    @Override
    public void seleccionarComprador(Long idVenta, String cedulaComprador) {
        Venta venta = repository.findById(idVenta).orElseThrow();
        venta.setComprador(usuarioRepository.findById(cedulaComprador).orElseThrow());
        venta.setPasoActual(2);
        repository.save(venta);
    }

    @Override
    public void actualizarVentaPaso2Ganado(Long idVenta, Long precioTotal, int cantidad, String observaciones) throws IOException {
        Venta venta = repository.findById(idVenta).orElseThrow();
        Ganado ganado = ganadoRepository.findById(venta.getProducto().getIdProducto()).orElseThrow();

        VentaGanado ventaGanado = ventaGanadoRepository.findByVenta(venta);
        ventaGanado.setObservacionesSanitarias(observaciones);
        ventaGanado.setVenta(venta);

        if(!Objects.equals(precioTotal, venta.getProducto().getPrecioProducto()) || cantidad != ganado.getCantidad()){
            ventaGanado.setCantidadNegociada(cantidad);
            ventaGanado.setPrecioNegociado(precioTotal);
            venta.setPendienteConfirmacion(true);
            repository.save(venta);

            notificacionService.notificacionContraoferta(venta);
        }else {
            venta.setPasoActual(3);
        }
        repository.save(venta);

        ventaGanadoRepository.save(ventaGanado);
    }

    @Override
    public void aceptarNegociacion(Long idVenta, String respuesta, String razonRechazo, int cantidad, Long precio) {
        Venta venta = repository.findById(idVenta).orElseThrow();

        if (respuesta.equals("RECHAZA")){
            venta.setPendienteConfirmacion(false);
            venta.setRazonRechazo(razonRechazo);
        }

        if (respuesta.equals("ACEPTA")){
            Ganado ganado = ganadoRepository.findById(venta.getProducto().getIdProducto()).orElseThrow();
            ganado.setCantidad(cantidad);
            venta.getProducto().setPrecioProducto(precio);
            venta.setPasoActual(3);

            ganadoRepository.save(ganado);
        }

        repository.save(venta);
    }

    @Override
    public void actualizarVentaPaso3Ganado(
            Long idVenta,
            MultipartFile gsmi,
            MultipartFile certificadoSanitario,
            MultipartFile facturaPropiedad,
            MultipartFile inventarioLote,
            MultipartFile certificadoSinigan,
            MultipartFile certificadoHierro,
            MultipartFile certificadoPesaje,
            String observacionesSanitarias
    ) throws IOException {

        Venta venta = repository.findById(idVenta).orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + idVenta));

        // Actualizar observaciones (si aplica) -- usa ventaDetalle solo para campos específicos
        Object ventaDetalle = obtenerVentaDetalle(venta, venta.getProducto().getClass().getSimpleName().toUpperCase());
        if (observacionesSanitarias != null && !observacionesSanitarias.isEmpty()) {
            actualizarObservaciones(ventaDetalle, observacionesSanitarias);
        }

        // Procesar cada archivo (si existe)
        if (gsmi != null && !gsmi.isEmpty()) {
            procesarComprobante(venta, gsmi, NombreComprobanteEnum.GSMI);
        }

        if (certificadoSanitario != null && !certificadoSanitario.isEmpty()) {
            procesarComprobante(venta, certificadoSanitario, NombreComprobanteEnum.CERTIFICADO_SANITARIO);
        }

        if (facturaPropiedad != null && !facturaPropiedad.isEmpty()) {
            procesarComprobante(venta, facturaPropiedad, NombreComprobanteEnum.FACTURA_PROPIEDAD);
        }

        if (inventarioLote != null && !inventarioLote.isEmpty()) {
            procesarComprobante(venta, inventarioLote, NombreComprobanteEnum.INVENTARIO_LOTE);
        }

        if (certificadoSinigan != null && !certificadoSinigan.isEmpty()) {
            procesarComprobante(venta, certificadoSinigan, NombreComprobanteEnum.CERTIFICADO_SINIGAN);
        }

        if (certificadoHierro != null && !certificadoHierro.isEmpty()) {
            procesarComprobante(venta, certificadoHierro, NombreComprobanteEnum.CERTIFICADO_HIERRO);
        }

        if (certificadoPesaje != null && !certificadoPesaje.isEmpty()) {
            procesarComprobante(venta, certificadoPesaje, NombreComprobanteEnum.CERTIFICADO_PESAJE);
        }

        repository.save(venta);
    }

    private Object obtenerVentaDetalle(Venta venta, String tipoProducto) {
        return switch (tipoProducto) {
            case "GANADO" -> ventaGanadoRepository.findByVenta(venta);
            case "TERRENO" -> ventaTerrenoRepository.findByVenta(venta);
            case "FINCA" -> ventaFincaRepository.findByVenta(venta);
            default -> throw new IllegalArgumentException("Tipo de producto no válido");
        };
    }

    private void actualizarObservaciones(Object ventaDetalle, String observaciones) {
        if (ventaDetalle instanceof VentaGanado ventaGanado) {
            ventaGanado.setObservacionesSanitarias(observaciones);
            ventaGanadoRepository.save(ventaGanado);
        }
    }

    private void procesarComprobante(Venta venta, MultipartFile archivo, NombreComprobanteEnum nombreComprobante) throws IOException {

        String nombreArchivo = guardarArchivo(archivo);

        InfoComprobante info = infoComprobanteRepository.findByNombreComprobante(nombreComprobante);

        Optional<Comprobante> optionalExistente = comprobanteRepository.findByVentaAndInfoComprobante(venta, info);

        if (optionalExistente.isPresent()) {
            Comprobante existente = optionalExistente.get();
            if (existente.getRutaArchivo() != null) {
                eliminarArchivo(existente.getRutaArchivo());
            }
            existente.setRutaArchivo(nombreArchivo);
            existente.setFechaSubida(LocalDateTime.now());
            comprobanteRepository.save(existente);
        } else {
            Comprobante nuevo = new Comprobante();
            nuevo.setVenta(venta);
            nuevo.setInfoComprobante(info);
            nuevo.setRutaArchivo(nombreArchivo);
            nuevo.setFechaSubida(LocalDateTime.now());
            comprobanteRepository.save(nuevo);
        }
    }

    private void eliminarArchivo(String nombreArchivo) {
        try {
            Path rutaArchivo = Paths.get("uploads/documentos/").resolve(nombreArchivo);
            Files.deleteIfExists(rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al eliminar archivo: " + e.getMessage());
        }
    }

    private String guardarArchivo(MultipartFile archivo) throws IOException {
        String directorioDocumentos = "uploads/documentos/";
        Path ruta = Paths.get(directorioDocumentos);

        if (!Files.exists(ruta)) {
            Files.createDirectories(ruta);
        }

        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaCompleta = ruta.resolve(nombreArchivo);

        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

        return nombreArchivo;
    }
}