package com.rslakra.distributedratelimiter.config;

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
        
        String period = refillPeriod.trim();
        
        // Try ISO-8601 format first (e.g., "PT1M", "PT60S", "PT1H")
        // ISO-8601 format is case-sensitive and starts with "PT"
        if (period.toUpperCase().startsWith("PT")) {
            try {
                return Duration.parse(period);
            } catch (Exception e) {
                // If ISO-8601 parsing fails, fall through to simple format parsing
            }
        }
        
        // Handle simple formats: "1m", "60s", "1h", etc.
        String periodLower = period.toLowerCase();
        if (periodLower.endsWith("m")) {
            try {
                long minutes = Long.parseLong(periodLower.substring(0, periodLower.length() - 1));
                return Duration.ofMinutes(minutes);
            } catch (NumberFormatException e) {
                return Duration.ofMinutes(1);
            }
        } else if (periodLower.endsWith("s")) {
            try {
                long seconds = Long.parseLong(periodLower.substring(0, periodLower.length() - 1));
                return Duration.ofSeconds(seconds);
            } catch (NumberFormatException e) {
                return Duration.ofMinutes(1);
            }
        } else if (periodLower.endsWith("h")) {
            try {
                long hours = Long.parseLong(periodLower.substring(0, periodLower.length() - 1));
                return Duration.ofHours(hours);
            } catch (NumberFormatException e) {
                return Duration.ofMinutes(1);
            }
        } else if (periodLower.endsWith("d")) {
            try {
                long days = Long.parseLong(periodLower.substring(0, periodLower.length() - 1));
                return Duration.ofDays(days);
            } catch (NumberFormatException e) {
                return Duration.ofMinutes(1);
            }
        } else {
            // Try to parse as ISO-8601 duration format (case-insensitive)
            try {
                return Duration.parse(period);
            } catch (Exception e) {
                // Default to 1 minute if parsing fails
                return Duration.ofMinutes(1);
            }
        }
    }
}

