package com.contacts.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

import java.io.IOException;
import java.util.logging.LogManager;

//@SpringBootApplication
@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class DemoApplication {

	public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(DemoApplication.class.getResourceAsStream("/log.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logging properties");
            e.printStackTrace();
        }
        SpringApplication.run(DemoApplication.class, args);
	}

}
