package com.example.cachingproxy.controller;

import com.example.cachingproxy.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class ProxyController {
    @Autowired
    private ProxyService proxyService;

    @Value("${origin.url:}")
    private String origin;

    private ConcurrentMap <String, Boolean> cacheTracker = new ConcurrentHashMap <> ();

    @GetMapping("/**")
    public ResponseEntity <String> proxy (HttpServletRequest request) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String url = this.origin + path + (query != null ? "?" + query : "");

        boolean fromCache = this.cacheTracker.containsKey(url);
        this.cacheTracker.putIfAbsent(url, true);
        String body = this.proxyService.fetchBodyFromOrigin(url);

        return this.proxyService.buildResponse (body, fromCache);
    }
}
