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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class TerranovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerranovaApplication.class, args);
    }

}
