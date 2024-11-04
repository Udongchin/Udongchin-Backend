package com.api.udc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UdcApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdcApplication.class, args);
	}

}
