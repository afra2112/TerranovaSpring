package com.proyecto.terranova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class    TerranovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerranovaApplication.class, args);
    }

}
