package com.rslakra.distributedratelimiter.controller;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.payload.response.ShapeResponse;
import com.rslakra.distributedratelimiter.service.ShapeService;
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

@RestController
@RequestMapping(value = "/api/v1/perimeters", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShapeController {

    private final Bucket bucket;
    private final ShapeService shapeService;

    @SuppressWarnings("deprecation")
    @Autowired
    public ShapeController(ShapeService shapeService, RateLimiterProperties rateLimiterProperties) {
        this.shapeService = shapeService;
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
     * Calculate shape properties (perimeter, area, volume, surface area) based on shape type.
     * 
     * @param shapeRequest the shape request containing shape type and dimensions
     * @return response with calculated shape properties
     */
    @PostMapping
    public ResponseEntity<ShapeResponse> calculateShape(@RequestBody ShapeRequest shapeRequest) {
        // Check rate limit first
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        
        // Validate request before processing
        try {
            if (shapeRequest == null || shapeRequest.getShape() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            ShapeResponse shapeResponse = shapeService.perimeter(shapeRequest);
            return ResponseEntity.ok(shapeResponse);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
