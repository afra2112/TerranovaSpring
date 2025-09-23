package com.proyecto.terranova.service;

import java.util.List;
import java.util.Map;

import com.proyecto.terranova.dto.ProductoDTO;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;

public interface ProductoService {
    Producto crearProductoBase(Map<String,String> datosForm , String cedula,Long idCiudad);
    Producto findById(Long id);
    List<Producto> findAll();
    List<Producto> obtenerTodosPorVendedor(Usuario vendedor);
    boolean delete(Long id);
    boolean existsById(Long id); // ValidaciÃ³n
    long count();// Contar registros
    void actualizarProducto(Producto prodForm);
    List<Producto> obtenerTodasMenosVendedor(Usuario vendedor);
}
