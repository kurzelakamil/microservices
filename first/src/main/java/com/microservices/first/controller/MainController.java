package com.microservices.first.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MainController {

	RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	public MainController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/")
	public String sendRequest() {
		String response = restTemplate.getForObject("http://second-service/", String.class);
		return response;
	}

}
