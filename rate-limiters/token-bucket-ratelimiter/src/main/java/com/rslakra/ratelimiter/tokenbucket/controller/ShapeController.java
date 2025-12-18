package com.rslakra.ratelimiter.tokenbucket.controller;

import com.rslakra.ratelimiter.tokenbucket.config.RateLimiterProperties;
import com.rslakra.ratelimiter.tokenbucket.payload.request.ShapeRequest;
import com.rslakra.ratelimiter.tokenbucket.payload.response.ShapeResponse;
import com.rslakra.ratelimiter.tokenbucket.service.ShapeService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for shape perimeter calculations with Token Bucket rate limiting.
 * 
 * Uses Bucket4j library which implements the Token Bucket algorithm.
 * Tokens are added to the bucket at a constant rate, and each request
 * consumes one token. If no tokens are available, the request is rejected.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@RestController
@RequestMapping(value = "/api/v1/perimeters", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShapeController {

    private final Bucket bucket;
    private final ShapeService shapeService;

    @SuppressWarnings("deprecation")
    @Autowired
    public ShapeController(ShapeService shapeService, RateLimiterProperties rateLimiterProperties) {
        this.shapeService = shapeService;
        // Token Bucket algorithm using Bucket4j
        // Using classic() with intervally refill - configurable via application.properties
        // Note: classic() is deprecated in Bucket4j 7.6.0 but still functional
        // The modern API would require different approach, so suppressing deprecation warning
        Bandwidth limit = Bandwidth.classic(
            rateLimiterProperties.getCapacity(),
            Refill.intervally(
                rateLimiterProperties.getRefillAmount(),
                rateLimiterProperties.getRefillPeriodAsDuration()
            )
        );
        this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * Calculate rectangle perimeter with rate limiting.
     * 
     * @param shapeRequest the shape request
     * @return shape response with perimeter
     */
    @PostMapping(value = "/rectangle")
    public ResponseEntity<ShapeResponse> rectangle(@RequestBody ShapeRequest shapeRequest) {
        if (bucket.tryConsume(1)) {
            ShapeResponse shapeResponse = shapeService.perimeter(shapeRequest);
            return ResponseEntity.ok(shapeResponse);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
