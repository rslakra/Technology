# Sliding Window Counter Rate Limiter Service

A Spring Boot application demonstrating rate limiting using the **Sliding Window Counter Algorithm**.

## Rate Limiting Pattern

This service implements the **Sliding Window Counter Algorithm**, which divides time into smaller sub-windows and maintains counters for each sub-window. The total count is calculated by summing the current window and the previous window proportionally. This provides more accurate rate limiting than Fixed Window Counter.

### How Sliding Window Counter Works

1. **Sub-Windows**: Time is divided into smaller sub-windows (e.g., 10 sub-windows per minute)
2. **Counters**: Each sub-window has its own counter tracking requests
3. **Weighted Count**: The total count is calculated as: current sub-window count + (previous sub-window count × remaining time ratio)
4. **Request Processing**: When a request arrives, it increments the current sub-window counter if the weighted count hasn't exceeded the limit
5. **Sliding Window**: As time progresses, old sub-windows are cleared and new ones are used
6. **Rate Limiting**: If the weighted count exceeds the limit, requests are rejected with HTTP 429 (Too Many Requests)

### Current Implementation

- **Algorithm**: Sliding Window Counter (custom implementation)
- **Rate Limit**: Configurable via `application.properties` (default: 50 requests per minute)
- **Sub-Windows**: 10 sub-windows per time window for better accuracy
- **Scope**: Per-controller instance (not shared across instances)
- **Configuration**: Fully configurable via Spring Boot properties

### Algorithm Characteristics

**Pros:**
- More accurate than Fixed Window Counter
- Prevents boundary bursts (unlike Fixed Window Counter)
- Better memory efficiency than Sliding Window Log
- Provides smooth rate limiting across window boundaries

**Cons:**
- More memory usage than Fixed Window Counter (stores multiple counters)
- More computational overhead than Fixed Window Counter
- Still less accurate than Sliding Window Log

### Code Example

```java
// Rate limiter is configured via RateLimiterProperties
@Autowired
public ShapeController(ShapeService shapeService, SlidingWindowCounterRateLimiter rateLimiter) {
    this.shapeService = shapeService;
    this.rateLimiter = rateLimiter;
}

// Usage in controller
if (rateLimiter.tryConsume()) {
    // Process request
} else {
    // Return 429 Too Many Requests
}
```

## API Endpoints

### Calculate Rectangle Perimeter

**POST** `/api/v1/perimeters/rectangle`

Calculate the perimeter of a rectangle with the given dimensions. This endpoint is rate-limited (configurable via `application.properties`, default: 50 requests per minute).

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
- `shape` (String, required): Shape type. Must be `"RECTANGLE"` for this endpoint.
- `dimension` (Object, required): Dimensions of the rectangle.
  - `length` (Double, required): Length of the rectangle.
  - `width` (Double, required): Width of the rectangle.

#### Response

**Success Response (200 OK):**
```json
{
  "shape": "RECTANGLE",
  "perimeter": 30.0
}
```

**Response Fields:**
- `shape` (String): The shape type that was processed.
- `perimeter` (Double): The calculated perimeter value (rounded to 2 decimal places).

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
curl -X POST http://localhost:8080/api/v1/perimeters/rectangle \
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
http POST http://localhost:8080/api/v1/perimeters/rectangle \
  shape=RECTANGLE \
  dimension:='{"length":10.0,"width":5.0}'
```

**Example Response:**
```json
{
  "shape": "RECTANGLE",
  "perimeter": 30.0
}
```

#### Rate Limiting

- **Rate Limit**: Configurable via `application.properties` (default: 50 requests per minute)
- **Window Size**: Configurable via `application.properties` (default: 1 minute)
- **Sub-Windows**: 10 sub-windows per window (fixed for optimal accuracy)
- **Rate Limit Scope**: Per service instance (not shared across instances)
- **Rate Limit Response**: HTTP 429 (Too Many Requests) with empty body

**Note**: The rate limit is applied per controller instance. In a distributed environment with multiple instances, each instance maintains its own sliding window counters. Rate limit values can be customized via configuration properties (see [Configuration](#configuration) section).

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Rate Limiting Algorithm**: Sliding Window Counter (custom implementation)
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
rate-limiter.capacity=50          # Maximum requests per window
rate-limiter.refill-amount=50      # Number of requests allowed per period (same as capacity for sliding window)
rate-limiter.refill-period=1m      # Window size (supports: 1m, 60s, 1h, PT1M, etc.)
```

