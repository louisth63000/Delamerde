package com.example.restservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class YamlConfig {

    // JSON ObjectMapper and MessageConverter
    @Bean
    public HttpMessageConverter<Object> jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    // YAML ObjectMapper and MessageConverter
    @Bean
    public HttpMessageConverter<Object> yamlMessageConverter() {
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        yamlObjectMapper.registerModule(new JavaTimeModule());
        yamlObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // MediaType for YAML
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(yamlObjectMapper);
        converter.setSupportedMediaTypes(List.of(MediaType.valueOf("application/x-yaml")));
        return converter;
    }
}
