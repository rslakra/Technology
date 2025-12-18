package com.rslakra.ratelimiter.leakybucket.controller;

import com.rslakra.ratelimiter.leakybucket.payload.request.ShapeRequest;
import com.rslakra.ratelimiter.leakybucket.payload.response.ShapeResponse;
import com.rslakra.ratelimiter.leakybucket.ratelimiter.LeakyBucketRateLimiter;
import com.rslakra.ratelimiter.leakybucket.service.ShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for shape perimeter calculations with Leaky Bucket rate limiting.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@RestController
@RequestMapping(value = "/api/v1/perimeters", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShapeController {

    private final LeakyBucketRateLimiter rateLimiter;
    private final ShapeService shapeService;

    @Autowired
    public ShapeController(ShapeService shapeService, LeakyBucketRateLimiter rateLimiter) {
        this.shapeService = shapeService;
        this.rateLimiter = rateLimiter;
    }

    /**
     * Calculate rectangle perimeter with rate limiting.
     * 
     * @param shapeRequest the shape request
     * @return shape response with perimeter
     */
    @PostMapping(value = "/rectangle")
    public ResponseEntity<ShapeResponse> rectangle(@RequestBody ShapeRequest shapeRequest) {
        if (rateLimiter.tryConsume()) {
            ShapeResponse shapeResponse = shapeService.perimeter(shapeRequest);
            return ResponseEntity.ok(shapeResponse);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
