package com.proyecto.terranova.service;

import java.io.IOException;
import java.util.List;

import com.proyecto.terranova.config.enums.EstadoVentaEnum;
import com.proyecto.terranova.config.enums.NombreComprobanteEnum;
import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface VentaService {
    Venta actualizarEstado(Venta venta, EstadoVentaEnum estado);
    Venta save(Venta venta);
    VentaDTO update(Long id, VentaDTO dto); // Actualizar
    Venta findById(Long id);
    List<VentaDTO> findAll();
    List<Venta> encontrarPorVendedor(Usuario vendedor);
    List<Venta> encontrarPorComprador(Usuario comprador);
    boolean delete(Long id);
    boolean existePorCita(Long idCita);
    long count(); // Contar registros
    Venta generarVenta(Long idCita);
    void seleccionarComprador(Long idVenta, String cedulaComprador);
    void actualizarVentaPaso2Ganado(Long idVenta, Long precioTotal, int cantidad, String observaciones) throws IOException;
    void aceptarNegociacion(Long idVenta, String respuesta, String razonRechazo, int cantidad, Long precio);
    void actualizarVentaPaso3Ganado(
            Long idVenta,
            MultipartFile gsmi,
            MultipartFile certificadosSanitarios,
            MultipartFile facturaPropiedad,
            MultipartFile inventarioLote,
            MultipartFile certificadoSinigan,
            MultipartFile certificadoHierro,
            MultipartFile certificadoPesaje,
            String observacionesSanitarias
    ) throws IOException;
}
