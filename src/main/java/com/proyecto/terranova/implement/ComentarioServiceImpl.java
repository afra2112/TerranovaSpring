package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.Comentario;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.ComentarioRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void agregarComentario(String cedulaComprador, Long idProducto, String contenido) {
        System.out.println("Cedula: " + cedulaComprador);
        Usuario comprador = usuarioRepository.findById(cedulaComprador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean esComprador = comprador.getRoles().stream()
                .anyMatch(rol -> rol.getNombreRol().name().equalsIgnoreCase("COMPRADOR"));

        if (!esComprador) {
            throw new RuntimeException("Solo los compradores pueden comentar");
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setComprador(comprador);
        comentario.setProducto(producto);
        comentario.setContenido(contenido);

        comentarioRepository.save(comentario);
    }

    @Override
    public List<Comentario> obtenerComentariosPorProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return comentarioRepository.findByProductoOrderByFechaComentarioDesc(producto);
    }
}
