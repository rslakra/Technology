# Distributed Rate Limiter Service

A comprehensive Spring Boot application demonstrating **all 5 major rate limiting algorithms** with a unified API for testing and comparison. This project implements Token Bucket, Fixed Window Counter, Sliding Window Counter, Sliding Window Log, and Leaky Bucket algorithms.

## Rate Limiting Algorithms

This service implements **5 different rate limiting algorithms**, each with its own characteristics and use cases:

1. **Token Bucket** - Allows bursts while maintaining average rate (using Bucket4j)
2. **Fixed Window Counter** - Simple, memory-efficient window-based counting
3. **Sliding Window Counter** - More accurate than fixed window with weighted counting
4. **Sliding Window Log** - Most accurate, maintains exact request timestamps
5. **Leaky Bucket** - Smooths traffic bursts with constant leak rate

All algorithms are accessible through dedicated test endpoints and share the same configuration properties.

### Algorithm Implementations

#### Token Bucket (Bucket4j)
- **How it works**: Bucket holds tokens that are refilled at a constant rate. Each request consumes one token.
- **Pros**: Allows bursts, smooth average rate, industry-standard
- **Cons**: Requires external library

#### Fixed Window Counter
- **How it works**: Divides time into fixed windows with counters. Counter resets at window boundary.
- **Pros**: Simple, memory efficient, low overhead
- **Cons**: Can allow bursts at window boundaries

#### Sliding Window Counter
- **How it works**: Uses sub-windows with weighted counting for better accuracy.
- **Pros**: More accurate than fixed window, prevents boundary bursts
- **Cons**: More memory than fixed window

#### Sliding Window Log
- **How it works**: Maintains a log of request timestamps within the window.
- **Pros**: Most accurate, prevents all boundary bursts
- **Cons**: Higher memory usage (stores all timestamps)

#### Leaky Bucket
- **How it works**: Maintains a bucket that leaks requests at a constant rate.
- **Pros**: Smooths traffic bursts, constant output rate
- **Cons**: Can reject requests even when average rate is acceptable

### Current Implementation

- **Algorithms**: All 5 algorithms implemented and available
- **Rate Limit**: Configurable via `application.properties` (default: 5 requests per minute)
- **Factory Pattern**: `RateLimiterFactory` provides algorithm selection
- **Test Endpoints**: Dedicated endpoints for testing each algorithm
- **Scope**: Per-instance (not shared across instances)
- **Configuration**: Fully configurable via Spring Boot properties

### Code Example

```java
// Rate limiter is configured via RateLimiterProperties
@Autowired
public ShapeController(ShapeService shapeService, RateLimiterProperties rateLimiterProperties) {
    this.shapeService = shapeService;
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

// Usage in controller
if (bucket.tryConsume(1)) {
    // Process request
} else {
    // Return 429 Too Many Requests
}
```

## API Endpoints

### Rate Limiter Test Endpoints

These endpoints allow you to test each rate limiting algorithm independently:

#### Test Fixed Window Counter
**GET** `/api/v1/rate-limiters/fixed-window-counter`

Test the Fixed Window Counter algorithm.

**Response (200 OK):**
```json
{
  "rateLimiterType": "FIXED_WINDOW_COUNTER",
  "allowed": true,
  "availableTokens": 4,
  "message": "Request allowed. Available tokens: 4"
}
```

**Response (429 Too Many Requests):**
```json
{
  "rateLimiterType": "FIXED_WINDOW_COUNTER",
  "allowed": false,
  "availableTokens": 0,
  "message": "Rate limit exceeded. Available tokens: 0"
}
```

#### Test Leaky Bucket
**GET** `/api/v1/rate-limiters/leaky-bucket`

Test the Leaky Bucket algorithm.

#### Test Sliding Window Counter
**GET** `/api/v1/rate-limiters/sliding-window-counter`

Test the Sliding Window Counter algorithm.

#### Test Sliding Window Log
**GET** `/api/v1/rate-limiters/sliding-window-log`

Test the Sliding Window Log algorithm.

