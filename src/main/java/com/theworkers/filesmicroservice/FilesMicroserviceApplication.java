package com.theworkers.filesmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// 🎯 CRÍTICO: Agregar la exclusión de Actuator Security
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        // 💡 NUEVA EXCLUSIÓN: Para resolver el error de Actuator
        ManagementWebSecurityAutoConfiguration.class
})
public class FilesMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesMicroserviceApplication.class, args);
    }
}