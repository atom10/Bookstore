package com.capybarainc.BookStore.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOConversion {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