#### Test Token Bucket
**GET** `/api/v1/rate-limiters/token-bucket`

Test the Token Bucket algorithm (using Bucket4j).

#### Generic Test Endpoint
**GET** `/api/v1/rate-limiters/{type}`

Test any rate limiter type dynamically. Accepts:
- `FIXED_WINDOW_COUNTER` or `fixed-window-counter`
- `LEAKY_BUCKET` or `leaky-bucket`
- `SLIDING_WINDOW_COUNTER` or `sliding-window-counter`
- `SLIDING_WINDOW_LOG` or `sliding-window-log`
- `TOKEN_BUCKET` or `token-bucket`

**Example:**
```bash
curl http://localhost:8080/api/v1/rate-limiters/FIXED_WINDOW_COUNTER
curl http://localhost:8080/api/v1/rate-limiters/token-bucket
```

### Shape Calculation Endpoint

**POST** `/api/v1/perimeters`

Calculate shape properties (perimeter, area, volume, surface area) based on the shape type provided in the request. This is a **generic endpoint** that accepts any shape type (RECTANGLE, CIRCLE, SQUARE, TRIANGLE, SPHERE, CUBE, etc.) and returns the appropriate calculated properties based on the shape type.

This endpoint is rate-limited using the Token Bucket algorithm (configurable via `application.properties`, default: 5 requests per minute).

#### Request

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "shape": "RECTANGLE",
  "dimension": {
    "length": 10.0,
    "width": 5.0
  }
}
```

**Request Fields:**
- `shape` (String, required): Shape type. Supported values: `"RECTANGLE"`, `"CIRCLE"`, `"SQUARE"`, `"TRIANGLE"`, `"SPHERE"`, `"CUBE"`, `"CYLINDER"`, `"CONE"`, `"CUBOID"`, etc.
- `dimension` (Object, required): Dimensions of the shape (varies by shape type).
  - For 2D shapes (Rectangle, Square, etc.):
    - `length` (Double): Length of the shape
    - `width` (Double): Width of the shape
    - `radius` (Double): Radius for circular shapes
    - `side` (Double): Side length for squares
  - For 3D shapes (Sphere, Cube, etc.):
    - `length`, `width`, `height` (Double): Dimensions for cuboids
    - `radius` (Double): Radius for spherical shapes
    - `side` (Double): Side length for cubes

#### Response

**Success Response (200 OK):**
```json
{
  "shape": "RECTANGLE",
  "perimeter": 30.0,
  "area": 50.0
}
```

**Response Fields:**
- `shape` (String): The shape type that was processed.
- `perimeter` (Double, optional): The calculated perimeter value for 2D shapes (rounded to 2 decimal places).
- `area` (Double, optional): The calculated area value for 2D shapes (rounded to 2 decimal places).
- `volume` (Double, optional): The calculated volume value for 3D shapes (rounded to 2 decimal places).
- `surfaceArea` (Double, optional): The calculated surface area value for 3D shapes (rounded to 2 decimal places).

**Rate Limit Exceeded (429 Too Many Requests):**
```
Status: 429 Too Many Requests
Body: (empty)
```

**Bad Request (400 Bad Request):**
```
Status: 400 Bad Request
Body: Error details
```

#### Example Requests

**Using cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/perimeters \
  -H "Content-Type: application/json" \
  -d '{
    "shape": "RECTANGLE",
    "dimension": {
      "length": 10.0,
      "width": 5.0
    }
  }'
```

**Using HTTPie:**
```bash
http POST http://localhost:8080/api/v1/perimeters \
  shape=RECTANGLE \
  dimension:='{"length":10.0,"width":5.0}'
```

**Example: Calculate Circle Perimeter**
```bash
curl -X POST http://localhost:8080/api/v1/perimeters \
  -H "Content-Type: application/json" \
  -d '{
    "shape": "CIRCLE",
    "dimension": {
      "radius": 5.0
    }
  }'
```

**Example: Calculate Cube Volume**
```bash
curl -X POST http://localhost:8080/api/v1/perimeters \
  -H "Content-Type: application/json" \
  -d '{
    "shape": "CUBE",
    "dimension": {
      "side": 5.0
    }
  }'
```

