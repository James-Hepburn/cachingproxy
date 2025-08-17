package com.example.cachingproxy.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProxyService {
    private RestTemplate restTemplate = new RestTemplate ();

    @Cacheable(value = "responses", key = "#url")
    public ResponseEntity <String> fetchFromOrigin (String url) {
        ResponseEntity <String> response = this.restTemplate.getForEntity (url, String.class);

        HttpHeaders headers = new HttpHeaders ();
        headers.putAll (response.getHeaders ());
        headers.set ("X-Cache", "MISS");

        return new ResponseEntity <> (response.getBody (), headers, response.getStatusCode ());
    }
}
