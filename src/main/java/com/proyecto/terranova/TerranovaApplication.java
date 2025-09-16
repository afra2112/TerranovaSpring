package com.proyecto.terranova;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Ciudad;
import com.proyecto.terranova.entity.Rol;
import com.proyecto.terranova.repository.CiudadRepository;
import com.proyecto.terranova.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TerranovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerranovaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RolRepository rolRepository, CiudadRepository ciudadRepository) {
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
