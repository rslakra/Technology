# Fixed Window Counter Rate Limiter Service

A Spring Boot application demonstrating rate limiting using the **Fixed Window Counter Algorithm**.

## Rate Limiting Pattern

This service implements the **Fixed Window Counter Algorithm**, which divides time into fixed-size windows and maintains a counter for each window. When a request arrives, it checks if the current window's counter has exceeded the limit. At the start of a new window, the counter resets.

### How Fixed Window Counter Works

1. **Time Windows**: Time is divided into fixed-size windows (e.g., 1 minute windows)
2. **Counter**: Each window has a counter that tracks the number of requests
3. **Request Processing**: When a request arrives, the counter is incremented if it hasn't exceeded the limit
4. **Window Reset**: At the start of a new window, the counter resets to zero
5. **Rate Limiting**: If the counter exceeds the limit, requests are rejected with HTTP 429 (Too Many Requests)

### Current Implementation

- **Algorithm**: Fixed Window Counter (custom implementation)
- **Rate Limit**: Configurable via `application.properties` (default: 50 requests per minute)
- **Window Size**: Configurable via `application.properties` (default: 1 minute)
- **Scope**: Per-controller instance (not shared across instances)
- **Configuration**: Fully configurable via Spring Boot properties

### Algorithm Characteristics

**Pros:**
- Simple to implement and understand
- Memory efficient (only stores current window counter)
- Low computational overhead

**Cons:**
- Can allow bursts at window boundaries (e.g., 50 requests at end of window 1 and 50 requests at start of window 2 = 100 requests in 2 seconds)
- Less accurate than sliding window algorithms

### Code Example

```java
// Rate limiter is configured via RateLimiterProperties
@Autowired
public ShapeController(ShapeService shapeService, 
                      FixedWindowCounterRateLimiter rateLimiter,
                      ShapeRepository shapeRepository) {
    this.shapeService = shapeService;
    this.rateLimiter = rateLimiter;
    this.shapeRepository = shapeRepository;
}

// Usage in controller
if (rateLimiter.tryConsume()) {
    // Process request
} else {
    // Return 429 Too Many Requests
}
```

## API Endpoints

### Calculate Shape Properties

**POST** `/api/v1/shapes/calculate`

Generic endpoint to calculate any shape's properties (perimeter, area, volume, surface area).

**POST** `/api/v1/shapes/{shapeType}`

Specific endpoints for each shape type:
- `/api/v1/shapes/circle` - Circle (perimeter, area)
- `/api/v1/shapes/rectangle` - Rectangle (perimeter, area)
- `/api/v1/shapes/square` - Square (perimeter, area)
- `/api/v1/shapes/triangle` - Triangle (perimeter, area)
- `/api/v1/shapes/parallelogram` - Parallelogram (perimeter, area)
- `/api/v1/shapes/trapezium` - Trapezium (perimeter, area)
- `/api/v1/shapes/polygon` - Polygon (perimeter, area)
- `/api/v1/shapes/sphere` - Sphere (volume, surface area)
- `/api/v1/shapes/cube` - Cube (volume, surface area)
- `/api/v1/shapes/cylinder` - Cylinder (volume, surface area)
- `/api/v1/shapes/cone` - Cone (volume, surface area)
- `/api/v1/shapes/cuboid` - Cuboid (volume, surface area)

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
- `shape` (String, required): Shape type (e.g., "RECTANGLE", "CIRCLE", "SPHERE")
- `dimension` (Object, required): Dimensions of the shape (varies by shape type)

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
- `shape` (String): The shape type that was processed
- `perimeter` (Double, optional): The calculated perimeter value (for 2D shapes)
- `area` (Double, optional): The calculated area value (for 2D shapes)
- `volume` (Double, optional): The calculated volume value (for 3D shapes)
- `surfaceArea` (Double, optional): The calculated surface area value (for 3D shapes)

