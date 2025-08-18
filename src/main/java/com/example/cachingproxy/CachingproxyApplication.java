package com.example.cachingproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@EnableCaching
@SpringBootApplication
public class CachingproxyApplication {

	public static void main(String[] args) {
		if (args.length > 0 && "--clear-cache".equals (args [0])) {
			SpringApplication app = new SpringApplication (CachingproxyApplication.class);
			app.setWebApplicationType (WebApplicationType.NONE);
			ConfigurableApplicationContext context = app.run (args);
			CacheManager cacheManager = context.getBean (CacheManager.class);
			cacheManager.getCache ("responses").clear ();
			System.out.println ("Cache cleared");
			context.close ();
			return;
		}

		int port = 3000;
		String origin = null;

		for (int i = 0; i < args.length; i++) {
			if ("--port".equals (args [i]) && i + 1 < args.length) {
				port = Integer.parseInt (args [i + 1]);
			} else if ("--origin".equals (args[i]) && i + 1 < args.length) {
				origin = args [i + 1];
			}
		}

		if (origin == null) {
			System.err.println ("Origin URL must be provided with --origin");
			return;
		}

		SpringApplication app = new SpringApplication (CachingproxyApplication.class);
		app.setDefaultProperties (Map.of ("server.port", port, "origin.url", origin));
		app.run (args);
	}

}
