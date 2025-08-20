package com.example.cachingproxy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CachingproxyApplication {
	private static String origin;
	private static int port;

	public static Map <String, String> parseArguments (String [] args) {
		Map <String, String> parameters = new HashMap<>();

		for (int i = 0; i < args.length; i++) {
			if (args [i].startsWith ("--")) {
				String key = args [i].substring (2);

				if (i + 1 < args.length && !args [i + 1].startsWith ("--")) {
					parameters.put (key, args [++i]);
				} else {
					parameters.put (key, "");
				}
			}
		}

		return parameters;
	}

	public static void main(String[] args) {
		Map <String, String> parameters = parseArguments (args);

		if (parameters.containsKey ("help")) {
			System.out.println("How to use:");
			System.out.println("  caching-proxy --port <number> --origin <url>");
			System.out.println("  caching-proxy --clear-cache");
			return;
		}

		if (parameters.containsKey ("port")) {
			port = Integer.parseInt (parameters.get ("pot"));
			System.setProperty ("server.port", String.valueOf (port));
		}

		if (parameters.containsKey ("origin")) {
			origin = parameters.get ("origin");
			System.setProperty ("proxy.origin", origin);
		}

		if (parameters.containsKey ("clear-cache")) {
			try {
				new RestTemplate ().postForLocation ("http://localhost: " + port + "/clear-cache", null);
				System.out.println ("Cache cleared");
			} catch (Exception e) {
				System.err.println ("Cache not cleared");
			}

			return;
		}

		new SpringApplicationBuilder (CachingproxyApplication.class)
				.web (WebApplicationType.SERVLET)
				.properties ("server.port=" + port, "proxy.origin=" + origin)
				.run (args);
	}

}
