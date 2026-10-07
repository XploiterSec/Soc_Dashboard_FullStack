package com.example.SocDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocDashboardApplication.class, args);
	}

}
