# swagger-services

A collection of Spring Boot 3.5.7 applications demonstrating Swagger/OpenAPI integration using springdoc-openapi.

## Projects

### swagger-sample
A minimal Spring Boot application demonstrating basic Swagger/OpenAPI integration.

**Features:**
- Spring Boot 3.5.7
- springdoc-openapi 2.6.0
- Java 21
- Simple REST API with Swagger documentation

### swagger-service
A full-featured Spring Boot application with JPA, Thymeleaf, and comprehensive Swagger/OpenAPI integration.

**Features:**
- Spring Boot 3.5.7
- springdoc-openapi 2.6.0
- Spring Data JPA with Jakarta Persistence
- Thymeleaf templates
- H2 and MySQL database support
- User and File management APIs
- Java 21

## Technology Stack

- **Spring Boot:** 3.5.7
- **Java:** 21
- **Swagger/OpenAPI:** springdoc-openapi 2.6.0
- **Build Tools:** Maven and Gradle
- **Database:** H2 (embedded) and MySQL

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (for Maven builds)
- Gradle 8.13+ (for Gradle builds)

### Building Projects

#### swagger-sample
```shell
cd swagger-sample
./buildMaven.sh    # Using Maven
# or
./buildGradle.sh   # Using Gradle
```

#### swagger-service
```shell
cd swagger-service
./buildMaven.sh    # Using Maven (Recommended)
# or
./buildGradle.sh   # Using Gradle (may have dependency issues)
```

## Access Points

### swagger-sample
- **Application:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

### swagger-service
- **Application:** http://localhost:8080/swagger-service
- **Swagger UI:** http://localhost:8080/swagger-service/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/swagger-service/api-docs
- **H2 Console:** http://localhost:8080/swagger-service/h2

## Key Upgrades

Both projects have been upgraded to:
- ✅ Spring Boot 3.5.7
- ✅ springdoc-openapi 2.6.0 (migrated from springfox-swagger)
- ✅ Jakarta Persistence (migrated from javax.persistence)
- ✅ Java 21
- ✅ Latest dependency versions

## Build Tools

Both projects support:
- **Maven:** Fully functional with `buildMaven.sh`
- **Gradle:** Configured with `buildGradle.sh`
  - ⚠️ Note: swagger-service Gradle build may have issues with appsuite dependencies due to POM metadata version mismatches

## Project Structure

```
swagger-services/
├── swagger-sample/          # Minimal Swagger example
│   ├── src/
│   ├── pom.xml
│   ├── build.gradle
│   ├── buildMaven.sh
│   ├── buildGradle.sh
│   └── README.md
├── swagger-service/         # Full-featured service
│   ├── src/
│   ├── pom.xml
│   ├── build.gradle
│   ├── buildMaven.sh
│   ├── buildGradle.sh
│   └── README.md
└── README.md               # This file
```

## References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.5.7/reference/html/)
- [springdoc-openapi Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)

## Author

- Rohtash Lakra

