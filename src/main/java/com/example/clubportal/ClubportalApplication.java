package com.example.clubportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ClubportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClubportalApplication.class, args);
	}

}
