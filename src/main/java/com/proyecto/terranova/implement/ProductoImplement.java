package com.proyecto.terranova.implement;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.repository.*;
import com.proyecto.terranova.service.UsuarioService;
import com.proyecto.terranova.specification.ProductoSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.proyecto.terranova.service.ProductoService;
import com.proyecto.terranova.dto.ProductoDTO;

@Service
public class ProductoImplement implements ProductoService {

    @Autowired
    private ProductoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private TerrenoRepository terrenoRepository;
    @Autowired
    private GanadoRepository ganadoRepository;
    @Autowired
    private FincaRepository fincaRepository;

    @Override
    public Producto crearProductoBase(Map<String, String> datosForm, String correo , Long idciudad) {
        Producto producto;
        String tipo = datosForm.get("tipoProducto");
        System.out.println("Datos recibidos: " + datosForm);
        if (tipo == null) throw new IllegalArgumentException("Tipo de producto No espesificado");

        switch (tipo.toLowerCase()){
            case "terreno":
                producto = modelMapper.map(datosForm, Terreno.class);
                break;
            case "finca":
                producto = modelMapper.map(datosForm, Finca.class);
                break;
            case "ganado" :
                producto = modelMapper.map(datosForm, Ganado.class);
                break;
            default:
                throw new IllegalArgumentException("Tipo de Producto No valido");
        }

        Ciudad ciudad = ciudadRepository.findById(idciudad).orElseThrow();
        producto.setCiudad(ciudad);
        producto.setFechaPublicacion(LocalDate.now());
        producto.setEstado("Disponible");


        producto.setLatitud(datosForm.get("latitud"));
        producto.setLongitud(datosForm.get("longitud"));

        Usuario vendedor = usuarioRepository.findByEmail(correo);
        if (vendedor == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        System.out.println("Cédula del vendedor: " + vendedor.getCedula());
        producto.setVendedor(vendedor);

        System.out.println("Producto mapeado: " + producto);

        return productoRepository.save(producto);
    }

    @Override
    public Producto findById(Long id) {
        return  repository.findById(id).orElseThrow();
    }

    @Override
    public List<Producto> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Producto> obtenerTodosPorVendedor(Usuario vendedor) {
        return repository.findByVendedor(vendedor);
    }

    @Override
    public boolean delete(Long id) {
        if(!repository.existsById(id)){
               return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void actualizarProducto(Producto prodForm) {
        Producto original = productoRepository.findById(prodForm.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        original.setNombreProducto(prodForm.getNombreProducto());
        original.setPrecioProducto(prodForm.getPrecioProducto());
        original.setEstado(prodForm.getEstado());
        original.setDescripcion(prodForm.getDescripcion());


        if (prodForm.getCiudad() != null && prodForm.getCiudad().getIdCiudad() != null) {
            Ciudad ciudad = ciudadRepository.findById(prodForm.getCiudad().getIdCiudad())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            original.setCiudad(ciudad);
        }


        if (original instanceof Terreno terrenoOriginal && prodForm instanceof Terreno terrenoNuevo) {
            terrenoOriginal.setTamanoTerreno(terrenoNuevo.getTamanoTerreno());
            terrenoOriginal.setUsoActual(terrenoNuevo.getUsoActual());
            terrenoOriginal.setTopografiaTerreno(terrenoNuevo.getTopografiaTerreno());
            terrenoOriginal.setServicios(terrenoNuevo.getServicios());
            terrenoOriginal.setTipoTerreno(terrenoNuevo.getTipoTerreno());
            terrenoOriginal.setAcceso(terrenoNuevo.getAcceso());

        } else if (original instanceof Finca fincaOriginal && prodForm instanceof Finca fincaNueva) {
            fincaOriginal.setEspacioTotal(fincaNueva.getEspacioTotal());
            fincaOriginal.setEspacioConstruido(fincaNueva.getEspacioConstruido());
            fincaOriginal.setEstratoFinca(fincaNueva.getEstratoFinca());
            fincaOriginal.setNumeroHabitaciones(fincaNueva.getNumeroHabitaciones());
            fincaOriginal.setNumeroBanos(fincaNueva.getNumeroBanos());

        } else if (original instanceof Ganado ganadoOriginal && prodForm instanceof Ganado ganadoNuevo) {
            ganadoOriginal.setRazaGanado(ganadoNuevo.getRazaGanado());
            ganadoOriginal.setPesoGanado(ganadoNuevo.getPesoGanado());
            ganadoOriginal.setEdadGanado(ganadoNuevo.getEdadGanado());
            ganadoOriginal.setGeneroGanado(ganadoNuevo.getGeneroGanado());
            ganadoOriginal.setTipoGanado(ganadoNuevo.getTipoGanado());
            ganadoOriginal.setEstadoSanitario(ganadoNuevo.getEstadoSanitario());
            ganadoOriginal.setCantidad(ganadoNuevo.getCantidad());
        }
        productoRepository.save(original);
    }
    @Override
    public List<Producto> obtenerTodasMenosVendedor(Usuario vendedor) {
        return repository.findByVendedorNot(vendedor);
    }

    @Override
    public List<Producto> filtrarConSpecification(String texto, String tipo, String orden) {
        Specification<Producto> spec = (root, query, cb) -> cb.conjunction();

        if (texto != null && !texto.isEmpty()) {
            spec = spec.and(ProductoSpecification.buscarPorTexto(texto));
        }
        if (tipo != null && !tipo.isEmpty()) {
            spec = spec.and(ProductoSpecification.filtrarPorTipo(tipo));
        }

        if (orden == null) {
            orden = "recientes";
        }

        Sort sort = switch (orden) {
            case "precio_asc" -> Sort.by("precioProducto").ascending();
            case "precio_desc" -> Sort.by("precioProducto").descending();
            case "recientes" -> Sort.by("fechaPublicacion").descending();
            case "antiguos" -> Sort.by("fechaPublicacion").ascending();
            default -> Sort.by("idProducto").descending();
        };

        return productoRepository.findAll(spec, sort);
    }

    @Override
    public void eliminarProducto(Long idProducto, String correo) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        Usuario vendedor = usuarioRepository.findByEmail(correo);
        if (vendedor == null || !producto.getVendedor().getCedula().equals(vendedor.getCedula())) {
            throw new IllegalArgumentException("No tienes permiso para eliminar este producto.");
        }

        // Eliminar imágenes asociadas
        List<Imagen> imagenes = imagenRepository.findByProducto(producto);
        imagenRepository.deleteAll(imagenes);

        // Eliminar entidad específica si aplica
        if (producto instanceof Terreno) {
            terrenoRepository.delete((Terreno) producto);
        } else if (producto instanceof Ganado) {
            ganadoRepository.delete((Ganado) producto);
        } else if (producto instanceof Finca) {
            fincaRepository.delete((Finca) producto);
        } else {
            productoRepository.delete(producto); // fallback
        }
    }

}
