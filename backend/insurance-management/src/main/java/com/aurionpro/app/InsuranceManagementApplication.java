package com.aurionpro.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class InsuranceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceManagementApplication.class, args);
	}

}
