package com.proyecto.terranova;

import com.proyecto.terranova.config.enums.RolEnum;
import com.proyecto.terranova.entity.Rol;
import com.proyecto.terranova.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TerranovaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerranovaApplication.class, args);
	}

    @Bean
    CommandLineRunner commandLineRunner(RolRepository rolRepository){
        return args -> {
            if(!rolRepository.existsByNombreRol(RolEnum.COMPRADOR)){
                Rol rol = new Rol();
                rol.setNombreRol(RolEnum.COMPRADOR);
                rolRepository.save(rol);
            }
            if(!rolRepository.existsByNombreRol(RolEnum.VENDEDOR)){
                Rol rol = new Rol();
                rol.setNombreRol(RolEnum.VENDEDOR);
                rolRepository.save(rol);
            }
        };
    }
}
