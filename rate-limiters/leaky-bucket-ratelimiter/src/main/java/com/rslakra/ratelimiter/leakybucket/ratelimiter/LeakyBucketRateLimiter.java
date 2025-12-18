package com.rslakra.ratelimiter.leakybucket.ratelimiter;

import com.rslakra.ratelimiter.leakybucket.config.RateLimiterProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Leaky Bucket Rate Limiter implementation.
 * 
 * This algorithm maintains a bucket with a fixed capacity. Requests are added
 * to the bucket, and the bucket leaks requests at a constant rate. If the bucket
 * is full when a request arrives, the request is rejected.
 * 
 * Pros: Smooths out traffic bursts, constant output rate
 * Cons: Can reject requests even when average rate is acceptable
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class LeakyBucketRateLimiter {

    private final int capacity;
    private final long leakRate; // requests per period
    private final Duration period;
    private final AtomicInteger currentLevel;
    private volatile Instant lastLeakTime;

    public LeakyBucketRateLimiter(RateLimiterProperties properties) {
        this.capacity = properties.getCapacity();
        this.period = properties.getRefillPeriodAsDuration();
        this.leakRate = properties.getRefillAmount();
        this.currentLevel = new AtomicInteger(0);
        this.lastLeakTime = Instant.now();
    }

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if bucket is full
     */
    public synchronized boolean tryConsume() {
        Instant now = Instant.now();
        
        // Calculate how many requests have leaked since last check
        long elapsedNanos = Duration.between(lastLeakTime, now).toNanos();
        long periodNanos = period.toNanos();
        
        if (elapsedNanos > 0) {
            // Calculate leaked requests (proportional to elapsed time)
            long leakedRequests = (elapsedNanos * leakRate) / periodNanos;
            
            if (leakedRequests > 0) {
                int current = currentLevel.get();
                int newLevel = Math.max(0, current - (int) leakedRequests);
                currentLevel.set(newLevel);
                lastLeakTime = now;
            }
        }
        
        // Check if bucket has space
        int current = currentLevel.get();
        if (current >= capacity) {
            return false; // Bucket is full
        }
        
        // Add request to bucket
        currentLevel.incrementAndGet();
        return true;
    }

    /**
     * Gets the current level of the bucket.
     * 
     * @return current number of requests in bucket
     */
    public int getCurrentLevel() {
        // Update leak before returning
        tryConsume();
        return currentLevel.get();
    }

    /**
     * Gets the number of remaining capacity in the bucket.
     * 
     * @return remaining capacity
     */
    public int getRemainingCapacity() {
        return Math.max(0, capacity - getCurrentLevel());
    }
}

