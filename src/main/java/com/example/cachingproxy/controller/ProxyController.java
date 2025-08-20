package com.example.cachingproxy.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ProxyController {
    @Value("${proxy.origin}")
    private String origin;

    private RestTemplate restTemplate;
    private Cache <String, ResponseEntity <String>> cache = CacheBuilder.newBuilder ()
            .expireAfterWrite (10, TimeUnit.MINUTES)
            .build ();

    @PostConstruct
    public void init () {
        this.restTemplate = new RestTemplate ();
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity <String> test (HttpServletRequest request) throws Exception {
        String path = request.getRequestURI ();
        String query = request.getQueryString ();
        String url = this.origin + path + (query != null ? "?" + query : "");

        ResponseEntity <String> cacheResponse = this.cache.getIfPresent (url);

        if (cacheResponse != null){
            return ResponseEntity.status (cacheResponse.getStatusCode ())
                    .headers (cacheResponse.getHeaders ())
                    .header ("X-Cache", "HIT")
                    .body (cacheResponse.getBody ());
        }

        ResponseEntity <String> response = this.restTemplate.getForEntity (url, String.class);
        this.cache.put (url, response);

        return ResponseEntity.status (response.getStatusCode ())
                .headers (response.getHeaders ())
                .header ("X-Cache", "MISS")
                .body (response.getBody ());
    }
}
