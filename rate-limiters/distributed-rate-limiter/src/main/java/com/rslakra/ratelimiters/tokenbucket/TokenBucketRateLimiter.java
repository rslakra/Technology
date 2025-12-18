package com.rslakra.ratelimiters.tokenbucket;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.ratelimiters.RateLimiter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

/**
 * Token Bucket Rate Limiter implementation using Bucket4j library.
 * 
 * This algorithm allows bursts of traffic while maintaining an average rate limit over time.
 * Tokens are added to the bucket at a constant rate, and each request consumes one token.
 * If no tokens are available, the request is rejected.
 * 
 * Pros: Allows bursts, smooth average rate limiting, industry-standard
 * Cons: Requires external library (Bucket4j)
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class TokenBucketRateLimiter implements RateLimiter {

    private final Bucket bucket;

    @SuppressWarnings("deprecation")
    public TokenBucketRateLimiter(RateLimiterProperties properties) {
        // Token Bucket algorithm using Bucket4j
        // Using classic() with intervally refill - configurable via application.properties
        // Note: classic() is deprecated in Bucket4j 7.6.0 but still functional
        Bandwidth limit = Bandwidth.classic(
            properties.getCapacity(),
            Refill.intervally(
                properties.getRefillAmount(),
                properties.getRefillPeriodAsDuration()
            )
        );
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * Attempts to allow a request.
     * 
     * @return true if request is allowed, false if rate limit is exceeded
     */
    @Override
    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }

    /**
     * Gets the number of available tokens.
     * 
     * @return available tokens
     */
    @Override
    public int getAvailableTokens() {
        return (int) bucket.getAvailableTokens();
    }
}
