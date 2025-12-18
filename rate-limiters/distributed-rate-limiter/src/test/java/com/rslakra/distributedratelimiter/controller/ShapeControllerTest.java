package com.rslakra.distributedratelimiter.controller;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.distributedratelimiter.enums.ShapeType;
import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.payload.response.ShapeResponse;
import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import com.rslakra.distributedratelimiter.service.ShapeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ShapeController.
 * Tests shape calculation endpoint functionality, validation, and error handling.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@WebMvcTest(ShapeController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "rate-limiter.capacity=10",
    "rate-limiter.refill-amount=10",
    "rate-limiter.refill-period=1m"
})
class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShapeService shapeService;

    private ShapeRequest shapeRequest;
    private ShapeResponse shapeResponse;

    @BeforeEach
    void setUp() {
        // Create a valid rectangle request
        shapeRequest = new ShapeRequest();
        shapeRequest.setShape(ShapeType.RECTANGLE);
        
        Dimension dimension = new Dimension();
        dimension.setLength(10.0);
        dimension.setWidth(5.0);
        shapeRequest.setDimension(dimension);

        // Create a valid response
        shapeResponse = new ShapeResponse();
        shapeResponse.setShape(ShapeType.RECTANGLE);
        shapeResponse.setPerimeter(30.0);
        
        when(shapeService.perimeter(any(ShapeRequest.class))).thenReturn(shapeResponse);
    }

    /**
     * Test successful shape calculation with valid request.
     */
    @Test
    void testCalculateShape_WithValidRequest_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shape").value("RECTANGLE"))
                .andExpect(jsonPath("$.perimeter").value(30.0));
    }

    /**
     * Test that null request returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WithNullRequest_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that request without shape type returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WithoutShapeType_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that request with null shape type returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WithNullShapeType_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":null,\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that invalid JSON returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WithInvalidJson_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"invalid\":\"json\""))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that service exception returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WhenServiceThrowsException_ShouldReturn400() throws Exception {
        when(shapeService.perimeter(any(ShapeRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid shape dimensions"));

        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that service NullPointerException returns 400 Bad Request.
     */
    @Test
    void testCalculateShape_WhenServiceThrowsNullPointerException_ShouldReturn400() throws Exception {
        when(shapeService.perimeter(any(ShapeRequest.class)))
                .thenThrow(new NullPointerException("Null dimension"));

        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test that rate limiting is applied (429 when limit exceeded).
     */
    @Test
    void testCalculateShape_WhenRateLimitExceeded_ShouldReturn429() throws Exception {
        // Consume all tokens (capacity is 10)
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }

        // 11th request should be rate limited
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test that rate limit check happens before validation.
     */
    @Test
    void testCalculateShape_RateLimitCheckBeforeValidation() throws Exception {
        // Consume all tokens
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/v1/perimeters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                    .andExpect(status().isOk());
        }

        // Even with invalid request (no shape), should return 429 (rate limit) not 400
        mockMvc.perform(post("/api/v1/perimeters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dimension\":{\"length\":10.0,\"width\":5.0}}"))
                .andExpect(status().isTooManyRequests());
    }
}

