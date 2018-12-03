package com.microservices.first.controller;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.second.model.Model;

@RestController
public class MainController {

	@Value("${kafka.topic.requestReplyTopic")
	private String requestReplyTopic;

	RestTemplate restTemplate;
	Model model = new Model();
	ReplyingKafkaTemplate<String, Model, Model> kafkaTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	public MainController(RestTemplate restTemplate, Model model,
			ReplyingKafkaTemplate<String, Model, Model> kafkaTemplate) {
		this.restTemplate = restTemplate;
		this.model = model;
		this.kafkaTemplate = kafkaTemplate;
	}

	@GetMapping("/")
	public String sendRequestByRestTemplate() {
		return restTemplate.getForObject("http://second-service/kafka", String.class);
	}

	@GetMapping("/kafka")
	public String sendRequestByKafka() throws InterruptedException, ExecutionException {
		model.setString("request");
		ProducerRecord<String, Model> record = new ProducerRecord<String, Model>("request", model);
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));
		RequestReplyFuture<String, Model, Model> sendAndReceive = kafkaTemplate.sendAndReceive(record);
		SendResult<String, Model> sendResult = sendAndReceive.getSendFuture().get();
		sendResult.getProducerRecord().headers()
				.forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));
		ConsumerRecord<String, Model> consumerRecord = sendAndReceive.get();
		return consumerRecord.value().getString();
	}

}
