package com.rslakra.ratelimiters;

/**
 * Enumeration of available rate limiting algorithm types.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
public enum RateLimiterType {
    
    /**
     * Fixed Window Counter algorithm.
     * Divides time into fixed-size windows with counters.
     */
    FIXED_WINDOW_COUNTER,
    
    /**
     * Leaky Bucket algorithm.
     * Maintains a bucket that leaks requests at a constant rate.
     */
    LEAKY_BUCKET,
    
    /**
     * Sliding Window Counter algorithm.
     * Uses sub-windows with weighted counting for better accuracy.
     */
    SLIDING_WINDOW_COUNTER,
    
    /**
     * Sliding Window Log algorithm.
     * Maintains a log of request timestamps for most accurate rate limiting.
     */
    SLIDING_WINDOW_LOG,
    
    /**
     * Token Bucket algorithm (using Bucket4j library).
     * Allows bursts while maintaining average rate limit.
     */
    TOKEN_BUCKET
}

