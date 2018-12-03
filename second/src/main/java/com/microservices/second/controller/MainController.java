package com.microservices.second.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.second.model.Model;

@RestController
public class MainController {

	@Value("${sqlStatement}")
	private String sqlStatement;

	@Value("${sqlUrl}")
	private String sqlUrl;

	@Value("${sqlUser}")
	private String sqlUser;

	@Value("${sqlPassword}")
	private String sqlPassword;

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/")
	public String sendResponse() {
		return getDataFromDb();
	}

	@KafkaListener(topics = "request", groupId = "group-id")
	@SendTo
	public Model listen(Model request) throws InterruptedException {
		request.setString(getDataFromDb());
		return request;
	}

	private String getDataFromDb() {
		String sql = "SELECT * FROM message";
		String response = "";
		try {
			Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
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
