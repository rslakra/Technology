package com.rslakra.ratelimiter.fixedwindowcounter.controller;

import com.rslakra.ratelimiter.fixedwindowcounter.enums.ShapeType;
import com.rslakra.ratelimiter.fixedwindowcounter.payload.request.ShapeRequest;
import com.rslakra.ratelimiter.fixedwindowcounter.payload.response.ShapeResponse;
import com.rslakra.ratelimiter.fixedwindowcounter.persistence.repository.ShapeRepository;
import com.rslakra.ratelimiter.fixedwindowcounter.ratelimiter.FixedWindowCounterRateLimiter;
import com.rslakra.ratelimiter.fixedwindowcounter.service.ShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for shape calculations with Fixed Window Counter rate limiting.
 * Provides APIs for calculating properties of various 2D and 3D geometric shapes.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@RestController
@RequestMapping(value = "/api/v1/shapes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShapeController {

    private final FixedWindowCounterRateLimiter rateLimiter;
    private final ShapeService shapeService;
    private final ShapeRepository shapeRepository;

    @Autowired
    public ShapeController(ShapeService shapeService, 
                          FixedWindowCounterRateLimiter rateLimiter,
                          ShapeRepository shapeRepository) {
        this.shapeService = shapeService;
        this.rateLimiter = rateLimiter;
        this.shapeRepository = shapeRepository;
    }

    /**
     * Generic endpoint to calculate any shape's properties.
     * 
     * @param shapeRequest the shape request
     * @return shape response with calculated properties
     */
    @PostMapping(value = "/calculate")
    public ResponseEntity<ShapeResponse> calculate(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        
        try {
            ShapeResponse shapeResponse = shapeService.calculate(shapeRequest);
            shapeRepository.save(shapeResponse);
            return ResponseEntity.ok(shapeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== 2D Shape Endpoints ==========

    /**
     * Calculate circle properties (perimeter and area).
     */
    @PostMapping(value = "/circle")
    public ResponseEntity<ShapeResponse> circle(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.CIRCLE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate rectangle properties (perimeter and area).
     */
    @PostMapping(value = "/rectangle")
    public ResponseEntity<ShapeResponse> rectangle(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.RECTANGLE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate square properties (perimeter and area).
     */
    @PostMapping(value = "/square")
    public ResponseEntity<ShapeResponse> square(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.SQUARE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate triangle properties (perimeter and area).
     */
    @PostMapping(value = "/triangle")
    public ResponseEntity<ShapeResponse> triangle(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.TRIANGLE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate parallelogram properties (perimeter and area).
     */
    @PostMapping(value = "/parallelogram")
    public ResponseEntity<ShapeResponse> parallelogram(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.PARALLELOGRAM);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate trapezium properties (perimeter and area).
     */
    @PostMapping(value = "/trapezium")
    public ResponseEntity<ShapeResponse> trapezium(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.TRAPEZOID);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate polygon properties (perimeter and area).
     */
    @PostMapping(value = "/polygon")
    public ResponseEntity<ShapeResponse> polygon(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.POLYGON);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== 3D Shape Endpoints ==========

    /**
     * Calculate sphere properties (volume and surface area).
     */
    @PostMapping(value = "/sphere")
    public ResponseEntity<ShapeResponse> sphere(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.SPHERE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate cube properties (volume and surface area).
     */
    @PostMapping(value = "/cube")
    public ResponseEntity<ShapeResponse> cube(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.CUBE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate cylinder properties (volume and surface area).
     */
    @PostMapping(value = "/cylinder")
    public ResponseEntity<ShapeResponse> cylinder(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.CYLINDER);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate cone properties (volume and surface area).
     */
    @PostMapping(value = "/cone")
    public ResponseEntity<ShapeResponse> cone(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.CONE);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Calculate cuboid properties (volume and surface area).
     */
    @PostMapping(value = "/cuboid")
    public ResponseEntity<ShapeResponse> cuboid(@RequestBody ShapeRequest shapeRequest) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            shapeRequest.setShape(ShapeType.CUBOID);
            ShapeResponse response = shapeService.calculate(shapeRequest);
            shapeRepository.save(response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== Repository Endpoints ==========

    /**
     * Get all shape calculations.
     */
    @GetMapping
    public ResponseEntity<List<ShapeResponse>> getAllShapes() {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        return ResponseEntity.ok(shapeRepository.findAll());
    }

    /**
     * Get shape calculations by type.
     */
    @GetMapping(value = "/type/{shapeType}")
    public ResponseEntity<List<ShapeResponse>> getShapesByType(@PathVariable String shapeType) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        try {
            ShapeType type = ShapeType.valueOf(shapeType.toUpperCase());
            return ResponseEntity.ok(shapeRepository.findByShapeType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get shape calculation by ID.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ShapeResponse> getShapeById(@PathVariable String id) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        Optional<ShapeResponse> shape = shapeRepository.findById(id);
        return shape.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete shape calculation by ID.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Map<String, String>> deleteShapeById(@PathVariable String id) {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        boolean deleted = shapeRepository.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Shape calculation deleted successfully", "id", id));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete all shape calculations.
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAllShapes() {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        shapeRepository.deleteAll();
        return ResponseEntity.ok(Map.of("message", "All shape calculations deleted successfully"));
    }

    /**
     * Get count of shape calculations.
     */
    @GetMapping(value = "/count")
    public ResponseEntity<Map<String, Long>> getShapeCount() {
        if (!rateLimiter.tryConsume()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        return ResponseEntity.ok(Map.of("count", shapeRepository.count()));
    }
}
