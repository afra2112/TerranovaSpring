package com.proyecto.terranova.service;

import com.proyecto.terranova.entity.Favorito;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoritoService {
    boolean agregarFavorito(Usuario usuario, Producto producto);
    boolean eliminarFavorito(Usuario usuario, Producto producto);
    List<Favorito> obtenerFavoritos(Usuario usuario);
    List<Long> obtenerIdsFavoritosPorUsuario(Usuario usuario);
}
