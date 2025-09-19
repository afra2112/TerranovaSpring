package com.proyecto.terranova;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Ciudad;
import com.proyecto.terranova.entity.Rol;
import com.proyecto.terranova.entity.Usuario;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.repository.RolRepository;
import com.proyecto.terranova.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TerranovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerranovaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository, CiudadRepository ciudadRepository) {
        return args -> {

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

            if(usuarioRepository.findByEmail("andres@gmail.com") == null){
                Usuario usuario = new Usuario();
                usuario.setRoles(rolRepository.findAll());
                usuario.setNombres("Andres");
                usuario.setApellidos("Ramirez");
                usuario.setCedula("1234567890");
                usuario.setTelefono("3102162732");
                usuario.setEmail("andres@gmail.com");
                usuario.setContrasena(passwordEncoder.encode("andres1234"));
                usuario.setNacimiento(LocalDate.parse("2007-06-05"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            if(usuarioRepository.findByEmail("nicolas@gmail.com") == null){
                Usuario usuario = new Usuario();
                usuario.setRoles(List.of(rolRepository.findBynombreRol(RolEnum.COMPRADOR)));
                usuario.setNombres("Nicolas");
                usuario.setApellidos("Villalba");
                usuario.setCedula("9876543210");
                usuario.setTelefono("3103400745");
                usuario.setEmail("nicolas@gmail.com");
                usuario.setContrasena(passwordEncoder.encode("nicolas1234"));
                usuario.setNacimiento(LocalDate.parse("2000-06-05"));
                usuario.setFechaRegistro(LocalDate.now());
                usuarioRepository.save(usuario);
            }

            if (ciudadRepository.count() == 0) {
                List<String> ciudades = Arrays.asList(
                        "Bogota",
                        "Medellin",
                        "Cali",
                        "Barranquilla",
                        "Cartagena",
                        "Cucuta",
                        "Bucaramanga",
                        "Pereira",
                        "Santa Marta",
                        "Ibague",
                        "Manizales",
                        "Pasto",
                        "Monteria",
                        "Neiva",
                        "Villavicencio",
                        "Armenia",
                        "Sincelejo",
                        "Valledupar",
                        "Popayan",
                        "Riohacha",
                        "Tunja",
                        "Florencia",
                        "Quibdó",
                        "Mocoa",
                        "San Jose del Guaviare",
                        "Mitu",
                        "Puerto Carreño",
                        "Yopal",
                        "Inirida",
                        "Leticia"
                );

                ciudades.forEach(nombre -> {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombreCiudad(nombre);
                    ciudadRepository.save(ciudad);
                });
            }
        };
    }
}
