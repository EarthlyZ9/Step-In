package com.earthlyz9.stepin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class StepInApplication {

	public static void main(String[] args) {
		SpringApplication.run(StepInApplication.class, args);
	}

}
