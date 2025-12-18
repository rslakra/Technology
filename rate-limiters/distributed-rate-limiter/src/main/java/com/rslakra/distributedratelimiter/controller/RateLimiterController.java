package com.rslakra.distributedratelimiter.controller;

import com.rslakra.distributedratelimiter.payload.response.RateLimiterTestResponse;
import com.rslakra.ratelimiters.RateLimiter;
import com.rslakra.ratelimiters.RateLimiterFactory;
import com.rslakra.ratelimiters.RateLimiterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing different rate limiter algorithms.
 * Provides endpoints to test each rate limiter type independently.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@RestController
@RequestMapping(value = "/api/v1/rate-limiters", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateLimiterController {

    private final RateLimiterFactory rateLimiterFactory;

    @Autowired
    public RateLimiterController(RateLimiterFactory rateLimiterFactory) {
        this.rateLimiterFactory = rateLimiterFactory;
    }

    /**
     * Test endpoint for Fixed Window Counter rate limiter.
     * 
     * GET /api/v1/rate-limiters/fixed-window-counter
     * 
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/fixed-window-counter")
    public ResponseEntity<RateLimiterTestResponse> testFixedWindowCounter() {
        return testRateLimiter(RateLimiterType.FIXED_WINDOW_COUNTER);
    }

    /**
     * Test endpoint for Leaky Bucket rate limiter.
     * 
     * GET /api/v1/rate-limiters/leaky-bucket
     * 
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/leaky-bucket")
    public ResponseEntity<RateLimiterTestResponse> testLeakyBucket() {
        return testRateLimiter(RateLimiterType.LEAKY_BUCKET);
    }

    /**
     * Test endpoint for Sliding Window Counter rate limiter.
     * 
     * GET /api/v1/rate-limiters/sliding-window-counter
     * 
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/sliding-window-counter")
    public ResponseEntity<RateLimiterTestResponse> testSlidingWindowCounter() {
        return testRateLimiter(RateLimiterType.SLIDING_WINDOW_COUNTER);
    }

    /**
     * Test endpoint for Sliding Window Log rate limiter.
     * 
     * GET /api/v1/rate-limiters/sliding-window-log
     * 
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/sliding-window-log")
    public ResponseEntity<RateLimiterTestResponse> testSlidingWindowLog() {
        return testRateLimiter(RateLimiterType.SLIDING_WINDOW_LOG);
    }

    /**
     * Test endpoint for Token Bucket rate limiter.
     * 
     * GET /api/v1/rate-limiters/token-bucket
     * 
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/token-bucket")
    public ResponseEntity<RateLimiterTestResponse> testTokenBucket() {
        return testRateLimiter(RateLimiterType.TOKEN_BUCKET);
    }

    /**
     * Generic test endpoint that accepts rate limiter type as path variable.
     * 
     * GET /api/v1/rate-limiters/{type}
     * 
     * @param type the rate limiter type (FIXED_WINDOW_COUNTER, LEAKY_BUCKET, etc.)
     * @return response indicating if request was allowed and available tokens
     */
    @GetMapping("/{type}")
    public ResponseEntity<RateLimiterTestResponse> testRateLimiterByType(
            @PathVariable String type) {
        try {
            RateLimiterType rateLimiterType = RateLimiterType.valueOf(type.toUpperCase().replace("-", "_"));
            return testRateLimiter(rateLimiterType);
        } catch (IllegalArgumentException e) {
            RateLimiterTestResponse errorResponse = new RateLimiterTestResponse(
                null, false, 0, "Invalid rate limiter type: " + type
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Common method to test a rate limiter.
     * 
     * @param type the rate limiter type
     * @return response with rate limit check result
     */
    private ResponseEntity<RateLimiterTestResponse> testRateLimiter(RateLimiterType type) {
        try {
            RateLimiter rateLimiter = rateLimiterFactory.getRateLimiter(type);
            boolean allowed = rateLimiter.tryConsume();
            int availableTokens = rateLimiter.getAvailableTokens();
            
            RateLimiterTestResponse response = new RateLimiterTestResponse(
                type,
                allowed,
                availableTokens,
                allowed 
                    ? "Request allowed. Available tokens: " + availableTokens
                    : "Rate limit exceeded. Available tokens: " + availableTokens
            );
            
            HttpStatus status = allowed ? HttpStatus.OK : HttpStatus.TOO_MANY_REQUESTS;
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            RateLimiterTestResponse errorResponse = new RateLimiterTestResponse(
                type, false, 0, "Error testing rate limiter: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

