package com.projectathena.mineworkerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableWebFlux
public class AthenaMineWorkerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AthenaMineWorkerServiceApplication.class, args);
    }

}