#### Property Details

- **`rate-limiter.capacity`** (Integer, default: 50)
  - Maximum number of requests allowed in a time window
  - Determines the rate limit threshold

- **`rate-limiter.refill-amount`** (Integer, default: 50)
  - Number of requests allowed per refill period
  - For Sliding Window Counter, this should typically match `capacity`
  - Combined with `refill-period`, determines the average request rate

- **`rate-limiter.refill-period`** (String, default: "1m")
  - Duration string specifying the window size
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

**Example 3: 1000 requests per hour**
```properties
rate-limiter.capacity=1000
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

### Running Tests

```bash
mvn test
```

### Test Coverage

The project includes comprehensive test cases demonstrating rate limiting functionality:

1. **ShapeControllerRateLimiterTest** (`@WebMvcTest`)
   - Tests rate limiting at the controller level using MockMvc
   - Verifies successful requests within rate limit
   - Verifies HTTP 429 response when rate limit is exceeded
   - Tests weighted count calculation

2. **RateLimiterIntegrationTest** (`@SpringBootTest`)
   - Full integration tests with complete Spring Boot context
   - Tests successful requests with valid data
   - Tests rate limiting behavior
   - Tests concurrent request handling
   - Tests that rate limit response has no body

### Test Scenarios Covered

- ✅ Requests within rate limit return HTTP 200
- ✅ Requests exceeding rate limit return HTTP 429
- ✅ Weighted count calculation across sub-windows
- ✅ Invalid requests return 400 before rate limit check
- ✅ Concurrent request handling
- ✅ Rate limit response format (no body for 429)

## Scope of Improvements

### 1. **Distributed Rate Limiting**
   - **Current**: Rate limiting is per-instance (not shared across multiple service instances)
   - **Improvement**: Implement distributed rate limiting using Redis or Hazelcast to share rate limit state across all service instances
   - **Benefit**: Consistent rate limiting in a distributed/microservices environment

### 2. **Per-User/Per-Key Rate Limiting**
   - **Current**: Single global counter for all requests
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

### 7. **Multiple Algorithm Support**
   - **Current**: Sliding Window Counter only
   - **Improvement**: Support multiple algorithms (Sliding Window Counter, Fixed Window Counter, Sliding Window Log, Token Bucket, Leaky Bucket)
   - **Benefit**: Choose the best algorithm for specific use cases

### 8. **Configurable Sub-Window Count**
   - **Current**: Fixed 10 sub-windows per window
   - **Improvement**: Make sub-window count configurable for fine-tuning accuracy vs. memory trade-off
   - **Benefit**: Flexibility to optimize based on use case

### 9. **Rate Limit Metrics and Monitoring**
   - **Current**: No metrics or monitoring
   - **Improvement**: Add metrics (requests allowed/rejected, current weighted count) and integrate with monitoring tools (Prometheus, Micrometer)
   - **Benefit**: Better observability and capacity planning

### 10. **Graceful Degradation**
   - **Current**: Binary allow/deny
   - **Improvement**: Implement queuing or priority-based request handling
   - **Benefit**: Better user experience during high load

### 11. **Rate Limit Testing** ✅ **IMPLEMENTED**
   - **Previous**: Basic context loading test only
   - **Current**: Comprehensive unit and integration tests for rate limiting scenarios
   - **Benefit**: Ensure rate limiting works correctly under various conditions

### 12. **Documentation and API Documentation**
   - **Current**: Basic documentation
   - **Improvement**: Add Swagger/OpenAPI documentation, detailed API documentation
   - **Benefit**: Better developer experience and API discoverability

### 13. **Error Handling and Retry Logic**
   - **Current**: Simple 429 response
   - **Improvement**: Add retry-after header, better error messages, client retry guidance
   - **Benefit**: Better client experience and compliance with HTTP standards

# Author
- Rohtash Lakra
