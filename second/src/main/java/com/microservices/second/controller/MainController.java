package com.microservices.second.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.second.model.Model;


@RestController
public class MainController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/")
	public String sendResponse() {
		return getDataFromDb();
	}
	
//	@GetMapping("/kafka")
//	public void kafka() {
//		kafkaTemplate.send("messenger", "hello");
//	}
	
//	@RequestMapping("/service-instances/{applicationName}")
//	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
//		return this.discoveryClient.getInstances(applicationName);
//	}
	
//	@KafkaListener(topics = "request", groupId = "group-id")
//	@SendTo
//	public String listen(String request) throws InterruptedException {
//		System.out.println(request);
//		request = getDataFromDb();
//		return request;
//	}

	@KafkaListener(topics = "request", groupId = "group-id")
	@SendTo
	public Model listen(Model request) throws InterruptedException {
		System.out.println(request);
		request.setString(getDataFromDb());
		return request;
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
