package com.microservices.first.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.second.model.Model;



@RestController
public class MainController {

	RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	public MainController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	ReplyingKafkaTemplate<String, Model, Model> kafkaTemplate;

	@GetMapping("/")
	public String sendRequest() {
		String response = restTemplate.getForObject("http://second-service/kafka", String.class);
		return response;
	}

	@PostMapping("/kafka"/*, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE*/)
	public String request() throws InterruptedException, ExecutionException {
		System.out.println("create producer record");
		Model request = new Model();
		request.setString("request");
		ProducerRecord<String, Model> record = new ProducerRecord<String, Model>("request", request);
		System.out.println("set reply topic in header");
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "requestReplyTopic".getBytes()));
		System.out.println("post in kafka topic");
		RequestReplyFuture<String, Model, Model> sendAndReceive = kafkaTemplate.sendAndReceive(record);

		System.out.println("confirm if producer produced successfully");
		SendResult<String, Model> sendResult = sendAndReceive.getSendFuture().get();
		
		 //print all headers
		sendResult.getProducerRecord().headers().forEach(header ->
		System.out.println(header.key() + ":" + header.value().toString()));

		// get consumer record
		ConsumerRecord<String, Model> consumerRecord = sendAndReceive.get();
		System.out.println("return consumer value");
		return consumerRecord.value().getString();
	}

}
