package com.theworkers.templatemicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FilesMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesMicroServiceApplication.class, args);
	}

}
