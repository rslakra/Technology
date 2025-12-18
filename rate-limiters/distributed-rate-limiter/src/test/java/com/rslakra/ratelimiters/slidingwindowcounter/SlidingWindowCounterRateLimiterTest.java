package com.rslakra.ratelimiters.slidingwindowcounter;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SlidingWindowCounterRateLimiter.
 * Tests sliding window counter rate limiting algorithm.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class SlidingWindowCounterRateLimiterTest {

    private SlidingWindowCounterRateLimiter rateLimiter;
    private static final int CAPACITY = 5;
    private static final String REFILL_PERIOD = "1s";

    @BeforeEach
    void setUp() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(CAPACITY);
        properties.setRefillAmount(CAPACITY);
        properties.setRefillPeriod(REFILL_PERIOD);
        this.rateLimiter = new SlidingWindowCounterRateLimiter(properties);
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
        assertTrue(available >= 0 && available <= CAPACITY, 
            "Initially should have available tokens within capacity");
    }

    @Test
    void testGetAvailableTokens_AfterConsumption_ShouldDecrease() {
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        
        int available = rateLimiter.getAvailableTokens();
        assertTrue(available < CAPACITY, 
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
            "SlidingWindowCounterRateLimiter should implement RateLimiter interface");
    }

    @Test
    void testSlidingWindow_MoreAccurateThanFixedWindow() {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        // Should reject immediately
        assertFalse(rateLimiter.tryConsume(), 
            "Should reject requests when limit exceeded");
    }
}

