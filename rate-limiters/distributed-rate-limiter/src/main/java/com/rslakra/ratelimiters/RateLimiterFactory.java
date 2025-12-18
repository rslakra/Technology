package com.rslakra.ratelimiters;

import com.rslakra.ratelimiters.fixedwindowcounter.FixedWindowCounterRateLimiter;
import com.rslakra.ratelimiters.leakybucket.LeakyBucketRateLimiter;
import com.rslakra.ratelimiters.slidingwindowcounter.SlidingWindowCounterRateLimiter;
import com.rslakra.ratelimiters.slidingwindowlog.SlidingWindowLogRateLimiter;
import com.rslakra.ratelimiters.tokenbucket.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory for creating rate limiter instances based on RateLimiterType.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Component
public class RateLimiterFactory {

    private final FixedWindowCounterRateLimiter fixedWindowCounterRateLimiter;
    private final LeakyBucketRateLimiter leakyBucketRateLimiter;
    private final SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter;
    private final SlidingWindowLogRateLimiter slidingWindowLogRateLimiter;
    private final TokenBucketRateLimiter tokenBucketRateLimiter;

    @Autowired
    public RateLimiterFactory(
            FixedWindowCounterRateLimiter fixedWindowCounterRateLimiter,
            LeakyBucketRateLimiter leakyBucketRateLimiter,
            SlidingWindowCounterRateLimiter slidingWindowCounterRateLimiter,
            SlidingWindowLogRateLimiter slidingWindowLogRateLimiter,
            TokenBucketRateLimiter tokenBucketRateLimiter) {
        this.fixedWindowCounterRateLimiter = fixedWindowCounterRateLimiter;
        this.leakyBucketRateLimiter = leakyBucketRateLimiter;
        this.slidingWindowCounterRateLimiter = slidingWindowCounterRateLimiter;
        this.slidingWindowLogRateLimiter = slidingWindowLogRateLimiter;
        this.tokenBucketRateLimiter = tokenBucketRateLimiter;
    }

    /**
     * Gets the rate limiter instance based on the specified type.
     * 
     * @param type the rate limiter type
     * @return the rate limiter instance
     * @throws IllegalArgumentException if the type is not supported
     */
    public RateLimiter getRateLimiter(RateLimiterType type) {
        if (type == null) {
            throw new IllegalArgumentException("RateLimiterType cannot be null");
        }

        return switch (type) {
            case FIXED_WINDOW_COUNTER -> fixedWindowCounterRateLimiter;
            case LEAKY_BUCKET -> leakyBucketRateLimiter;
            case SLIDING_WINDOW_COUNTER -> slidingWindowCounterRateLimiter;
            case SLIDING_WINDOW_LOG -> slidingWindowLogRateLimiter;
            case TOKEN_BUCKET -> tokenBucketRateLimiter;
        };
    }
}

