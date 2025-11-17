package com.proyecto.terranova.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${imagenes.directorio}")
    private String directorioImagenes;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:" + directorioImagenes)
                .addResourceLocations(
                        "file:" + directorioImagenes + "/",
                        "classpath:/static/imagenes/"
                );
    }
}
