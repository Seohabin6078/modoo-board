package com.modooboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ModooBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModooBoardApplication.class, args);
	}

}
