package com.example.cachingproxy.controller;

import com.example.cachingproxy.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {
    @Autowired
    private ProxyService proxyService;

    @Value("${origin.url}")
    private String origin;

    @GetMapping("/**")
    public ResponseEntity <String> proxy (HttpServletRequest request) {
        String path = request.getRequestURI ();
        String query = request.getQueryString ();
        String url = origin + path + (query != null ? "?" + query : "");

        ResponseEntity <String> response = this.proxyService.fetchFromOrigin (url);

        HttpHeaders headers = new HttpHeaders ();
        headers.putAll (response.getHeaders ());

        if (!headers.containsKey ("X-Cache")) {
            headers.set ("X-Cache", "HIT");
        }

        return new ResponseEntity <> (response.getBody (), headers, response.getStatusCode ());
    }
}
