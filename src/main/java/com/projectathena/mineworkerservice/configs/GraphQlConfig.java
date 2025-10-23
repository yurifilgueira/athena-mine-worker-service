package com.projectathena.mineworkerservice.configs;

import com.projectathena.mineworkerservice.configs.scalar.LocalDateTimeScalar;
import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(LocalDateTimeScalar.createLocalDateTimeScalar());
    }

}
