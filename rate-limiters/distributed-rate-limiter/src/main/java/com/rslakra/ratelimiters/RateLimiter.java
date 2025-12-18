package com.rslakra.ratelimiters;

/**
 * Interface for rate limiting implementations.
 * Provides a common contract for all rate limiting algorithms.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
public interface RateLimiter {

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if rate limit is exceeded
     */
    boolean tryConsume();

    /**
     * Gets the number of available tokens/requests.
     * 
     * @return available tokens/requests
     */
    int getAvailableTokens();
}

