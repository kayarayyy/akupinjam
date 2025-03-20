package com.example.akupinjam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.akupinjam")
@EntityScan(basePackages = "com.example.akupinjam.models")
@EnableJpaRepositories(basePackages = "com.example.akupinjam.repository")
public class AkupinjamApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkupinjamApplication.class, args);
	}

}
