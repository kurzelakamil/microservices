package com.microservices.second.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.microservices.second.controller.MainController;

import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
@ComponentScan(basePackageClasses = MainController.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
