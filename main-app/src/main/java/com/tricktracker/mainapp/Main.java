package com.tricktracker.mainapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan; // для Spring Boot 4
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.tricktracker.mainapp",
        "com.tricktracker.authservice",
        "com.tricktracker.userservice" // <-- ДОБАВЛЕНО
})
@EnableJpaRepositories(basePackages = {
        "com.tricktracker.authservice.repository",
        "com.tricktracker.userservice.repository" // <-- ДОБАВЛЕНО
})
@EntityScan(basePackages = {
        "com.tricktracker.authservice.entity",
        "com.tricktracker.userservice.entity" // <-- ДОБАВЛЕНО
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}