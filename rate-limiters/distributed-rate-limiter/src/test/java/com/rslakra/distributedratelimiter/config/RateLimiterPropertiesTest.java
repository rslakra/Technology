package com.rslakra.distributedratelimiter.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimiterProperties.
 * Tests configuration properties parsing and duration conversion.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class RateLimiterPropertiesTest {

    private RateLimiterProperties properties;

    @BeforeEach
    void setUp() {
        properties = new RateLimiterProperties();
    }

    @Test
    void testDefaultValues() {
        assertEquals(50, properties.getCapacity());
        assertEquals(50, properties.getRefillAmount());
        assertEquals("1m", properties.getRefillPeriod());
    }

    @Test
    void testSetAndGetCapacity() {
        properties.setCapacity(100);
        assertEquals(100, properties.getCapacity());
    }

    @Test
    void testSetAndGetRefillAmount() {
        properties.setRefillAmount(75);
        assertEquals(75, properties.getRefillAmount());
    }

    @Test
    void testSetAndGetRefillPeriod() {
        properties.setRefillPeriod("2m");
        assertEquals("2m", properties.getRefillPeriod());
    }

    @Test
    void testGetRefillPeriodAsDuration_WithMinutes() {
        properties.setRefillPeriod("5m");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(5), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithSeconds() {
        properties.setRefillPeriod("30s");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofSeconds(30), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithHours() {
        properties.setRefillPeriod("2h");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofHours(2), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithDays() {
        properties.setRefillPeriod("1d");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofDays(1), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithISO8601Format() {
        properties.setRefillPeriod("PT1M");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(1), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithNullPeriod() {
        properties.setRefillPeriod(null);
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(1), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithEmptyPeriod() {
        properties.setRefillPeriod("");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(1), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithWhitespace() {
        properties.setRefillPeriod("  2m  ");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(2), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_WithInvalidFormat_DefaultsToOneMinute() {
        properties.setRefillPeriod("invalid");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(1), duration);
    }

    @Test
    void testGetRefillPeriodAsDuration_CaseInsensitive() {
        properties.setRefillPeriod("5M");
        Duration duration = properties.getRefillPeriodAsDuration();
        assertEquals(Duration.ofMinutes(5), duration);
    }
}

