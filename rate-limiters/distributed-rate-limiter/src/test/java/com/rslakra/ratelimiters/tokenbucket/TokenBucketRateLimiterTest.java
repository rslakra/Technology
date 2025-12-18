package com.rslakra.ratelimiters.tokenbucket;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TokenBucketRateLimiter implementation.
 * Tests the Token Bucket algorithm behavior using Bucket4j library.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
class TokenBucketRateLimiterTest {

    private TokenBucketRateLimiter rateLimiter;
    private static final int CAPACITY = 10;
    private static final int REFILL_AMOUNT = 10;
    private static final String REFILL_PERIOD = "1s";

    @BeforeEach
    void setUp() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(CAPACITY);
        properties.setRefillAmount(REFILL_AMOUNT);
        properties.setRefillPeriod(REFILL_PERIOD);
        this.rateLimiter = new TokenBucketRateLimiter(properties);
    }

    /**
     * Test that tokens can be consumed when available.
     */
    @Test
    void testTokenConsumption_WhenTokensAvailable_ShouldSucceed() {
        assertTrue(rateLimiter.tryConsume(), "Should consume 1 token successfully");
        assertTrue(rateLimiter.tryConsume(), "Should consume another token");
        
        int available = rateLimiter.getAvailableTokens();
        assertTrue(available >= 0 && available <= CAPACITY, 
            "Available tokens should be within capacity");
    }

    /**
     * Test that token consumption fails when bucket is empty.
     */
    @Test
    void testTokenConsumption_WhenBucketEmpty_ShouldFail() {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(rateLimiter.tryConsume(), "Should consume token " + (i + 1));
        }

        // Next consumption should fail
        assertFalse(rateLimiter.tryConsume(), "Should fail when bucket is empty");
        assertEquals(0, rateLimiter.getAvailableTokens(), "Available tokens should be 0");
    }

    /**
     * Test that bucket allows burst consumption up to capacity.
     */
    @Test
    void testBurstConsumption_UpToCapacity_ShouldSucceed() {
        // Consume tokens one by one up to capacity
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(rateLimiter.tryConsume(), 
                "Should allow burst consumption up to capacity: " + (i + 1));
        }
        
        // Next consumption should fail
        assertFalse(rateLimiter.tryConsume(), 
            "Should fail after burst consumption");
    }

    /**
     * Test that getAvailableTokens returns correct count.
     */
    @Test
    void testGetAvailableTokens_ShouldReturnCorrectCount() {
        // Initially should have full capacity
        int available = rateLimiter.getAvailableTokens();
        assertTrue(available >= 0 && available <= CAPACITY, 
            "Available tokens should be within capacity");

        // Consume some tokens
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        rateLimiter.tryConsume();
        
        // Available tokens should decrease
        int availableAfter = rateLimiter.getAvailableTokens();
        assertTrue(availableAfter < available, 
            "Available tokens should decrease after consumption");
    }

    /**
     * Test that multiple small consumptions work correctly.
     */
    @Test
    void testMultipleSmallConsumptions() {
        // Consume tokens one by one
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(rateLimiter.tryConsume(), 
                "Should consume token " + (i + 1) + " successfully");
        }

        // Should be empty now
        assertFalse(rateLimiter.tryConsume(), "Bucket should be empty");
        assertEquals(0, rateLimiter.getAvailableTokens(), "Available tokens should be 0");
    }

    /**
     * Test that rate limiter implements RateLimiter interface correctly.
     */
    @Test
    void testImplementsRateLimiterInterface() {
        assertTrue(rateLimiter instanceof RateLimiter, 
            "TokenBucketRateLimiter should implement RateLimiter interface");
    }

    /**
     * Test with different capacity values.
     */
    @Test
    void testWithDifferentCapacity() {
        RateLimiterProperties properties = new RateLimiterProperties();
        properties.setCapacity(5);
        properties.setRefillAmount(5);
        properties.setRefillPeriod("1s");
        
        TokenBucketRateLimiter smallRateLimiter = new TokenBucketRateLimiter(properties);
        
        // Should be able to consume up to capacity
        for (int i = 0; i < 5; i++) {
            assertTrue(smallRateLimiter.tryConsume(), 
                "Should consume token " + (i + 1) + " with capacity 5");
        }
        
        // Should fail after capacity
        assertFalse(smallRateLimiter.tryConsume(), 
            "Should fail after consuming all tokens");
    }
}

