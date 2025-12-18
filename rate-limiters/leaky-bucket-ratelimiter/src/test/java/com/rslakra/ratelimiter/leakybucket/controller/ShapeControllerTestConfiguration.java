package com.rslakra.ratelimiter.leakybucket.controller;

import com.rslakra.ratelimiter.leakybucket.config.RateLimiterProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration to provide beans for WebMvcTest.
 * This ensures RateLimiterProperties is available for context loading.
 */
@TestConfiguration
public class ShapeControllerTestConfiguration {

    @Bean
    @Primary
    public RateLimiterProperties rateLimiterProperties() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(50);
        properties.setRefillAmount(50);
        properties.setRefillPeriod("1m");
        return properties;
    }
}

