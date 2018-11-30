package com.microservices.second.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/")
	public String sendResponse() {
		return "hello World";
	}
}
