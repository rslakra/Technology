package com.rslakra.distributedratelimiter.payload.response;

import com.rslakra.ratelimiters.RateLimiterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for rate limiter test endpoints.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateLimiterTestResponse {

    private RateLimiterType rateLimiterType;
    private boolean allowed;
    private int availableTokens;
    private String message;
}

