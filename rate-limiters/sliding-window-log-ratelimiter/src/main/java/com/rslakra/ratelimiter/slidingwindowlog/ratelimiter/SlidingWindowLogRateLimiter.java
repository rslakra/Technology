package com.rslakra.ratelimiter.slidingwindowlog.ratelimiter;

import com.rslakra.ratelimiter.slidingwindowlog.config.RateLimiterProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sliding Window Log Rate Limiter implementation.
 * 
 * This algorithm maintains a log of request timestamps within the current window.
 * When a request arrives, it removes old timestamps outside the window and checks
 * if adding the new request would exceed the limit.
 * 
 * Pros: Most accurate rate limiting, prevents boundary bursts
 * Cons: Higher memory usage (stores all timestamps in window)
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class SlidingWindowLogRateLimiter {

    private final int limit;
    private final Duration windowSize;
    private final ConcurrentLinkedQueue<Instant> requestLog;
    private final ReentrantLock lock;

    public SlidingWindowLogRateLimiter(RateLimiterProperties properties) {
        this.limit = properties.getCapacity();
        this.windowSize = properties.getRefillPeriodAsDuration();
        this.requestLog = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
    }

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if rate limit is exceeded
     */
    public boolean tryConsume() {
        lock.lock();
        try {
            Instant now = Instant.now();
            Instant windowStart = now.minus(windowSize);
            
            // Remove all timestamps outside the current window
            while (!requestLog.isEmpty() && requestLog.peek().isBefore(windowStart)) {
                requestLog.poll();
            }
            
            // Check if adding this request would exceed the limit
            if (requestLog.size() >= limit) {
                return false; // Rate limit exceeded
            }
            
            // Add current request timestamp
            requestLog.offer(now);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the number of remaining requests.
     * 
     * @return remaining requests
     */
    public int getRemainingRequests() {
        lock.lock();
        try {
            Instant now = Instant.now();
            Instant windowStart = now.minus(windowSize);
            
            // Remove all timestamps outside the current window
            while (!requestLog.isEmpty() && requestLog.peek().isBefore(windowStart)) {
                requestLog.poll();
            }
            
            return Math.max(0, limit - requestLog.size());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the current number of requests in the window.
     * 
     * @return current request count
     */
    public int getCurrentRequestCount() {
        lock.lock();
        try {
            Instant now = Instant.now();
            Instant windowStart = now.minus(windowSize);
            
            // Remove all timestamps outside the current window
            while (!requestLog.isEmpty() && requestLog.peek().isBefore(windowStart)) {
                requestLog.poll();
            }
            
            return requestLog.size();
        } finally {
            lock.unlock();
        }
    }
}

