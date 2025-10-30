package com.samjsddevelopment.applicationtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.spring.client.annotation.Deployment;


@Deployment(resources = {
	"classpath*:/process/**/*.bpmn"
})
@SpringBootApplication
public class ApplicationTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTrackerApplication.class, args);
	}

}
