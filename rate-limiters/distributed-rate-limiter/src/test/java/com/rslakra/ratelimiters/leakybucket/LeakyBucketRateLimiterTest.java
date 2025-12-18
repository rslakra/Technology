package com.rslakra.ratelimiters.leakybucket;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LeakyBucketRateLimiter.
 * Tests leaky bucket rate limiting algorithm.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class LeakyBucketRateLimiterTest {

    private LeakyBucketRateLimiter rateLimiter;
    private static final int CAPACITY = 5;
    private static final int REFILL_AMOUNT = 5;
    private static final String REFILL_PERIOD = "1s";

    @BeforeEach
    void setUp() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(CAPACITY);
        properties.setRefillAmount(REFILL_AMOUNT);
        properties.setRefillPeriod(REFILL_PERIOD);
        this.rateLimiter = new LeakyBucketRateLimiter(properties);
    }

    @Test
    void testTryConsume_WithinCapacity_ShouldSucceed() {
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(rateLimiter.tryConsume(), 
                "Should allow request " + (i + 1) + " within capacity");
        }
    }

    @Test
    void testTryConsume_ExceedsCapacity_ShouldFail() {
        // Fill bucket to capacity
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        // Next request should fail (bucket is full)
        assertFalse(rateLimiter.tryConsume(), 
            "Should reject request when bucket is full");
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
    void testGetAvailableTokens_WhenFull_ShouldReturnZero() {
        // Fill bucket to capacity
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        int available = rateLimiter.getAvailableTokens();
        assertEquals(0, available, 
            "Available tokens should be 0 when bucket is full");
    }

    @Test
    void testGetCurrentLevel_AfterConsumption() {
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        
        int level = rateLimiter.getCurrentLevel();
        assertEquals(2, level, 
            "Current level should reflect number of requests in bucket");
    }

    @Test
    void testGetRemainingCapacity_AfterConsumption() {
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        
        int remaining = rateLimiter.getRemainingCapacity();
        assertEquals(CAPACITY - 2, remaining, 
            "Remaining capacity should decrease after consumption");
    }

    @Test
    void testImplementsRateLimiterInterface() {
        assertTrue(rateLimiter instanceof RateLimiter, 
            "LeakyBucketRateLimiter should implement RateLimiter interface");
    }

    @Test
    void testLeak_AfterPeriod() throws InterruptedException {
        // Fill bucket to capacity
        for (int i = 0; i < CAPACITY; i++) {
            rateLimiter.tryConsume();
        }

        // Wait for leak period (1 second + buffer)
        Thread.sleep(1100);

        // Should be able to consume again after leak
        assertTrue(rateLimiter.tryConsume(), 
            "Should allow requests after bucket leaks");
        
        int available = rateLimiter.getAvailableTokens();
        assertTrue(available > 0, 
            "Should have available tokens after leak");
    }
}

