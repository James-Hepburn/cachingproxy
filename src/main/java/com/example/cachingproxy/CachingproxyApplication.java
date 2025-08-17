package com.example.cachingproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CachingproxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingproxyApplication.class, args);
	}

}
