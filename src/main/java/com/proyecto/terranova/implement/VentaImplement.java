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
        Venta venta = repository.findById(idVenta).orElseThrow();
        String tipoProducto = venta.getProducto().getClass().getSimpleName().toUpperCase();

        Object ventaDetalle = obtenerVentaDetalle(venta, tipoProducto);

        if (observacionesSanitarias != null && !observacionesSanitarias.isEmpty()) {
            actualizarObservaciones(ventaDetalle, observacionesSanitarias);
        }

        //aqudocumentos obligatorios
        if (gsmi != null && !gsmi.isEmpty()) {
            procesarComprobante(ventaDetalle, gsmi, NombreComprobanteEnum.GSMI);
        }

        if (certificadoSanitario != null && !certificadoSanitario.isEmpty()) {
            procesarComprobante(ventaDetalle, certificadoSanitario, NombreComprobanteEnum.CERTIFICADO_SANITARIO);
        }

        if (facturaPropiedad != null && !facturaPropiedad.isEmpty()) {
            procesarComprobante(ventaDetalle, facturaPropiedad, NombreComprobanteEnum.FACTURA_PROPIEDAD);
        }

        //documentos opcionales
        if (inventarioLote != null && !inventarioLote.isEmpty()) {
            procesarComprobante(ventaDetalle, inventarioLote, NombreComprobanteEnum.INVENTARIO_LOTE);
        }

        if (certificadoSinigan != null && !certificadoSinigan.isEmpty()) {
            procesarComprobante(ventaDetalle, certificadoSinigan, NombreComprobanteEnum.CERTIFICADO_SINIGAN);
        }

        if (certificadoHierro != null && !certificadoHierro.isEmpty()) {
            procesarComprobante(ventaDetalle, certificadoHierro, NombreComprobanteEnum.CERTIFICADO_HIERRO);
        }

        if (certificadoPesaje != null && !certificadoPesaje.isEmpty()) {
            procesarComprobante(ventaDetalle, certificadoPesaje, NombreComprobanteEnum.CERTIFICADO_PESAJE);
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

    private void procesarComprobante(Object ventaDetalle, MultipartFile archivo, NombreComprobanteEnum nombreComprobante) throws IOException {

        // primero guardo el archivo en la carpeta uploads
        String rutaArchivo = guardarArchivo(archivo);

        // despues agarro el infocomprobante
        InfoComprobante infoComprobante = obtenerOCrearInfoComprobante(ventaDetalle, nombreComprobante);

        // aqui valido si existe un comprobante
        Comprobante comprobanteExistente = infoComprobante.getComprobante();

        if (comprobanteExistente != null) {
            // Eliminar el archivo anterior del sistema de archivos
            eliminarArchivo(comprobanteExistente.getRutaArchivo());

            // Actualizar el comprobante existente
            comprobanteExistente.setRutaArchivo(rutaArchivo);
            comprobanteExistente.setFechaSubida(LocalDateTime.now());
            comprobanteRepository.save(comprobanteExistente);
        } else {
            Comprobante nuevoComprobante = new Comprobante();
            nuevoComprobante.setRutaArchivo(rutaArchivo);
            nuevoComprobante.setFechaSubida(LocalDateTime.now());
            nuevoComprobante.setInfoComprobante(infoComprobante);

            comprobanteRepository.save(nuevoComprobante);

            infoComprobante.setComprobante(nuevoComprobante);
            infoComprobanteRepository.save(infoComprobante);
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

    private InfoComprobante obtenerOCrearInfoComprobante(Object ventaDetalle, NombreComprobanteEnum nombreComprobante) {

        Map<NombreComprobanteEnum, InfoComprobante> comprobantesInfo = null;

        if (ventaDetalle instanceof VentaGanado ventaGanado) {
            comprobantesInfo = ventaGanado.getComprobantesInfo();
        } else if (ventaDetalle instanceof VentaTerreno ventaTerreno) {
            comprobantesInfo = ventaTerreno.getComprobantesInfo();
        } else if (ventaDetalle instanceof VentaFinca ventaFinca) {
            comprobantesInfo = ventaFinca.getComprobantesInfo();
        }

        InfoComprobante infoComprobante = comprobantesInfo != null ? comprobantesInfo.get(nombreComprobante) : null;

        if (infoComprobante == null) {
            infoComprobante = new InfoComprobante();
            infoComprobante.setNombreComprobante(nombreComprobante);

            if (ventaDetalle instanceof VentaGanado ventaGanado) {
                infoComprobante.setVentaGanado(ventaGanado);
            } else if (ventaDetalle instanceof VentaTerreno ventaTerreno) {
                infoComprobante.setVentaTerreno(ventaTerreno);
            } else if (ventaDetalle instanceof VentaFinca ventaFinca) {
                infoComprobante.setVentaFinca(ventaFinca);
            }

            infoComprobante = infoComprobanteRepository.save(infoComprobante);
        }

        return infoComprobante;
    }
}