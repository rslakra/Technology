# Rate Limiter Implementations

This directory contains multiple Spring Boot applications demonstrating different rate limiting algorithms. Each project implements a specific rate limiting algorithm and provides a complete working example with tests.

## Projects Overview

### 1. **distributed-rate-limiter** ⭐
A comprehensive Spring Boot application that implements **all 5 rate limiting algorithms** with a unified API for testing each algorithm independently.

**Features:**
- Implements all 5 rate limiting algorithms (Token Bucket, Fixed Window Counter, Sliding Window Counter, Sliding Window Log, Leaky Bucket)
- Factory pattern for algorithm selection
- Dedicated test endpoints for each algorithm
- Configurable rate limits via `application.properties`
- Comprehensive test coverage

**Key Endpoints:**
- `/api/v1/rate-limiters/fixed-window-counter` - Test Fixed Window Counter
- `/api/v1/rate-limiters/leaky-bucket` - Test Leaky Bucket
- `/api/v1/rate-limiters/sliding-window-counter` - Test Sliding Window Counter
- `/api/v1/rate-limiters/sliding-window-log` - Test Sliding Window Log
- `/api/v1/rate-limiters/token-bucket` - Test Token Bucket
- `/api/v1/perimeters/rectangle` - Shape calculation endpoint (uses Token Bucket)

### 2. **token-bucket-ratelimiter**
Implements the **Token Bucket Algorithm** using Bucket4j library.

**Algorithm Characteristics:**
- ✅ Allows bursts of traffic up to bucket capacity
- ✅ Smooth average rate limiting over time
- ✅ Industry-standard algorithm
- ⚠️ Requires external library (Bucket4j)

### 3. **fixed-window-counter-ratelimiter**
Implements the **Fixed Window Counter Algorithm** (custom implementation).

**Algorithm Characteristics:**
- ✅ Simple to implement and understand
- ✅ Memory efficient
- ✅ Low computational overhead
- ⚠️ Can allow bursts at window boundaries
- ⚠️ Less accurate than sliding window algorithms

### 4. **sliding-window-counter-ratelimiter**
Implements the **Sliding Window Counter Algorithm** (custom implementation).

**Algorithm Characteristics:**
- ✅ More accurate than Fixed Window Counter
- ✅ Prevents boundary bursts
- ✅ Better memory efficiency than Sliding Window Log
- ⚠️ More memory usage than Fixed Window Counter
- ⚠️ Still less accurate than Sliding Window Log

### 5. **sliding-window-log-ratelimiter**
Implements the **Sliding Window Log Algorithm** (custom implementation).

**Algorithm Characteristics:**
- ✅ Most accurate rate limiting algorithm
- ✅ Prevents boundary bursts completely
- ✅ Exact request count within any time window
- ⚠️ Higher memory usage (stores all timestamps)
- ⚠️ More computational overhead

### 6. **leaky-bucket-ratelimiter**
Implements the **Leaky Bucket Algorithm** (custom implementation).

**Algorithm Characteristics:**
- ✅ Smooths out traffic bursts
- ✅ Provides constant output rate
- ✅ Prevents sudden spikes in request processing
- ⚠️ Can reject requests even when average rate is acceptable
- ⚠️ Less flexible than token bucket

## Algorithm Comparison

| Algorithm | Accuracy | Memory | Complexity | Burst Allowance | Best For |
|-----------|----------|--------|------------|----------------|----------|
| **Fixed Window Counter** | Low | Very Low | Low | High at boundaries | Simple use cases |
| **Sliding Window Counter** | Medium | Low | Medium | Moderate | Balanced accuracy/efficiency |
| **Sliding Window Log** | High | High | Medium | None | Maximum accuracy |
| **Token Bucket** | Medium-High | Low | Low | Yes (up to capacity) | General purpose |
| **Leaky Bucket** | Medium | Low | Medium | No | Smooth traffic flow |

## Quick Start

### Running the Distributed Rate Limiter (All Algorithms)

```bash
cd distributed-rate-limiter
./buildMaven.sh
./runMaven.sh
```

### Testing Individual Algorithms

```bash
# Test Fixed Window Counter
curl http://localhost:8080/api/v1/rate-limiters/fixed-window-counter

# Test Leaky Bucket
curl http://localhost:8080/api/v1/rate-limiters/leaky-bucket

# Test Sliding Window Counter
curl http://localhost:8080/api/v1/rate-limiters/sliding-window-counter

# Test Sliding Window Log
curl http://localhost:8080/api/v1/rate-limiters/sliding-window-log

# Test Token Bucket
curl http://localhost:8080/api/v1/rate-limiters/token-bucket
```

### Running Individual Rate Limiter Projects

Each project can be run independently:

```bash
cd <project-name>
./buildMaven.sh
./runMaven.sh
```

## Configuration

All projects use the same configuration pattern via `application.properties`:

```properties
# Rate Limiter Configuration
rate-limiter.capacity=50          # Maximum requests/tokens
rate-limiter.refill-amount=50      # Refill/leak rate per period
rate-limiter.refill-period=1m      # Time period (1m, 60s, 1h, PT1M, etc.)
```

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Build Tool**: Maven
- **Rate Limiting Libraries**: 
  - Bucket4j 7.6.0 (for Token Bucket)
  - Custom implementations (for other algorithms)

## Testing

Each project includes comprehensive test coverage:

```bash
mvn test
```

Test coverage includes:
- Unit tests for rate limiter implementations
- Integration tests with Spring Boot context
- Controller-level tests with MockMvc
- Concurrent request handling tests

## References

- [Rate Limiting Fundamentals](https://blog.bytebytego.com/p/rate-limiting-fundamentals)
- [Design A Rate Limiter](https://bytebytego.com/courses/system-design-interview/design-a-rate-limiter)
- [Design a Rate Limiter](https://www.hellointerview.com/learn/system-design/problem-breakdowns/distributed-rate-limiter)

## Author
- Rohtash Lakra
