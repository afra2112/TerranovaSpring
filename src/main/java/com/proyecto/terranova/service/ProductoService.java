package com.proyecto.terranova.service;

import java.util.List;
import java.util.Map;

import com.proyecto.terranova.config.enums.EstadoCitaEnum;
import com.proyecto.terranova.config.enums.EstadoProductoEnum;
import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;

public interface ProductoService {
    Producto crearProductoBase(Map<String,String> datosForm , String cedula,Long idCiudad);
    Producto findById(Long id);
    List<Producto> findAll();
    List<Producto> obtenerTodosPorVendedor(Usuario vendedor);
    List<Producto> obtenerTodosPorVendedorYEstado(Usuario vendedor, EstadoProductoEnum estadoProductoEnum);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count();
    void actualizarProducto(Producto prodForm);
    void eliminarProducto(Long idProducto, String correo);
    List<Producto> obtenerTodasMenosVendedor(Usuario vendedor);
    List<Producto> filtrarConSpecification(String texto, String tipo, String orden);
}
