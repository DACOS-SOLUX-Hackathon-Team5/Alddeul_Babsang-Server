package com.example.alddeul_babsang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class VWorldConfig {

    @Value("${vworld.api.url}")
    private String apiUrl;

    @Value("${vworld.api.parameters.key}")
    private String apiKey;

    @Value("${vworld.api.parameters.service}")
    private String service;

    @Value("${vworld.api.parameters.request}")
    private String request;

    @Value("${vworld.api.parameters.refine}")
    private String refine;

    @Value("${vworld.api.parameters.simple}")
    private String simple;

    @Value("${vworld.api.parameters.type}")
    private String type;

    public String getApiUrl() {
        return apiUrl;
    }

    @Bean
    public Map<String, String> defaultParams() {
        return Map.of(
                "service", service,
                "request", request,
                "refine", refine,
                "simple", simple,
                "type", type,
                "key", apiKey
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

