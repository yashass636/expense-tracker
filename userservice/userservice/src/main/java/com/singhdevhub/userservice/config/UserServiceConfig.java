package com.singhdevhub.userService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig
{

    @Bean
    public ObjectMapper objectMapperInit(){
        return new ObjectMapper();
    }

}