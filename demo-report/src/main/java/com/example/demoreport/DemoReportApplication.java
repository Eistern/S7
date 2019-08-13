package com.example.demoreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class DemoReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReportApplication.class, args);
	}

	@KafkaListener(topics = "phone-update, person-update")
	public void f() {

	}
}
