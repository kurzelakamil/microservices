package com.microservices.second.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