**Rate Limit Exceeded (429 Too Many Requests):**
```
Status: 429 Too Many Requests
Body: (empty)
```

**Bad Request (400 Bad Request):**
```
Status: 400 Bad Request
Body: (empty)
```

#### Example Requests

**Using cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/shapes/rectangle \
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
http POST http://localhost:8080/api/v1/shapes/rectangle \
  shape=RECTANGLE \
  dimension:='{"length":10.0,"width":5.0}'
```

**Example Response:**
```json
{
  "shape": "RECTANGLE",
  "perimeter": 30.0,
  "area": 50.0
}
```

#### Rate Limiting

- **Rate Limit**: Configurable via `application.properties` (default: 50 requests per minute)
- **Window Size**: Configurable via `application.properties` (default: 1 minute)
- **Rate Limit Scope**: Per service instance (not shared across instances)
- **Rate Limit Response**: HTTP 429 (Too Many Requests) with empty body

**Note**: The rate limit is applied per controller instance. In a distributed environment with multiple instances, each instance maintains its own rate limit counter. Rate limit values can be customized via configuration properties (see [Configuration](#configuration) section).

### Repository Endpoints

**GET** `/api/v1/shapes` - Get all shape calculations

**GET** `/api/v1/shapes/type/{shapeType}` - Get shapes by type

**GET** `/api/v1/shapes/{id}` - Get shape by ID

**GET** `/api/v1/shapes/count` - Get count of shape calculations

**DELETE** `/api/v1/shapes/{id}` - Delete shape by ID

**DELETE** `/api/v1/shapes` - Delete all shapes

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Rate Limiting Algorithm**: Fixed Window Counter (custom implementation)
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
rate-limiter.refill-amount=50      # Number of requests allowed per period (same as capacity for fixed window)
rate-limiter.refill-period=1m      # Window size (supports: 1m, 60s, 1h, PT1M, etc.)
```

#### Property Details

- **`rate-limiter.capacity`** (Integer, default: 50)
  - Maximum number of requests allowed in a time window
  - Determines the burst capacity (how many requests can be made in a window)

- **`rate-limiter.refill-amount`** (Integer, default: 50)
  - Number of requests allowed per refill period
  - For Fixed Window Counter, this should typically match `capacity`
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
   - Verifies successful requests within rate limit (first 50 requests)
   - Verifies HTTP 429 response when rate limit is exceeded (51st request)
   - Tests token consumption behavior
   - Tests request validation before rate limiting

2. **RateLimiterIntegrationTest** (`@SpringBootTest`)
   - Full integration tests with complete Spring Boot context
   - Tests successful requests with valid data
   - Tests rate limiting behavior (50 requests succeed, 51st fails)
   - Tests concurrent request handling
   - Tests that rate limit response has no body

### Test Scenarios Covered

- ✅ Requests within rate limit (first 50) return HTTP 200
- ✅ Requests exceeding rate limit (51st+) return HTTP 429
- ✅ Window counter resets at window boundary
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

### 7. **Sliding Window Algorithms**
   - **Current**: Fixed Window Counter only
   - **Improvement**: Support multiple algorithms (Fixed Window Counter, Sliding Window Counter, Sliding Window Log, Token Bucket, Leaky Bucket)
   - **Benefit**: Choose the best algorithm for specific use cases

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
   - **Current**: Comprehensive unit and integration tests for rate limiting scenarios
   - **Benefit**: Ensure rate limiting works correctly under various conditions

### 11. **Documentation and API Documentation**
   - **Current**: Basic documentation
   - **Improvement**: Add Swagger/OpenAPI documentation, detailed API documentation
   - **Benefit**: Better developer experience and API discoverability

### 12. **Error Handling and Retry Logic**
   - **Current**: Simple 429 response
   - **Improvement**: Add retry-after header, better error messages, client retry guidance
   - **Benefit**: Better client experience and compliance with HTTP standards

# Author
- Rohtash Lakra
