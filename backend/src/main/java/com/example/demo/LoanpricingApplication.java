package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoanpricingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanpricingApplication.class, args);
		System.out.println("Backend running on port 9999");
	}

}
