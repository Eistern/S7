package com.example.demoreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.contacts.demo.kafka")
public class DemoReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReportApplication.class, args);
	}
}
