package com.example.comparador_service;

import com.example.comparador_service.client.AutoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = AutoClient.class)
public class ComparadorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComparadorServiceApplication.class, args);
	}

}
