package com.inventra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.inventra.repository")
public class InventraBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventraBackendApplication.class, args);
    }
}
