package com.projectathena.mineworkerservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeneralConfigs {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
