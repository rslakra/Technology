package com.rslakra.ratelimiters.fixedwindowcounter;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fixed Window Counter Rate Limiter implementation.
 * 
 * This algorithm divides time into fixed-size windows and maintains a counter
 * for each window. When a request arrives, it checks if the current window's
 * counter has exceeded the limit. At the start of a new window, the counter resets.
 * 
 * Pros: Simple, memory efficient
 * Cons: Can allow bursts at window boundaries (e.g., 50 requests at end of window 1
 *       and 50 requests at start of window 2 = 100 requests in 2 seconds)
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class FixedWindowCounterRateLimiter implements RateLimiter {

    private final int limit;
    private final Duration windowSize;
    private volatile Window currentWindow;

    public FixedWindowCounterRateLimiter(RateLimiterProperties properties) {
        this.limit = properties.getCapacity();
        this.windowSize = properties.getRefillPeriodAsDuration();
        this.currentWindow = new Window(Instant.now(), new AtomicInteger(0));
    }

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if rate limit is exceeded
     */
    @Override
    public synchronized boolean tryConsume() {
        Instant now = Instant.now();
        
        // Check if we need to start a new window
        if (now.isAfter(currentWindow.getStartTime().plus(windowSize))) {
            // Start a new window
            currentWindow = new Window(now, new AtomicInteger(0));
        }
        
        // Check if current window has exceeded the limit
        int currentCount = currentWindow.getCounter().get();
        if (currentCount >= limit) {
            return false; // Rate limit exceeded
        }
        
        // Increment counter and allow request
        currentWindow.getCounter().incrementAndGet();
        return true;
    }

    /**
     * Gets the number of available tokens in the current window.
     * 
     * @return available tokens
     */
    @Override
    public synchronized int getAvailableTokens() {
        Instant now = Instant.now();
        
        // Check if we need to start a new window
        if (now.isAfter(currentWindow.getStartTime().plus(windowSize))) {
            return limit; // New window, all requests available
        }
        
        return Math.max(0, limit - currentWindow.getCounter().get());
    }

    /**
     * Represents a time window with its counter.
     */
    private static class Window {
        private final Instant startTime;
        private final AtomicInteger counter;

        public Window(Instant startTime, AtomicInteger counter) {
            this.startTime = startTime;
            this.counter = counter;
        }

        public Instant getStartTime() {
            return startTime;
        }

        public AtomicInteger getCounter() {
            return counter;
        }
    }
}
