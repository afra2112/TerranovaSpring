package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Favorito;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.FavoritoRepository;
import com.proyecto.terranova.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritoImplement implements FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Override
    public boolean agregarFavorito(Usuario usuario, Producto producto) {
        if (!favoritoRepository.existsByUsuarioAndProducto(usuario, producto)) {
            Favorito favorito = new Favorito();
            favorito.setUsuario(usuario);
            favorito.setProducto(producto);
            favorito.setFechaFavorito(LocalDateTime.now());
            favoritoRepository.save(favorito);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarFavorito(Usuario usuario, Producto producto) {
        Optional <Favorito> favorito = favoritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (favorito.isPresent()) {
            favoritoRepository.delete(favorito.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Favorito> obtenerFavoritos(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Long> obtenerIdsFavoritosPorUsuario(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario).stream()
                .map(Favorito::getProducto)
                .map(Producto::getIdProducto)
                .toList();
    }
}
