package com.rslakra.ratelimiter.fixedwindowcounter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Configuration properties for rate limiter.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterProperties {

    /**
     * Maximum capacity of the rate limiter bucket (number of tokens).
     */
    private int capacity = 50;

    /**
     * Number of tokens to refill per period.
     */
    private int refillAmount = 50;

    /**
     * Refill period as a duration string (e.g., "1m", "60s", "PT1M").
     * Supports ISO-8601 duration format or simple format like "1m", "60s".
     */
    private String refillPeriod = "1m";

    /**
     * Converts the refill period string to Duration.
     * 
     * @return Duration object
     */
    public Duration getRefillPeriodAsDuration() {
        if (refillPeriod == null || refillPeriod.isEmpty()) {
            return Duration.ofMinutes(1);
        }
        
        String period = refillPeriod.trim().toLowerCase();
        
        // Handle simple formats: "1m", "60s", "1h", etc.
        if (period.endsWith("m")) {
            long minutes = Long.parseLong(period.substring(0, period.length() - 1));
            return Duration.ofMinutes(minutes);
        } else if (period.endsWith("s")) {
            long seconds = Long.parseLong(period.substring(0, period.length() - 1));
            return Duration.ofSeconds(seconds);
        } else if (period.endsWith("h")) {
            long hours = Long.parseLong(period.substring(0, period.length() - 1));
            return Duration.ofHours(hours);
        } else if (period.endsWith("d")) {
            long days = Long.parseLong(period.substring(0, period.length() - 1));
            return Duration.ofDays(days);
        } else {
            // Try to parse as ISO-8601 duration format (e.g., "PT1M", "PT60S")
            try {
                return Duration.parse(period);
            } catch (Exception e) {
                // Default to 1 minute if parsing fails
                return Duration.ofMinutes(1);
            }
        }
    }
}

