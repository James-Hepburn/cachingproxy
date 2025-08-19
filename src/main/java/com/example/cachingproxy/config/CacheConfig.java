package com.example.cachingproxy.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager () {
        CaffeineCacheManager manager = new CaffeineCacheManager ("responses");
        manager.setCaffeine (Caffeine.newBuilder ().maximumSize (1000).expireAfterWrite (Duration.ofMinutes (10)));
        return manager;
    }
}
