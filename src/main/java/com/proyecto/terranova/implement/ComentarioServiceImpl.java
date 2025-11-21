package com.proyecto.terranova.implement;


import com.proyecto.terranova.entity.Asistencia;
import com.proyecto.terranova.entity.Comentario;
import com.proyecto.terranova.entity.Producto;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.AsistenciaRepository;
import com.proyecto.terranova.repository.ComentarioRepository;
import com.proyecto.terranova.repository.ProductoRepository;
import com.proyecto.terranova.repository.UsuarioRepository;
import com.proyecto.terranova.service.ComentarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Override
    @Transactional
    public void agregarComentario(String cedulaComprador, Long idProducto, String contenido) {
        System.out.println("Intentando comentar producto ID: " + idProducto + " por usuario: " + cedulaComprador);

        // Buscar comprador
        Usuario comprador = usuarioRepository.findById(cedulaComprador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que tenga rol de comprador
        boolean esComprador = comprador.getRoles().stream()
                .anyMatch(rol -> rol.getNombreRol().name().equalsIgnoreCase("COMPRADOR"));

        if (!esComprador) {
            throw new RuntimeException("Solo los compradores pueden comentar");
        }

        // Buscar producto
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // ✅ Validar que el comprador haya asistido a una cita relacionada con este producto
        boolean asistioACita = asistenciaRepository.existsByUsuarioAndCita_ProductoAndAsistioTrue(comprador, producto);

        if (!asistioACita) {
            throw new RuntimeException("Solo puedes comentar si asististe a la cita del producto");
        }

        // Crear comentario
        Comentario comentario = new Comentario();
        comentario.setComprador(comprador);
        comentario.setProducto(producto);
        comentario.setContenido(contenido);
        comentario.setFechaComentario(LocalDateTime.now());

        comentarioRepository.save(comentario);

        System.out.println("✅ Comentario guardado correctamente por usuario: " + comprador.getNombres());
    }

    @Override
    public List<Comentario> obtenerComentariosPorProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return comentarioRepository.findByProductoOrderByFechaComentarioDesc(producto);
    }
}
