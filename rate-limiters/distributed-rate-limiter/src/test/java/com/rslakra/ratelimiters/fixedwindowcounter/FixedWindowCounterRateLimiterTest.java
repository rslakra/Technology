package com.rslakra.ratelimiters.fixedwindowcounter;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FixedWindowCounterRateLimiter.
 * Tests fixed window counter rate limiting algorithm.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class FixedWindowCounterRateLimiterTest {

    private FixedWindowCounterRateLimiter rateLimiter;
    private static final int CAPACITY = 5;
    private static final String REFILL_PERIOD = "1s";

    @BeforeEach
    void setUp() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(CAPACITY);
        properties.setRefillAmount(CAPACITY);
        properties.setRefillPeriod(REFILL_PERIOD);
        this.rateLimiter = new FixedWindowCounterRateLimiter(properties);
    }

    @Test
    void testTryConsume_WithinLimit_ShouldSucceed() {
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(rateLimiter.tryConsume(), 
                "Should allow request " + (i + 1) + " within limit");
        }
    }

    @Test
    void testTryConsume_ExceedsLimit_ShouldFail() {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        // Next request should fail
        assertFalse(rateLimiter.tryConsume(), 
            "Should reject request when limit exceeded");
    }

    @Test
    void testGetAvailableTokens_Initially_ShouldReturnCapacity() {
        int available = rateLimiter.getAvailableTokens();
        assertEquals(CAPACITY, available, 
            "Initially should have full capacity available");
    }

    @Test
    void testGetAvailableTokens_AfterConsumption_ShouldDecrease() {
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        
        int available = rateLimiter.getAvailableTokens();
        assertEquals(CAPACITY - 2, available, 
            "Available tokens should decrease after consumption");
    }

    @Test
    void testGetAvailableTokens_WhenLimitExceeded_ShouldReturnZero() {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        int available = rateLimiter.getAvailableTokens();
        assertEquals(0, available, 
            "Available tokens should be 0 when limit exceeded");
    }

    @Test
    void testImplementsRateLimiterInterface() {
        assertTrue(rateLimiter instanceof RateLimiter, 
            "FixedWindowCounterRateLimiter should implement RateLimiter interface");
    }

    @Test
    void testWindowReset_AfterPeriod() throws InterruptedException {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        // Wait for window to reset (1 second + buffer)
        Thread.sleep(1100);

        // Should be able to consume again after window reset
        assertTrue(rateLimiter.tryConsume(), 
            "Should allow requests after window resets");
        
        int available = rateLimiter.getAvailableTokens();
        assertTrue(available > 0, 
            "Should have available tokens after window reset");
    }
}

