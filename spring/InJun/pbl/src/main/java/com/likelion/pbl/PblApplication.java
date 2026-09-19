package com.likelion.pbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.likelion.pbl.week11")
@EntityScan(basePackages = "com.likelion.pbl.week11")
@EnableJpaRepositories(basePackages = "com.likelion.pbl.week11")
public class PblApplication {

	public static void main(String[] args) {
		SpringApplication.run(PblApplication.class, args);
	}
}
