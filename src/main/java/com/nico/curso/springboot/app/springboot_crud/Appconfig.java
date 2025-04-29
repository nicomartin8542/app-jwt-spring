package com.nico.curso.springboot.app.springboot_crud;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messages.properties")
public class Appconfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
