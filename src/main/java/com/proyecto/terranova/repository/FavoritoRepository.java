package com.proyecto.terranova.repository;

import com.proyecto.terranova.entity.Favorito;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    List<Favorito> findByUsuario(Usuario usuario);
}
