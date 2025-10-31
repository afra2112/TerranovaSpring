package com.proyecto.terranova.config;

import com.proyecto.terranova.entity.*;
import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
            DisponibilidadRepository disponibilidadRepository,
            ProductoRepository productoRepository) {

        return args -> {
            Random random = new Random();

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

            if (ciudadRepository.count() == 0) {
                List<String> ciudades = Arrays.asList(
                        "Bogota", "Medellin", "Cali", "Barranquilla", "Cartagena",
                        "Cucuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibague",
                        "Manizales", "Pasto", "Monteria", "Neiva", "Villavicencio",
                        "Armenia", "Sincelejo", "Valledupar", "Popayan", "Riohacha",
                        "Tunja", "Florencia", "Quibdó", "Mocoa", "San Jose del Guaviare",
                        "Mitu", "Puerto Carreño", "Yopal", "Inirida", "Leticia"
                );

                ciudades.forEach(nombre -> {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombreCiudad(nombre);
                    ciudadRepository.save(ciudad);
                });
            }

            if (usuarioRepository.findByEmail("afra65069@gmail.com") == null) {
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(
                        rolRepository.findBynombreRol(RolEnum.COMPRADOR),
                        rolRepository.findBynombreRol(RolEnum.VENDEDOR)
                ));
                usuario.setNombres("Alejandro");
                usuario.setApellidos("García Martínez");
                usuario.setCedula("1234567890");
                usuario.setTelefono("3001234567");
                usuario.setEmail("afra65069@gmail.com");
                usuario.setContrasena(passwordEncoder.encode("password123"));
                usuario.setNacimiento(LocalDate.parse("1990-05-15"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            String emailUsuario2 = "maria.rodriguez" + random.nextInt(1000) + "@example.com";
            if (usuarioRepository.findByEmail(emailUsuario2) == null) {
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(
                        rolRepository.findBynombreRol(RolEnum.COMPRADOR),
                        rolRepository.findBynombreRol(RolEnum.VENDEDOR)
                ));
                usuario.setNombres("María");
                usuario.setApellidos("Rodríguez López");
                usuario.setCedula("9876543210");
                usuario.setTelefono("3109876543");
                usuario.setEmail(emailUsuario2);
                usuario.setContrasena(passwordEncoder.encode("password456"));
                usuario.setNacimiento(LocalDate.parse("1988-08-22"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            String emailUsuario3 = "carlos.mendez" + random.nextInt(1000) + "@example.com";
            if (usuarioRepository.findByEmail(emailUsuario3) == null) {
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(rolRepository.findBynombreRol(RolEnum.COMPRADOR)));
                usuario.setNombres("Carlos");
                usuario.setApellidos("Méndez Silva");
                usuario.setCedula("5551234567");
                usuario.setTelefono("3201234567");
                usuario.setEmail(emailUsuario3);
                usuario.setContrasena(passwordEncoder.encode("password789"));
                usuario.setNacimiento(LocalDate.parse("1995-03-10"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            String emailUsuario4 = "laura.torres" + random.nextInt(1000) + "@example.com";
            if (usuarioRepository.findByEmail(emailUsuario4) == null) {
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(rolRepository.findBynombreRol(RolEnum.COMPRADOR)));
                usuario.setNombres("Laura");
                usuario.setApellidos("Torres Ramírez");
                usuario.setCedula("7778889990");
                usuario.setTelefono("3157654321");
                usuario.setEmail(emailUsuario4);
                usuario.setContrasena(passwordEncoder.encode("password321"));
                usuario.setNacimiento(LocalDate.parse("1992-11-28"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            if (terrenoRepository.count() == 0 && fincaRepository.count() == 0) {
                // Obtener vendedores (usuarios con rol VENDEDOR)
                List<Usuario> vendedores = usuarioRepository.findAll().stream()
                        .filter(u -> u.getRoles().stream()
                                .anyMatch(r -> r.getNombreRol() == RolEnum.VENDEDOR))
                        .toList();

                List<Ciudad> todasCiudades = ciudadRepository.findAll();
                String[] tiposTerreno = {"Residencial", "Comercial", "Industrial", "Agrícola"};
                String[] tiposFinca = {"Ganadera", "Agrícola", "Recreativa", "Mixta"};

                for (Usuario vendedor : vendedores) {
                    // Crear 2 terrenos por cada tipo (8 terrenos por vendedor)
                    for (String tipo : tiposTerreno) {
                        for (int i = 1; i <= 2; i++) {
                            Terreno terreno = new Terreno();
                            terreno.setNombreProducto("Terreno " + tipo + " " + vendedor.getNombres() + " #" + i);
                            terreno.setDescripcion("Terreno " + tipo + " #" + i + " en excelente ubicación con todos los servicios");
                            terreno.setEstado("DISPONIBLE");
                            terreno.setFechaPublicacion(LocalDate.now().minusDays(random.nextInt(30)));
                            terreno.setLatitud(String.valueOf(4.0 + random.nextDouble() * 6.0));
                            terreno.setLongitud(String.valueOf(-77.0 + random.nextDouble() * 5.0));
                            terreno.setPrecioProducto(new BigDecimal(50000000 + random.nextInt(200000000)).toBigInteger().longValue());
                            terreno.setVendedor(vendedor);
                            terreno.setCiudad(todasCiudades.get(random.nextInt(todasCiudades.size())));
                            terreno.setServicios("Agua, Gas");
                            terreno.setTopografiaTerreno("Plano");

                            // Campos específicos de terreno
                            terreno.setAcceso("Pavimentado");
                            terreno.setNombreProducto("Terreno " + tipo + " Premium");
                            terreno.setTipoTerreno(tipo);
                            terreno.setUsoActual("Disponible para desarrollo");

                            terreno = terrenoRepository.save(terreno);

                            crearDisponibilidades(terreno.getIdProducto(), disponibilidadRepository, productoRepository);
                        }
                    }

                    // Crear 2 fincas por cada tipo (8 fincas por vendedor)
                    for (String tipo : tiposFinca) {
                        for (int i = 1; i <= 2; i++) {
                            Finca finca = new Finca();
                            finca.setNombreProducto("Finca " + tipo + " " + vendedor.getNombres() + " #" + i);
                            finca.setDescripcion("Finca " + tipo + " #" + i + " con todas las comodidades y servicios");
                            finca.setEstado("DISPONIBLE");
                            finca.setFechaPublicacion(LocalDate.now().minusDays(random.nextInt(30)));
                            finca.setLatitud(String.valueOf(4.0 + random.nextDouble() * 6.0));
                            finca.setLongitud(String.valueOf(-77.0 + random.nextDouble() * 5.0));
                            finca.setPrecioProducto(new BigDecimal(100000000 + random.nextInt(400000000)).toBigInteger().longValue());
                            finca.setVendedor(vendedor);
                            finca.setCiudad(todasCiudades.get(random.nextInt(todasCiudades.size())));

                            // Campos específicos de finca
                            finca.setEspacioConstruido(new BigDecimal(150 + random.nextInt(350)).toString());
                            finca.setEspacioTotal(new BigDecimal(5000 + random.nextInt(45000)).toString());
                            finca.setEstratoFinca(random.nextInt(3) + 3);
                            finca.setNumeroHabitaciones(random.nextInt(4) + 2);
                            finca.setNumeroBanos(random.nextInt(3) + 1);

                            finca = fincaRepository.save(finca);

                            crearDisponibilidades(finca.getIdProducto(), disponibilidadRepository, productoRepository);
                        }
                    }
                }

                System.out.println("✓ Database seeded successfully!");
                System.out.println("  - 30 ciudades");
                System.out.println("  - 2 roles");
                System.out.println("  - 4 usuarios (2 vendedores, 4 compradores)");
                System.out.println("  - " + (vendedores.size() * 16) + " productos (terrenos y fincas)");
                System.out.println("  - " + (vendedores.size() * 16 * 7) + " disponibilidades");
            }
        };
    }

    private void crearDisponibilidades(Long idProducto, DisponibilidadRepository disponibilidadRepository, ProductoRepository productoRepository) {
        LocalDate hoy = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setProducto(productoRepository.findById(idProducto).orElseThrow());
            disponibilidad.setFecha(hoy.plusDays(i));
            disponibilidad.setHora(LocalTime.of(9, 0));
            disponibilidad.setDisponible(true);
            disponibilidad.setDescripcion("Disponible para visita");

            disponibilidadRepository.save(disponibilidad);
        }
    }
}
