package com.proyecto.terranova.config;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner commandLineRunner(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            RolRepository rolRepository,
            CiudadRepository ciudadRepository,
            TerrenoRepository terrenoRepository,
            FincaRepository fincaRepository,
            GanadoRepository ganadoRepository,
            ProductoRepository productoRepository) {

        return args -> {
            Random random = new Random();

            //ROLES
            if (!rolRepository.existsByNombreRol(RolEnum.COMPRADOR)) {
                Rol rol = new Rol();
                rol.setNombreRol(RolEnum.COMPRADOR);
                rolRepository.save(rol);
            }
            if (!rolRepository.existsByNombreRol(RolEnum.VENDEDOR)) {
                Rol rol = new Rol();
                rol.setNombreRol(RolEnum.VENDEDOR);
                rolRepository.save(rol);
            }

            //CIUDADES
            if (ciudadRepository.count() == 0) {
                List<String> ciudades = Arrays.asList(
                        "Bogota", "Medellin", "Cali", "Barranquilla", "Cartagena",
                        "Cucuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibague",
                        "Manizales", "Pasto", "Monteria", "Neiva", "Villavicencio",
                        "Armenia", "Sincelejo", "Valledupar", "Popayan", "Riohacha",
                        "Tunja", "Florencia", "Quibdo", "Mocoa", "San Jose del Guaviare",
                        "Mitu", "Puerto Carreno", "Yopal", "Inirida", "Leticia"
                );

                ciudades.forEach(nombre -> {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombreCiudad(nombre);
                    ciudadRepository.save(ciudad);
                });
            }

            //USUARIOS
            if (usuarioRepository.findByEmail("afra65069@gmail.com") == null) {
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(
                        rolRepository.findBynombreRol(RolEnum.COMPRADOR),
                        rolRepository.findBynombreRol(RolEnum.VENDEDOR)
                ));
                usuario.setNombres("Andres");
                usuario.setApellidos("Ramirez");
                usuario.setCedula("1234567890");
                usuario.setTelefono("3001234567");
                usuario.setEmail("afra65069@gmail.com");
                usuario.setContrasena(passwordEncoder.encode("andres1234"));
                usuario.setNacimiento(LocalDate.parse("1990-05-15"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }
            crearUsuarioSiNoExiste(usuarioRepository, passwordEncoder, rolRepository,
                    "maria@gmail.com", "Maria", "Rodriguez Lopez", "9876543210", "3109876543", "maria1234", true);
            crearUsuarioSiNoExiste(usuarioRepository, passwordEncoder, rolRepository,
                    "carlos@gmail.com", "Carlos", "Mendez Silva", "5551234567", "3201234567", "carlos1234", false);
            crearUsuarioSiNoExiste(usuarioRepository, passwordEncoder, rolRepository,
                    "laura@gmail.com", "Laura", "Torres Ramirez", "7778889990", "3157654321", "laura1234", false);

            //PRODUCTOS
            if (productoRepository.count() == 0) {

                List<Usuario> vendedores = usuarioRepository.findAll().stream()
                        .filter(u -> u.getRoles().stream().anyMatch(r -> r.getNombreRol() == RolEnum.VENDEDOR))
                        .toList();

                List<Ciudad> todasCiudades = ciudadRepository.findAll();
                String[] tiposTerreno = {"Residencial", "Agricola"};
                String[] tiposFinca = {"Ganadera", "Recreativa"};
                String[] razasGanado = {"Brahman", "Holstein"};
                String[] tiposGanado = {"Lechero", "De carne"};
                String[] estadosSanitarios = {"Excelente", "Vacunado y controlado"};

                for (Usuario vendedor : vendedores) {

                    //2 TERRENOS
                    for (String tipo : tiposTerreno) {
                        Terreno terreno = new Terreno();
                        terreno.setNombreProducto("Terreno " + tipo + " " + vendedor.getNombres());
                        terreno.setDescripcion("Terreno " + tipo.toLowerCase() + " con excelente ubicación, ideal para proyectos.");
                        terreno.setEstado("DISPONIBLE");
                        terreno.setFechaPublicacion(LocalDate.now().minusDays(random.nextInt(15)));
                        terreno.setLatitud(String.valueOf(4.0 + random.nextDouble() * 6.0));
                        terreno.setLongitud(String.valueOf(-77.0 + random.nextDouble() * 5.0));
                        terreno.setPrecioProducto(80_000_000L + random.nextInt(120_000_000));
                        terreno.setVendedor(vendedor);
                        terreno.setCiudad(todasCiudades.get(random.nextInt(todasCiudades.size())));
                        terreno.setServicios("Agua, Luz");
                        terreno.setTopografiaTerreno("Plano");
                        terreno.setAcceso("Pavimentado");
                        terreno.setTipoTerreno(tipo);
                        terreno.setUsoActual("Desarrollo");
                        terreno.setTamanoTerreno(5000);

                        terreno = terrenoRepository.save(terreno);
                        agregarImagenesProducto(terreno, "terreno", productoRepository);
                    }

                    //2 FINCAS
                    for (String tipo : tiposFinca) {
                        Finca finca = new Finca();
                        finca.setNombreProducto("Finca " + tipo + " " + vendedor.getNombres());
                        finca.setDescripcion("Finca " + tipo.toLowerCase() + " completamente equipada y lista para uso inmediato.");
                        finca.setEstado("DISPONIBLE");
                        finca.setFechaPublicacion(LocalDate.now().minusDays(random.nextInt(15)));
                        finca.setLatitud(String.valueOf(4.0 + random.nextDouble() * 6.0));
                        finca.setLongitud(String.valueOf(-77.0 + random.nextDouble() * 5.0));
                        finca.setPrecioProducto(150_000_000L + random.nextInt(250_000_000));
                        finca.setVendedor(vendedor);
                        finca.setCiudad(todasCiudades.get(random.nextInt(todasCiudades.size())));
                        finca.setEspacioConstruido(String.valueOf(200 + random.nextInt(300)));
                        finca.setEspacioTotal(String.valueOf(6000 + random.nextInt(20000)));
                        finca.setEstratoFinca(random.nextInt(3) + 3);
                        finca.setNumeroHabitaciones(random.nextInt(3) + 2);
                        finca.setNumeroBanos(random.nextInt(2) + 1);

                        finca = fincaRepository.save(finca);
                        agregarImagenesProducto(finca, "finca", productoRepository);
                    }

                    //2 GANADOS
                    for (int i = 0; i < 2; i++) {
                        String raza = razasGanado[random.nextInt(razasGanado.length)];
                        Ganado ganado = new Ganado();
                        ganado.setNombreProducto("Ganado " + raza + " " + vendedor.getNombres() + " #" + (i + 1));
                        ganado.setDescripcion("Ejemplar de raza " + raza + " en excelente estado sanitario.");
                        ganado.setEstado("DISPONIBLE");
                        ganado.setFechaPublicacion(LocalDate.now().minusDays(random.nextInt(15)));
                        ganado.setLatitud(String.valueOf(4.0 + random.nextDouble() * 6.0));
                        ganado.setLongitud(String.valueOf(-77.0 + random.nextDouble() * 5.0));
                        ganado.setPrecioProducto(6_000_000L + random.nextInt(10_000_000));
                        ganado.setVendedor(vendedor);
                        ganado.setCiudad(todasCiudades.get(random.nextInt(todasCiudades.size())));

                        ganado.setCantidad(1 + random.nextInt(5));
                        ganado.setEdadGanado(12 + random.nextInt(24));
                        ganado.setEstadoSanitario(estadosSanitarios[random.nextInt(estadosSanitarios.length)]);
                        ganado.setGeneroGanado(random.nextBoolean() ? "Macho" : "Hembra");
                        ganado.setPesoGanado(300 + random.nextInt(200));
                        ganado.setRazaGanado(raza);
                        ganado.setTipoGanado(tiposGanado[random.nextInt(tiposGanado.length)]);

                        ganado = ganadoRepository.save(ganado);
                        agregarImagenesProducto(ganado, "ganado", productoRepository);
                    }
                }

                System.out.println("Base de datos inicializada correctamente (6 productos por vendedor).");
            }
        };
    }

    private void crearUsuarioSiNoExiste(UsuarioRepository usuarioRepository,
                                        PasswordEncoder passwordEncoder,
                                        RolRepository rolRepository,
                                        String email, String nombres, String apellidos,
                                        String cedula, String telefono, String contrasena, boolean vendedor) {
        Random random = new Random();

        if (usuarioRepository.findByEmail(email) == null) {
            Usuario usuario = new Usuario();
            if (vendedor) {
                usuario.setRoles(List.of(
                        rolRepository.findBynombreRol(RolEnum.COMPRADOR),
                        rolRepository.findBynombreRol(RolEnum.VENDEDOR)
                ));
            } else {
                usuario.setRoles(List.of(rolRepository.findBynombreRol(RolEnum.COMPRADOR)));
            }
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCedula(cedula);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            usuario.setContrasena(passwordEncoder.encode(contrasena));
            usuario.setNacimiento(LocalDate.now().minusYears(25 + random.nextInt(15)));
            usuario.setFechaRegistro(LocalDate.now());
            usuarioRepository.save(usuario);
        }
    }

    private void agregarImagenesProducto(Producto producto, String tipo, ProductoRepository productoRepository) {
        Random random = new Random();
        int cantidadImagenes = 2 + random.nextInt(2);
        List<Imagen> imagenes = new ArrayList<>();

        Long idProducto = producto.getIdProducto();
        String nombreVendedor = producto.getVendedor().getNombres().replaceAll("\\s+", "").toLowerCase();

        for (int i = 1; i <= cantidadImagenes; i++) {
            Imagen imagen = new Imagen();
            imagen.setProducto(producto);
            imagen.setNombreArchivo(
                    "/imagenes/"+"imagen" + i + "_" + tipo + "_id" + idProducto + "_" + nombreVendedor + ".jpg"
            );
            imagenes.add(imagen);
        }

        producto.setImagenes(imagenes);
        productoRepository.save(producto);
    }
}
