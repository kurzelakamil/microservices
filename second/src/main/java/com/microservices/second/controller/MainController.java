package com.microservices.second.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/")
	public String sendResponse() {
		return getDataFromDb();
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	public String getDataFromDb() {
		String sql = "SELECT * FROM message";
		String response = "";
		try {
			Connection conn = DriverManager.getConnection("jdbc:h2:mem://testdb", "root", "password");
			PreparedStatement preStm = conn.prepareStatement(sql);
			ResultSet rs = preStm.executeQuery();
			if (rs.next()) {
				response = rs.getString("response");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}
}
