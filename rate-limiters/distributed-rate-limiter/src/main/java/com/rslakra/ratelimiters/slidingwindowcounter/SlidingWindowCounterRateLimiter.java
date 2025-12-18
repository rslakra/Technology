package com.rslakra.ratelimiters.slidingwindowcounter;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sliding Window Counter Rate Limiter implementation.
 * 
 * This algorithm divides time into smaller sub-windows and maintains counters
 * for each sub-window. The total count is calculated by summing the current
 * window and the previous window proportionally. This provides more accurate
 * rate limiting than Fixed Window Counter.
 * 
 * Pros: More accurate than fixed window, prevents boundary bursts
 * Cons: More memory usage than fixed window
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class SlidingWindowCounterRateLimiter implements RateLimiter {

    private final int limit;
    private final Duration windowSize;
    private final int numberOfSubWindows;
    private final Duration subWindowSize;
    private final AtomicInteger[] subWindowCounters;
    private volatile int currentSubWindowIndex;
    private volatile Instant lastWindowStart;

    public SlidingWindowCounterRateLimiter(RateLimiterProperties properties) {
        this.limit = properties.getCapacity();
        this.windowSize = properties.getRefillPeriodAsDuration();
        // Use 10 sub-windows for better accuracy
        this.numberOfSubWindows = 10;
        this.subWindowSize = Duration.ofNanos(windowSize.toNanos() / numberOfSubWindows);
        this.subWindowCounters = new AtomicInteger[numberOfSubWindows];
        for (int i = 0; i < numberOfSubWindows; i++) {
            this.subWindowCounters[i] = new AtomicInteger(0);
        }
        this.currentSubWindowIndex = 0;
        this.lastWindowStart = Instant.now();
    }

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if rate limit is exceeded
     */
    @Override
    public synchronized boolean tryConsume() {
        Instant now = Instant.now();
        
        // Calculate which sub-window we're in
        long elapsedNanos = Duration.between(lastWindowStart, now).toNanos();
        long subWindowNanos = subWindowSize.toNanos();
        
        // Calculate how many sub-windows have passed
        int subWindowsPassed = (int) (elapsedNanos / subWindowNanos);
        
        if (subWindowsPassed > 0) {
            // Clear old sub-windows
            int windowsToClear = Math.min(subWindowsPassed, numberOfSubWindows);
            for (int i = 0; i < windowsToClear; i++) {
                int indexToClear = (currentSubWindowIndex + i) % numberOfSubWindows;
                subWindowCounters[indexToClear].set(0);
            }
            
            // Update current sub-window index
            currentSubWindowIndex = (currentSubWindowIndex + subWindowsPassed) % numberOfSubWindows;
            lastWindowStart = lastWindowStart.plus(Duration.ofNanos(subWindowNanos * subWindowsPassed));
        }
        
        // Calculate weighted count (current window + proportional part of previous window)
        double weightedCount = calculateWeightedCount(now);
        
        if (weightedCount >= limit) {
            return false; // Rate limit exceeded
        }
        
        // Increment current sub-window counter
        subWindowCounters[currentSubWindowIndex].incrementAndGet();
        return true;
    }

    /**
     * Calculates the weighted count across sub-windows.
     * 
     * @param now current time
     * @return weighted count
     */
    private double calculateWeightedCount(Instant now) {
        long elapsedNanos = Duration.between(lastWindowStart, now).toNanos();
        long subWindowNanos = subWindowSize.toNanos();
        
        // Calculate how far into the current sub-window we are (0.0 to 1.0)
        double currentSubWindowProgress = (elapsedNanos % subWindowNanos) / (double) subWindowNanos;
        
        // Get count from current sub-window
        int currentCount = subWindowCounters[currentSubWindowIndex].get();
        
        // Get count from previous sub-window (weighted by remaining time)
        int previousSubWindowIndex = (currentSubWindowIndex - 1 + numberOfSubWindows) % numberOfSubWindows;
        int previousCount = subWindowCounters[previousSubWindowIndex].get();
        
        // Weighted count = current window count + (previous window count * remaining time ratio)
        return currentCount + (previousCount * (1.0 - currentSubWindowProgress));
    }

    /**
     * Gets the number of available tokens.
     * 
     * @return available tokens
     */
    @Override
    public synchronized int getAvailableTokens() {
        Instant now = Instant.now();
        double weightedCount = calculateWeightedCount(now);
        return Math.max(0, (int) (limit - weightedCount));
    }
}

