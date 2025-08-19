package com.example.cachingproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.HashMap;
import java.util.Map;

@EnableCaching
@SpringBootApplication
public class CachingproxyApplication {

	public static void main(String[] args) {
		Map <String, Object> properties = new HashMap<>();
		String origin = null;

		for (int i = 0; i < args.length; i++) {
			if (args [i].startsWith ("--origin=")) {
				origin = args [i].substring ("--origin=".length ());
			} else if (args [i].startsWith ("--server.port=")) {
				properties.put ("server.port", Integer.parseInt (args [i].substring ("--server.port=".length ())));
			} else if ("--clear-cache".equals (args [i])) {
				properties.put ("clear-cache", true);
			}
		}

		if (origin == null) {
			System.err.println ("Origin URL must be provided with --origin");
			return;
		}

		properties.put ("origin.url", origin);

		SpringApplication app = new SpringApplication (CachingproxyApplication.class);
		app.setDefaultProperties (properties);
		app.run (args);

		System.out.printf ("Caching proxy started on port %s, forwarding to %s%n", properties.get ("server.port"), origin);
	}
}
