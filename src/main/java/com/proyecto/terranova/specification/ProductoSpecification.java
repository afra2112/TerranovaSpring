package com.proyecto.terranova.specification;

import jakarta.persistence.criteria.Predicate;
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

    public static Specification<Producto> filtrarPorRaza(String raza) {
        return (root, query, cb) -> cb.equal(root.get("razaGanado"), raza);
    }

    public static Specification<Producto> filtrarPorPeso(Integer min, Integer max) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (min != null) p = cb.and(p, cb.ge(root.get("pesoGanado"), min));
            if (max != null) p = cb.and(p, cb.le(root.get("pesoGanado"), max));
            return p;
        };
    }

    public static Specification<Producto> filtrarPorEdad(Integer min, Integer max) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (min != null) p = cb.and(p, cb.ge(root.get("edadGanado"), min));
            if (max != null) p = cb.and(p, cb.le(root.get("edadGanado"), max));
            return p;
        };
    }

    public static Specification<Producto> filtrarPorGenero(String genero) {
        return (root, query, cb) -> cb.equal(root.get("generoGanado"), genero);
    }
}
