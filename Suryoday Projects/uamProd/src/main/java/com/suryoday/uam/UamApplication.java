package com.suryoday.uam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.suryoday.*"})
public class UamApplication {

	public static void main(String[] args) {
		SpringApplication.run(UamApplication.class, args);
	}

}
