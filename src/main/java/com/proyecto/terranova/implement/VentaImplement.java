package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.service.NotificacionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.VentaService;
import com.proyecto.terranova.repository.VentaRepository;
import com.proyecto.terranova.dto.VentaDTO;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VentaImplement implements VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Venta actualizarDatosVenta(Venta venta, List<Long> idsComprobantesEliminados, List<Long> idsGastosEliminados, List<MultipartFile> comprobantes) throws IOException {
        Venta ventaAcual = repository.findById(venta.getIdVenta()).orElseThrow();
        ventaAcual.setFechaVenta(venta.getFechaVenta());
        ventaAcual.setMetodoPago(venta.getMetodoPago());
        ventaAcual.setNota(venta.getNota());

        if(idsGastosEliminados != null){
            ventaAcual.getListaGastos().removeIf(gastoVenta -> idsGastosEliminados.contains(gastoVenta.getIdGasto()));
        }

        if(venta.getListaGastos() != null){
            for (GastoVenta gasto : venta.getListaGastos()){
                gasto.setVenta(ventaAcual);
                ventaAcual.getListaGastos().add(gasto);
            }
        }

        if(idsComprobantesEliminados != null){
            ventaAcual.getListaComprobantes().removeIf(comprobante -> idsComprobantesEliminados.contains(comprobante.getIdComprobante()));
        }

        if(venta.getListaComprobantes() != null && comprobantes != null){
            for (MultipartFile file : comprobantes){
                String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path rutaArchivo = Paths.get("archivos").resolve(nombreArchivo);
                Files.write(rutaArchivo, file.getBytes());

                Comprobante comprobante = new Comprobante();
                comprobante.setFechaSubida(LocalDateTime.now());
                comprobante.setVenta(ventaAcual);
                comprobante.setNombreComprobante(file.getOriginalFilename());
                comprobante.setRutaArchivo(nombreArchivo);
                ventaAcual.getListaComprobantes().add(comprobante);
            }
        }

        ventaAcual.setGananciaNeta(ventaAcual.getProducto().getPrecioProducto() - ventaAcual.getTotalGastos());

        repository.save(ventaAcual);

        return ventaAcual;
    }

    @Override
    public Venta actualizarEstado(Venta venta, String estado) {
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
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }


    @Override
    public Venta generarVenta(Long idProducto, Usuario comprador) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow();

        Venta venta = new Venta();
        venta.setComprador(comprador);
        venta.setEstado("En Proceso");
        venta.setFechaInicioVenta(LocalDateTime.now());
        venta.setProducto(producto);
        venta.setVendedor(producto.getVendedor());

        return repository.save(venta);
    }
}
