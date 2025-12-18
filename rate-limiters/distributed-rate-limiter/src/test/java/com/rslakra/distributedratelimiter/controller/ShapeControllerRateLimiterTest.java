package com.rslakra.distributedratelimiter.controller;

import com.rslakra.distributedratelimiter.enums.ShapeType;
import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import com.rslakra.distributedratelimiter.service.ShapeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.rslakra.distributedratelimiter.config.RateLimiterProperties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for rate limiting functionality in ShapeController.
 * 
 * @author Rohtash Lakra
 * @created 12/14/25
 */
@WebMvcTest(ShapeController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "rate-limiter.capacity=50",
    "rate-limiter.refill-amount=50",
    "rate-limiter.refill-period=1m"
})
class ShapeControllerRateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShapeService shapeService;

    private ShapeRequest validRequest;

    @BeforeEach
    void setUp() {
        // Create a valid shape request
        validRequest = new ShapeRequest();
        validRequest.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(10.0);
        dimension.setWidth(5.0);
        validRequest.setDimension(dimension);

        // Mock the service response
        com.rslakra.distributedratelimiter.payload.response.ShapeResponse mockResponse = 
            new com.rslakra.distributedratelimiter.payload.response.ShapeResponse();
        mockResponse.setShape(ShapeType.RECTANGLE);
        mockResponse.setPerimeter(30.0);
        
        when(shapeService.perimeter(any(ShapeRequest.class))).thenReturn(mockResponse);
    }

    /**
     * Test that requests within the rate limit are successful (HTTP 200).
     * Rate limit is 50 requests per minute, so first 50 requests should succeed.
     */
    @Test
    void testRequestsWithinRateLimit_ShouldSucceed() throws Exception {
        // Make 50 requests (the rate limit)
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }
    }

    /**
     * Test that requests exceeding the rate limit return HTTP 429 (Too Many Requests).
     * After 50 successful requests, the 51st request should be rate limited.
     */
    @Test
    void testRequestsExceedingRateLimit_ShouldReturn429() throws Exception {
        // First, consume all 50 tokens
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }

        // The 51st request should be rate limited
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test that rate limiting is applied per controller instance.
     * Each request consumes 1 token from the bucket.
     */
    @Test
    void testRateLimitTokenConsumption() throws Exception {
        // Make 25 requests - should all succeed
        for (int i = 0; i < 25; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }

        // Make 25 more requests - should still succeed (total 50)
        for (int i = 0; i < 25; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }

        // 51st request should fail
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test that invalid JSON requests are handled before rate limiting.
     */
    @Test
    void testInvalidRequest_ShouldReturn400_BeforeRateLimit() throws Exception {
        // Invalid JSON should return 400 Bad Request, not 429
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"invalid\":\"json\""))
                .andExpect(status().isBadRequest());
    }
}

