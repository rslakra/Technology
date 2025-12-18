package com.rslakra.distributedratelimiter.controller;

import com.rslakra.distributedratelimiter.config.RateLimiterProperties;
import com.rslakra.distributedratelimiter.payload.response.RateLimiterTestResponse;
import com.rslakra.ratelimiters.RateLimiter;
import com.rslakra.ratelimiters.RateLimiterFactory;
import com.rslakra.ratelimiters.RateLimiterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for RateLimiterController.
 * Tests all rate limiter test endpoints, error handling, and response formats.
 * 
 * @author Rohtash Lakra
 * @created 12/17/25
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "rate-limiter.capacity=5",
    "rate-limiter.refill-amount=5",
    "rate-limiter.refill-period=1m"
})
class RateLimiterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RateLimiterFactory rateLimiterFactory;

    // Create a mock RateLimiter manually since there are multiple RateLimiter beans
    // We don't use @MockitoBean because there are 5 RateLimiter implementations
    private final RateLimiter rateLimiter = org.mockito.Mockito.mock(RateLimiter.class);

    @BeforeEach
    void setUp() {
        // Reset mock before each test
        org.mockito.Mockito.reset(rateLimiter);
        when(rateLimiterFactory.getRateLimiter(any(RateLimiterType.class))).thenReturn(rateLimiter);
    }

    /**
     * Test Fixed Window Counter endpoint returns 200 when request is allowed.
     */
    @Test
    void testFixedWindowCounter_WhenAllowed_ShouldReturn200() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(4);

        mockMvc.perform(get("/api/v1/rate-limiters/fixed-window-counter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rateLimiterType").value("FIXED_WINDOW_COUNTER"))
                .andExpect(jsonPath("$.allowed").value(true))
                .andExpect(jsonPath("$.availableTokens").value(4))
                .andExpect(jsonPath("$.message").value("Request allowed. Available tokens: 4"));
    }

    /**
     * Test Fixed Window Counter endpoint returns 429 when rate limit exceeded.
     */
    @Test
    void testFixedWindowCounter_WhenRateLimited_ShouldReturn429() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(false);
        when(rateLimiter.getAvailableTokens()).thenReturn(0);

        mockMvc.perform(get("/api/v1/rate-limiters/fixed-window-counter"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rateLimiterType").value("FIXED_WINDOW_COUNTER"))
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.availableTokens").value(0))
                .andExpect(jsonPath("$.message").value("Rate limit exceeded. Available tokens: 0"));
    }

    /**
     * Test Leaky Bucket endpoint.
     */
    @Test
    void testLeakyBucket_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(3);

        mockMvc.perform(get("/api/v1/rate-limiters/leaky-bucket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("LEAKY_BUCKET"))
                .andExpect(jsonPath("$.allowed").value(true));
    }

    /**
     * Test Sliding Window Counter endpoint.
     */
    @Test
    void testSlidingWindowCounter_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(2);

        mockMvc.perform(get("/api/v1/rate-limiters/sliding-window-counter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("SLIDING_WINDOW_COUNTER"))
                .andExpect(jsonPath("$.allowed").value(true));
    }

    /**
     * Test Sliding Window Log endpoint.
     */
    @Test
    void testSlidingWindowLog_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(1);

        mockMvc.perform(get("/api/v1/rate-limiters/sliding-window-log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("SLIDING_WINDOW_LOG"))
                .andExpect(jsonPath("$.allowed").value(true));
    }

    /**
     * Test Token Bucket endpoint.
     */
    @Test
    void testTokenBucket_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(5);

        mockMvc.perform(get("/api/v1/rate-limiters/token-bucket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("TOKEN_BUCKET"))
                .andExpect(jsonPath("$.allowed").value(true));
    }

    /**
     * Test generic endpoint with valid type (uppercase with underscores).
     */
    @Test
    void testRateLimiterByType_WithValidTypeUppercase_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(4);

        mockMvc.perform(get("/api/v1/rate-limiters/FIXED_WINDOW_COUNTER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("FIXED_WINDOW_COUNTER"));
    }

    /**
     * Test generic endpoint with valid type (lowercase with hyphens).
     */
    @Test
    void testRateLimiterByType_WithValidTypeLowercase_ShouldWork() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(3);

        mockMvc.perform(get("/api/v1/rate-limiters/token-bucket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("TOKEN_BUCKET"));
    }

    /**
     * Test generic endpoint with invalid type returns 400.
     */
    @Test
    void testRateLimiterByType_WithInvalidType_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/rate-limiters/INVALID_TYPE"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rateLimiterType").doesNotExist())
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.availableTokens").value(0))
                .andExpect(jsonPath("$.message").value("Invalid rate limiter type: INVALID_TYPE"));
    }

    /**
     * Test that exception in rate limiter factory returns 500.
     */
    @Test
    void testRateLimiter_WhenFactoryThrowsException_ShouldReturn500() throws Exception {
        when(rateLimiterFactory.getRateLimiter(any(RateLimiterType.class)))
                .thenThrow(new RuntimeException("Factory error"));

        mockMvc.perform(get("/api/v1/rate-limiters/token-bucket"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rateLimiterType").value("TOKEN_BUCKET"))
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.message").value("Error testing rate limiter: Factory error"));
    }

    /**
     * Test that exception in rate limiter tryConsume returns 500.
     */
    @Test
    void testRateLimiter_WhenTryConsumeThrowsException_ShouldReturn500() throws Exception {
        when(rateLimiter.tryConsume()).thenThrow(new RuntimeException("Rate limiter error"));

        mockMvc.perform(get("/api/v1/rate-limiters/fixed-window-counter"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.rateLimiterType").value("FIXED_WINDOW_COUNTER"))
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.message").value("Error testing rate limiter: Rate limiter error"));
    }

    /**
     * Test that exception in getAvailableTokens returns 500.
     */
    @Test
    void testRateLimiter_WhenGetAvailableTokensThrowsException_ShouldReturn500() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenThrow(new RuntimeException("Tokens error"));

        mockMvc.perform(get("/api/v1/rate-limiters/leaky-bucket"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.rateLimiterType").value("LEAKY_BUCKET"))
                .andExpect(jsonPath("$.allowed").value(false))
                .andExpect(jsonPath("$.message").value("Error testing rate limiter: Tokens error"));
    }

    /**
     * Test all specific endpoints are accessible.
     */
    @Test
    void testAllSpecificEndpoints_ShouldBeAccessible() throws Exception {
        when(rateLimiter.tryConsume()).thenReturn(true);
        when(rateLimiter.getAvailableTokens()).thenReturn(5);

        // Test all specific endpoints
        mockMvc.perform(get("/api/v1/rate-limiters/fixed-window-counter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("FIXED_WINDOW_COUNTER"));

        mockMvc.perform(get("/api/v1/rate-limiters/leaky-bucket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("LEAKY_BUCKET"));

        mockMvc.perform(get("/api/v1/rate-limiters/sliding-window-counter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("SLIDING_WINDOW_COUNTER"));

        mockMvc.perform(get("/api/v1/rate-limiters/sliding-window-log"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("SLIDING_WINDOW_LOG"));

        mockMvc.perform(get("/api/v1/rate-limiters/token-bucket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateLimiterType").value("TOKEN_BUCKET"));
    }
}

