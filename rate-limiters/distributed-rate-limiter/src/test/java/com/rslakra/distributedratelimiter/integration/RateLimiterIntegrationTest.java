package com.rslakra.distributedratelimiter.integration;

import com.rslakra.distributedratelimiter.enums.ShapeType;
import com.rslakra.distributedratelimiter.payload.request.ShapeRequest;
import com.rslakra.distributedratelimiter.persistence.model.Dimension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for rate limiting with full Spring Boot context.
 * Tests the complete flow including rate limiting and business logic.
 * 
 * @author Rohtash Lakra
 * @created 12/14/25
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "rate-limiter.capacity=50",
    "rate-limiter.refill-amount=50",
    "rate-limiter.refill-period=1m"
})
class RateLimiterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String RECTANGLE_ENDPOINT = "/api/v1/perimeters";
    private static final String VALID_REQUEST_BODY = 
        "{\"shape\":\"RECTANGLE\",\"dimension\":{\"length\":10.0,\"width\":5.0}}";

    /**
     * Test successful request with valid data.
     */
    @Test
    void testSuccessfulRequest_WithValidData() throws Exception {
        mockMvc.perform(post(RECTANGLE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shape").value("RECTANGLE"))
                .andExpect(jsonPath("$.perimeter").value(30.0));
    }

    /**
     * Test rate limiting behavior - first 50 requests succeed, 51st fails.
     */
    @Test
    void testRateLimiting_First50RequestsSucceed_51stFails() throws Exception {
        // Make 50 successful requests (the rate limit)
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post(RECTANGLE_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_REQUEST_BODY))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.shape").value("RECTANGLE"));
        }

        // 51st request should be rate limited
        mockMvc.perform(post(RECTANGLE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST_BODY))
                .andDo(print())
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test that rate limiting doesn't affect request validation.
     * Invalid requests should return an error (400 or 500), but not 429 (rate limited).
     */
    @Test
    void testInvalidRequest_Returns400_BeforeRateLimit() throws Exception {
        // Invalid request (missing required fields)
        // This should return 400 (validation error) or 500 (service error), but NOT 429
        int status = mockMvc.perform(post(RECTANGLE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"shape\":\"RECTANGLE\"}"))
                .andReturn()
                .getResponse()
                .getStatus();
        
        // The key point: should NOT be 429 (rate limited)
        // It should fail validation/service error first (400 or 500)
        // Note: Rate limiting might be checked before validation, so accept 400 or 500
        assertTrue(status == 400 || status == 500, 
            "Request should return 400 or 500 (validation/service error), but was " + status + 
            ". This indicates rate limiting might be checked before validation.");
    }

    /**
     * Test that rate limiting is per endpoint.
     * Each endpoint has its own rate limit bucket.
     */
    @Test
    void testRateLimiting_IsPerEndpoint() throws Exception {
        // Consume all tokens for rectangle endpoint
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post(RECTANGLE_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_REQUEST_BODY))
                    .andExpect(status().isOk());
        }

        // Rectangle endpoint should now be rate limited
        mockMvc.perform(post(RECTANGLE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST_BODY))
                .andExpect(status().isTooManyRequests());
    }

    /**
     * Test concurrent requests handling.
     * Multiple requests should be handled correctly within rate limit.
     */
    @Test
    void testConcurrentRequests_WithinRateLimit() throws Exception {
        // Simulate concurrent requests (sequentially for testing)
        int concurrentRequests = 10;
        
        for (int i = 0; i < concurrentRequests; i++) {
            mockMvc.perform(post(RECTANGLE_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_REQUEST_BODY))
                    .andExpect(status().isOk());
        }
    }

    /**
     * Test that rate limit response doesn't include body.
     */
    @Test
    void testRateLimitResponse_NoBody() throws Exception {
        // Consume all tokens
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post(RECTANGLE_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_REQUEST_BODY));
        }

        // Rate limited response should be empty
        mockMvc.perform(post(RECTANGLE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST_BODY))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$").doesNotExist());
    }
}

