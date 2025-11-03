package com.proyecto.terranova.service;

import java.io.IOException;
import java.util.List;
import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;
import org.springframework.web.multipart.MultipartFile;

public interface VentaService {
    Venta actualizarDatosVenta(Venta venta, List<Long> idsComprobantesEliminados, List<Long> idsGastosEliminados, List<MultipartFile> comprobantes) throws IOException;
    Venta actualizarEstado(Venta venta, String estado);
    Venta save(Venta venta);
    VentaDTO update(Long id, VentaDTO dto); // Actualizar
    Venta findById(Long id);
    List<VentaDTO> findAll();
    List<Venta> encontrarPorVendedor(Usuario vendedor);
    List<Venta> encontrarPorComprador(Usuario comprador);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
    Venta generarVenta(Long idProducto, Usuario comprador);
}