**Example Responses:**

**Rectangle Response:**
```json
{
  "shape": "RECTANGLE",
  "perimeter": 30.0,
  "area": 50.0
}
```

**Circle Response:**
```json
{
  "shape": "CIRCLE",
  "perimeter": 31.42,
  "area": 78.54
}
```

**Cube Response:**
```json
{
  "shape": "CUBE",
  "volume": 125.0,
  "surfaceArea": 150.0
}
```

#### Rate Limiting

- **Rate Limit**: Configurable via `application.properties` (default: 5 requests per minute)
- **Algorithm**: Token Bucket (for this endpoint)
- **Rate Limit Scope**: Per service instance (not shared across instances)
- **Rate Limit Response**: HTTP 429 (Too Many Requests) with empty body

**Note**: The rate limit is applied per controller instance. In a distributed environment with multiple instances, each instance maintains its own rate limit state. Rate limit values can be customized via configuration properties (see [Configuration](#configuration) section).

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Rate Limiting Library**: Bucket4j 7.6.0
- **Build Tool**: Maven

## Running the Application

```bash
./buildMaven.sh
./runMaven.sh
```

The service will start on port 8080.

## Configuration

### Rate Limiter Configuration

The rate limiter is fully configurable via `application.properties`. You can customize the rate limiting behavior without modifying code.

#### Configuration Properties

```properties
# Rate Limiter Configuration
rate-limiter.capacity=5           # Maximum capacity (tokens/requests per window)
rate-limiter.refill-amount=50     # Refill/leak rate per period
rate-limiter.refill-period=1m     # Time period (supports: 1m, 60s, 1h, PT1M, etc.)
```

**Note**: The default capacity is set to 5 for easier testing. Adjust based on your needs.

#### Property Details

- **`rate-limiter.capacity`** (Integer, default: 50)
  - Maximum number of tokens the bucket can hold
  - Determines the burst capacity (how many requests can be made in quick succession)

- **`rate-limiter.refill-amount`** (Integer, default: 50)
  - Number of tokens to refill per refill period
  - Combined with `refill-period`, determines the average request rate

- **`rate-limiter.refill-period`** (String, default: "1m")
  - Duration string specifying how often tokens are refilled
  - Supported formats:
    - Simple format: `1m` (1 minute), `60s` (60 seconds), `1h` (1 hour), `1d` (1 day)
    - ISO-8601 format: `PT1M` (1 minute), `PT60S` (60 seconds), `PT1H` (1 hour)

#### Configuration Examples

**Example 1: 100 requests per minute**
```properties
rate-limiter.capacity=100
rate-limiter.refill-amount=100
rate-limiter.refill-period=1m
```

**Example 2: 10 requests per second**
```properties
rate-limiter.capacity=10
rate-limiter.refill-amount=10
rate-limiter.refill-period=1s
```

**Example 3: 1000 requests per hour with burst capacity of 200**
```properties
rate-limiter.capacity=200
rate-limiter.refill-amount=1000
rate-limiter.refill-period=1h
```

**Example 4: Using ISO-8601 duration format**
```properties
rate-limiter.capacity=50
rate-limiter.refill-amount=50
rate-limiter.refill-period=PT1M
```

#### Environment-Specific Configuration

You can override these values for different environments:

**`application-dev.properties`** (Development)
```properties
rate-limiter.capacity=100
rate-limiter.refill-amount=100
rate-limiter.refill-period=1m
```

**`application-prod.properties`** (Production)
```properties
rate-limiter.capacity=50
rate-limiter.refill-amount=50
rate-limiter.refill-period=1m
```

## Testing

### Running Unit Tests

```bash
mvn test
```

### Testing Rate Limiters via API

You can test each rate limiter algorithm using the dedicated test endpoints:

#### Example: Testing Fixed Window Counter

```bash
# Make requests within limit (default: 5 requests per minute)
for i in {1..5}; do
  curl http://localhost:8080/api/v1/rate-limiters/fixed-window-counter
  echo ""
done

# 6th request should be rate limited (429)
curl http://localhost:8080/api/v1/rate-limiters/fixed-window-counter
```

#### Example: Testing All Algorithms

```bash
# Test Token Bucket
curl http://localhost:8080/api/v1/rate-limiters/token-bucket

# Test Leaky Bucket
curl http://localhost:8080/api/v1/rate-limiters/leaky-bucket

# Test Sliding Window Counter
curl http://localhost:8080/api/v1/rate-limiters/sliding-window-counter

# Test Sliding Window Log
curl http://localhost:8080/api/v1/rate-limiters/sliding-window-log

# Test Fixed Window Counter
curl http://localhost:8080/api/v1/rate-limiters/fixed-window-counter
```

#### Example: Comparing Algorithms

```bash
# Test all algorithms in sequence to compare behavior
for algorithm in token-bucket leaky-bucket sliding-window-counter sliding-window-log fixed-window-counter; do
  echo "Testing $algorithm:"
  curl -s http://localhost:8080/api/v1/rate-limiters/$algorithm | jq .
  echo ""
done
```

### Test Coverage

The project includes comprehensive test cases demonstrating rate limiting functionality:

1. **ShapeControllerTest** (`@WebMvcTest`)
   - Unit tests for ShapeController endpoint functionality
   - Tests successful shape calculation with valid requests
   - Tests validation (null request, missing shape type, invalid JSON)
   - Tests service exception handling
   - Tests rate limiting behavior (429 when limit exceeded)
   - Tests rate limit check before validation

2. **ShapeControllerRateLimiterTest** (`@WebMvcTest`)
   - Integration tests for rate limiting functionality in ShapeController
   - Tests rate limiting at the controller level using MockMvc
   - Verifies successful requests within rate limit (first 50 requests)
   - Verifies HTTP 429 response when rate limit is exceeded (51st request)
   - Tests token consumption behavior
   - Tests request validation before rate limiting

3. **RateLimiterControllerTest** (`@WebMvcTest`)
   - Unit tests for RateLimiterController
   - Tests all 5 rate limiter test endpoints (Fixed Window Counter, Leaky Bucket, Sliding Window Counter, Sliding Window Log, Token Bucket)
   - Tests successful requests (200 OK) and rate limited requests (429 Too Many Requests)
   - Tests generic endpoint with valid/invalid types
   - Tests error handling (factory exceptions, rate limiter exceptions)
   - Tests response format and JSON structure

4. **Bucket4jRateLimiterTest** (Unit Tests)
   - Direct testing of Bucket4j Token Bucket algorithm
   - Tests token consumption when tokens are available
   - Tests failure when bucket is empty
   - Tests burst consumption up to capacity
   - Tests token refill after refill period
   - Tests intervally refill strategy
   - Tests available tokens tracking

5. **RateLimiterIntegrationTest** (`@SpringBootTest`)
   - Full integration tests with complete Spring Boot context
   - Tests successful requests with valid data
   - Tests rate limiting behavior
   - Tests concurrent request handling
   - Tests that rate limit response has no body

### Test Scenarios Covered

**ShapeController Tests:**
- ✅ Successful shape calculation with valid request (200 OK)
- ✅ Null request handling (400 Bad Request)
- ✅ Missing/null shape type validation (400 Bad Request)
- ✅ Invalid JSON handling (400 Bad Request)
- ✅ Service exception handling (400 Bad Request)
- ✅ Rate limiting behavior (429 when limit exceeded)
- ✅ Rate limit check happens before validation

**Rate Limiter Controller Tests:**
- ✅ All 5 rate limiter endpoints return correct responses
- ✅ Successful requests return 200 OK with proper JSON structure
- ✅ Rate limited requests return 429 Too Many Requests
- ✅ Generic endpoint accepts valid/invalid types
- ✅ Error handling for factory and rate limiter exceptions
- ✅ Response format validation (rateLimiterType, allowed, availableTokens, message)

**Integration Tests:**
- ✅ Requests within rate limit return HTTP 200
- ✅ Requests exceeding rate limit return HTTP 429
- ✅ Token consumption and state management for all algorithms
- ✅ Burst consumption up to capacity
- ✅ Refill/leak behavior after time period
- ✅ Invalid requests return 400 before rate limit check
- ✅ Concurrent request handling
- ✅ Rate limit response format (JSON with status for test endpoints)
- ✅ All 5 algorithms can be tested independently

## Scope of Improvements

### 1. **Distributed Rate Limiting**
   - **Current**: Rate limiting is per-instance (not shared across multiple service instances)
   - **Improvement**: Implement distributed rate limiting using Redis or Hazelcast to share rate limit state across all service instances
   - **Benefit**: Consistent rate limiting in a distributed/microservices environment

### 2. **Per-User/Per-Key Rate Limiting**
   - **Current**: Single global bucket for all requests
   - **Improvement**: Implement per-user or per-API-key rate limiting
   - **Benefit**: Prevent individual users from exhausting the rate limit, fair resource distribution

### 3. **Configurable Rate Limits** ✅ **IMPLEMENTED**
   - **Previous**: Hard-coded rate limit (50 requests/minute)
   - **Current**: Fully configurable via `application.properties` with `RateLimiterProperties`
   - **Configuration**: `rate-limiter.capacity`, `rate-limiter.refill-amount`, `rate-limiter.refill-period`
   - **Benefit**: Flexibility to adjust limits without code changes, environment-specific configurations

### 4. **Multiple Rate Limit Tiers**
   - **Current**: Single rate limit for all endpoints
   - **Improvement**: Different rate limits for different endpoints or user tiers (e.g., free tier: 10/min, premium: 100/min)
   - **Benefit**: Support for tiered service offerings

### 5. **Rate Limit Headers**
   - **Current**: No rate limit information in response headers
   - **Improvement**: Add standard rate limit headers (X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset)
   - **Benefit**: Better client experience, clients can adjust their request patterns

### 6. **Rate Limiting as AOP/Filter**
   - **Current**: Rate limiting logic embedded in controller
   - **Improvement**: Extract to a Spring AOP aspect or Filter/Interceptor for cleaner separation of concerns
   - **Benefit**: Reusable rate limiting across multiple controllers, easier maintenance

### 7. **Multiple Algorithm Support** ✅ **IMPLEMENTED**
   - **Previous**: Token Bucket only
   - **Current**: All 5 algorithms implemented (Token Bucket, Fixed Window Counter, Sliding Window Counter, Sliding Window Log, Leaky Bucket)
   - **Benefit**: Choose the best algorithm for specific use cases, test and compare algorithms

### 8. **Rate Limit Metrics and Monitoring**
   - **Current**: No metrics or monitoring
   - **Improvement**: Add metrics (requests allowed/rejected, current rate limit usage) and integrate with monitoring tools (Prometheus, Micrometer)
   - **Benefit**: Better observability and capacity planning

### 9. **Graceful Degradation**
   - **Current**: Binary allow/deny
   - **Improvement**: Implement queuing or priority-based request handling
   - **Benefit**: Better user experience during high load

### 10. **Rate Limit Testing** ✅ **IMPLEMENTED**
   - **Previous**: Basic context loading test only
   - **Current**: Comprehensive unit and integration tests, plus dedicated test endpoints for each algorithm
   - **Test Endpoints**: `/api/v1/rate-limiters/{algorithm-name}` for testing each algorithm independently
   - **Benefit**: Easy testing and comparison of different algorithms, comprehensive test coverage

### 11. **Documentation and API Documentation**
   - **Current**: Minimal documentation
   - **Improvement**: Add Swagger/OpenAPI documentation, detailed API documentation
   - **Benefit**: Better developer experience and API discoverability

### 12. **Error Handling and Retry Logic**
   - **Current**: Simple 429 response
   - **Improvement**: Add retry-after header, better error messages, client retry guidance
   - **Benefit**: Better client experience and compliance with HTTP standards

# Author
- Rohtash Lakra
