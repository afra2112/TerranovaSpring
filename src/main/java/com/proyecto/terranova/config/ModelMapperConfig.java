package com.proyecto.terranova.config;

import com.proyecto.terranova.dto.VentaDTO;
import com.proyecto.terranova.entity.Venta;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.typeMap(Venta.class, VentaDTO.class)
                .addMappings(mapper -> mapper.skip(VentaDTO::setListaComprobantes));
        return modelMapper;
    }
}