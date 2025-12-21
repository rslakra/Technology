# swagger-service

A Spring Boot 3.5.7 application with springdoc-openapi for API documentation.

## Swagger UI

This project uses **springdoc-openapi 2.6.0** for API documentation.

To view the OpenAPI JSON specification:
```shell
http://localhost:8080/swagger-service/api-docs
```

To access the Swagger UI:
```shell
http://localhost:8080/swagger-service/swagger-ui.html
```

Or alternatively:
```shell
http://localhost:8080/swagger-service/swagger-ui/index.html
```

**Note:** The context path is `/swagger-service` as configured in `application.properties`.

## Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot 3.5.7 Reference Documentation](https://docs.spring.io/spring-boot/docs/3.5.7/reference/html/)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.5.7/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.5.7/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)
* [springdoc-openapi Documentation](https://springdoc.org/)

## Build Application

### Using Maven
```shell
./buildMaven.sh
```

Or manually:
```shell
mvn clean install -DskipTests=true
```

### Using Gradle
**Note:** Gradle build may have issues with appsuite dependencies due to POM metadata version mismatches. Maven build is recommended.

```shell
./buildGradle.sh
```

Or manually:
```shell
./gradlew clean build -x test
```

## Run Application

### Using Maven
```shell
./runMaven.sh
```

### Using Gradle
```shell
./runGradle.sh
```

## H2 Database Console

The H2 console is accessible at:
```shell
http://localhost:8080/swagger-service/h2
```

**Note:** Spring Security auto-configuration is disabled, so no login is required.

## Configuration

### Security
Spring Security auto-configuration is disabled in `application.properties`:
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
```

This allows:
- Direct access to H2 console without authentication
- No generated security password warnings

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)

# Author
- Rohtash Lakra