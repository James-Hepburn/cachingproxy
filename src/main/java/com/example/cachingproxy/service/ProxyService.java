package com.example.cachingproxy.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProxyService {
    private RestTemplate restTemplate = new RestTemplate ();

    @Cacheable(value = "responses", key = "#url")
    public String fetchBodyFromOrigin (String url) {
        return this.restTemplate.getForObject (url, String.class);
    }

    public ResponseEntity <String> buildResponse (String body, boolean fromCache) {
        return ResponseEntity.status (HttpStatus.OK)
                .header ("X-Cache", fromCache ? "HIT" : "MISS")
                .body (body);
    }
}
