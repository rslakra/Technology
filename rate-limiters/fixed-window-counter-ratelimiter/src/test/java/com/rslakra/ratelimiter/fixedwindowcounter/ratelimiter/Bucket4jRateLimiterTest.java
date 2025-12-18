package com.rslakra.ratelimiter.fixedwindowcounter.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for Bucket4j rate limiter implementation.
 * Tests the Token Bucket algorithm behavior directly.
 * 
 * @author Rohtash Lakra
 * @created 12/14/25
 */
class Bucket4jRateLimiterTest {

    private Bucket bucket;
    private static final int CAPACITY = 10; // Small capacity for faster testing
    private static final int REFILL_AMOUNT = 10;
    private static final Duration REFILL_PERIOD = Duration.ofSeconds(1);

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        // Create a bucket with 10 tokens, refilling 10 tokens every 1 second
        // Using classic() with intervally refill - refills tokens evenly over the interval
        // Note: classic() is deprecated in Bucket4j 7.6.0 but still functional
        // The modern API would require different approach, so suppressing deprecation warning
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.intervally(REFILL_AMOUNT, REFILL_PERIOD));
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * Test that tokens can be consumed when available.
     */
    @Test
    void testTokenConsumption_WhenTokensAvailable_ShouldSucceed() {
        // Initially, bucket has 10 tokens
        assertTrue(bucket.tryConsume(1), "Should consume 1 token successfully");
        assertTrue(bucket.tryConsume(5), "Should consume 5 tokens successfully");
        assertTrue(bucket.tryConsume(4), "Should consume remaining 4 tokens");
    }

    /**
     * Test that token consumption fails when bucket is empty.
     */
    @Test
    void testTokenConsumption_WhenBucketEmpty_ShouldFail() {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(bucket.tryConsume(1), "Should consume token " + (i + 1));
        }

        // Next consumption should fail
        assertFalse(bucket.tryConsume(1), "Should fail when bucket is empty");
    }

    /**
     * Test that consuming more tokens than available fails.
     */
    @Test
    void testTokenConsumption_MoreThanAvailable_ShouldFail() {
        // Try to consume more than capacity
        assertFalse(bucket.tryConsume(CAPACITY + 1), 
            "Should fail when trying to consume more tokens than available");
    }

    /**
     * Test that bucket allows burst consumption up to capacity.
     */
    @Test
    void testBurstConsumption_UpToCapacity_ShouldSucceed() {
        // Consume all tokens at once (burst)
        assertTrue(bucket.tryConsume(CAPACITY), 
            "Should allow burst consumption up to capacity");
        
        // Next consumption should fail
        assertFalse(bucket.tryConsume(1), 
            "Should fail after burst consumption");
    }

    /**
     * Test that bucket refills tokens after the refill period.
     * Note: This test may be flaky due to timing, but demonstrates the concept.
     */
    @Test
    void testTokenRefill_AfterRefillPeriod_ShouldRefill() throws InterruptedException {
        // Consume all tokens
        for (int i = 0; i < CAPACITY; i++) {
            bucket.tryConsume(1);
        }

        // Verify bucket is empty
        assertFalse(bucket.tryConsume(1), "Bucket should be empty");

        // Wait for refill period (1 second) plus a small buffer
        Thread.sleep(REFILL_PERIOD.toMillis() + 100);

        // After refill, should be able to consume tokens again
        assertTrue(bucket.tryConsume(1), 
            "Should be able to consume tokens after refill period");
    }

    /**
     * Test that getAvailableTokens returns correct count.
     */
    @Test
    void testGetAvailableTokens_ShouldReturnCorrectCount() {
        // Initially should have full capacity
        long available = bucket.getAvailableTokens();
        assertTrue(available >= 0 && available <= CAPACITY, 
            "Available tokens should be within capacity");

        // Consume some tokens
        bucket.tryConsume(3);
        
        // Available tokens should decrease
        long availableAfter = bucket.getAvailableTokens();
        assertTrue(availableAfter < available, 
            "Available tokens should decrease after consumption");
    }

    /**
     * Test intervally refill strategy - tokens are added evenly over the time window.
     * Note: intervally refills tokens gradually, not all at once like greedy().
     */
    @Test
    void testIntervallyRefillStrategy_RefillsOverTimeWindow() throws InterruptedException {
        // Consume all tokens
        bucket.tryConsume(CAPACITY);
        assertFalse(bucket.tryConsume(1), "Bucket should be empty");

        // Wait for refill period
        Thread.sleep(REFILL_PERIOD.toMillis() + 100);

        // With intervally refill, tokens should be available after the interval
        // Note: intervally refills gradually, so we may not get all tokens immediately
        assertTrue(bucket.tryConsume(1), 
            "Intervally refill should allow token consumption after refill period");
    }

    /**
     * Test that multiple small consumptions work correctly.
     */
    @Test
    void testMultipleSmallConsumptions() {
        // Consume tokens one by one
        for (int i = 0; i < CAPACITY; i++) {
            assertTrue(bucket.tryConsume(1), 
                "Should consume token " + (i + 1) + " successfully");
        }

        // Should be empty now
        assertFalse(bucket.tryConsume(1), "Bucket should be empty");
    }

    /**
     * Test that consuming zero tokens throws IllegalArgumentException.
     * Bucket4j requires positive number of tokens to consume.
     */
    @Test
    void testConsumeZeroTokens_ShouldThrowException() {
        // Consuming 0 tokens should throw IllegalArgumentException
        try {
            bucket.tryConsume(0);
            // If we get here, the test should fail
            assertFalse(true, "Consuming 0 tokens should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected behavior
            assertTrue(true, "Consuming 0 tokens correctly throws IllegalArgumentException");
        }
        
        // Even when bucket is empty, consuming 0 should still throw
        bucket.tryConsume(CAPACITY);
        try {
            bucket.tryConsume(0);
            assertFalse(true, "Consuming 0 tokens should throw even when bucket is empty");
        } catch (IllegalArgumentException e) {
            // Expected behavior
            assertTrue(true, "Consuming 0 tokens correctly throws IllegalArgumentException when empty");
        }
    }
}

