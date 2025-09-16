package com.projectathena.mineworkerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AthenaMineWorkerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AthenaMineWorkerServiceApplication.class, args);
    }

}
