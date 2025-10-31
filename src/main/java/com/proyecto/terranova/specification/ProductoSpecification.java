package com.proyecto.terranova.specification;

import org.springframework.data.jpa.domain.Specification;
import com.proyecto.terranova.entity.Producto;

public class ProductoSpecification {

    public static Specification<Producto> buscarPorTexto(String texto) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("nombreProducto")), "%" + texto.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("descripcion")), "%" + texto.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("ciudad").get("nombreCiudad")), "%" + texto.toLowerCase() + "%")
        );
    }

    public static Specification<Producto> filtrarPorTipo(String tipo) {
        return (root, query, cb) -> {
            switch (tipo.toLowerCase()) {
                case "ganado":
                    return cb.equal(root.type(), com.proyecto.terranova.entity.Ganado.class);
                case "terreno":
                    return cb.equal(root.type(), com.proyecto.terranova.entity.Terreno.class);
                case "finca":
                    return cb.equal(root.type(), com.proyecto.terranova.entity.Finca.class);
                default:
                    return null;
            }
        };
    }
}
