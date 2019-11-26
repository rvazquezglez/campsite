package com.volcano.campsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CampsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampsiteApplication.class, args);
	}

}
