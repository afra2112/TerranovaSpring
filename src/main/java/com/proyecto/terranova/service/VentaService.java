package com.proyecto.terranova.service;

import java.util.List;
import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.entity.Venta;

public interface VentaService {
    Venta save(Venta venta);
    VentaDTO update(Long id, VentaDTO dto); // Actualizar
    Venta findById(Long id);
    List<VentaDTO> findAll();
    List<Venta> encontrarPorVendedor(Usuario vendedor);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count(); // Contar registros
}
