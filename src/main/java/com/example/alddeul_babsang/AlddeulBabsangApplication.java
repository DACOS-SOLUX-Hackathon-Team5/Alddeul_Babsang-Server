package com.example.alddeul_babsang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AlddeulBabsangApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlddeulBabsangApplication.class, args);
	}

}
