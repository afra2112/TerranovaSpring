package com.proyecto.terranova.implement;

import com.proyecto.terranova.config.enums.EstadoTransporteEnum;
import com.proyecto.terranova.config.enums.EstadoVentaEnum;
import com.proyecto.terranova.config.enums.TipoArchivoTransporteEnum;
import com.proyecto.terranova.entity.ArchivosTransportes;
import com.proyecto.terranova.entity.Transporte;
import com.proyecto.terranova.entity.Venta;
import com.proyecto.terranova.repository.ArchivosTransporteRepository;
import com.proyecto.terranova.repository.TransporteRepository;
import com.proyecto.terranova.repository.VentaRepository;
import com.proyecto.terranova.service.TransporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class TransporteImplement implements TransporteService {
    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private ArchivosTransporteRepository archivosTransporteRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public Transporte programarTransporte(
            Long idVenta,
            LocalDate fechaTransporte,
            LocalTime horaTransporte,
            String empresaTransporte,
            String placaVehiculo,
            String nombreConductor,
            String telefonoConductor,
            String cedulaConductor,
            String puntoEntrega,
            String observacionesComprador
    ) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Transporte transporte = new Transporte();
        transporte.setVenta(venta);
        transporte.setFechaTransporte(fechaTransporte);
        transporte.setHoraTransporte(horaTransporte);
        transporte.setEmpresaTransporte(empresaTransporte);
        transporte.setPlacaVehiculo(placaVehiculo);
        transporte.setNombreConductor(nombreConductor);
        transporte.setTelefonoConductor(telefonoConductor);
        transporte.setCedulaConductor(cedulaConductor);
        transporte.setPuntoEntrega(puntoEntrega);
        transporte.setObservacionesComprador(observacionesComprador);
        transporte.setEstadoTransporte(EstadoTransporteEnum.PROGRAMADO);

        transporteRepository.save(transporte);

        venta.setPasoActual(5);
        ventaRepository.save(venta);

        return transporte;
    }

    @Override
    public void confirmarCarga(
            Long idTransporte,
            MultipartFile fotoCamionVacio,
            MultipartFile fotoCargaGanado,
            MultipartFile fotoGsmiConductor,
            String observacionesVendedor
    ) throws IOException {
        Transporte transporte = transporteRepository.findById(idTransporte)
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        guardarArchivoTransporte(transporte, fotoCamionVacio, TipoArchivoTransporteEnum.CAMION_VACIO, "Camión vacío antes de cargar");
        guardarArchivoTransporte(transporte, fotoCargaGanado, TipoArchivoTransporteEnum.CARGA_GANADO, "Carga del ganado");
        guardarArchivoTransporte(transporte, fotoGsmiConductor, TipoArchivoTransporteEnum.GSMI_CONDUCTOR, "GSMI y conductor");

        transporte.setEstadoTransporte(EstadoTransporteEnum.EN_CAMINO);
        transporte.setFechaHoraCarga(LocalDateTime.now());
        transporte.setObservacionesVendedor(observacionesVendedor);

        transporteRepository.save(transporte);
    }

    @Override
    public void confirmarRecepcion(
            Long idTransporte,
            MultipartFile fotoGanadoDescargado,
            MultipartFile fotoGanadoNuevoLote
    ) throws IOException {
        Transporte transporte = transporteRepository.findById(idTransporte)
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        guardarArchivoTransporte(transporte, fotoGanadoDescargado, TipoArchivoTransporteEnum.GANADO_DESCARGADO, "Ganado descargado");
        guardarArchivoTransporte(transporte, fotoGanadoNuevoLote, TipoArchivoTransporteEnum.GANADO_NUEVO_LOTE, "Ganado en nuevo lote");

        transporte.setEstadoTransporte(EstadoTransporteEnum.COMPLETADO);
        transporte.setFechaHoraEntrega(LocalDateTime.now());
        transporteRepository.save(transporte);

        Venta venta = transporte.getVenta();
        venta.setEstado(EstadoVentaEnum.FINALIZADA);
        ventaRepository.save(venta);
    }

    @Override
    public Transporte encontrarPorId(Long idTransporte) {
        return transporteRepository.findById(idTransporte).orElseThrow();
    }


    private void guardarArchivoTransporte(Transporte transporte, MultipartFile archivo, TipoArchivoTransporteEnum tipo, String descripcion) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Archivo " + tipo + " es obligatorio");
        }

        String rutaArchivo = guardarArchivo(archivo);

        ArchivosTransportes archivoTransporte = new ArchivosTransportes();
        archivoTransporte.setTransporte(transporte);
        archivoTransporte.setTipoArchivo(tipo);
        archivoTransporte.setNombreArchivo(archivo.getOriginalFilename());
        archivoTransporte.setRutaArchivo(rutaArchivo);
        archivoTransporte.setDescripcion(descripcion);
        archivoTransporte.setFechaSubida(LocalDateTime.now());

        archivosTransporteRepository.save(archivoTransporte);
    }

    private String guardarArchivo(MultipartFile archivo) throws IOException {
        String directorioTransporte = "uploads/transporte/";
        Path ruta = Paths.get(directorioTransporte);

        if (!Files.exists(ruta)) {
            Files.createDirectories(ruta);
        }

        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaCompleta = ruta.resolve(nombreArchivo);

        Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

        return nombreArchivo;
    }

    @Override
    public Transporte obtenerPorVenta(Venta venta) {
        return transporteRepository.findByVenta(venta).orElse(null);
    }
}
