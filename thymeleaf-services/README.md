# Thymeleaf Services

A collection of Thymeleaf-based web applications demonstrating different integration patterns and use cases.

## Projects

This repository contains multiple Thymeleaf projects:

- **thymeleaf-tomcat** - WAR-based application using raw Thymeleaf with custom filter-based architecture
- **thymeleaf-templates** - Spring Boot JAR application with Thymeleaf templates
- **thymeleaf-fragments** - Spring Boot application demonstrating Thymeleaf fragments
- **thymeleaf-layout** - Spring Boot application demonstrating Thymeleaf layout dialect
- **thymeleaf-layouts** - Alternative layout implementation
- **thymeleaf-sidebar** - Spring Boot application with sidebar navigation
- **thymeleaf-sidebars** - Enhanced sidebar implementation

## Quick Start

### Building All Projects

```bash
# Build all projects
./buildGradle.sh  # For Gradle projects
# Or build individual projects using their build scripts
```

### Running Projects

Each project has its own run script:
- `thymeleaf-tomcat/runMaven.sh` - Runs the WAR application with Cargo embedded Tomcat
- `thymeleaf-templates/runMaven.sh` - Runs the Spring Boot application

## Architecture

### thymeleaf-tomcat

A WAR-based application that demonstrates:
- **Custom Filter Architecture**: Uses `ThymeleafFilter` to intercept requests and route to controllers
- **Abstract Controller Pattern**: All controllers extend `AbstractThymeleafController` for common functionality
- **Dependency Injection**: Controllers use constructor injection for `IServletWebApplication`, `ServletContext`, and `ITemplateEngine`
- **Raw Thymeleaf**: Uses Thymeleaf directly without Spring Boot's integration layer

**Key Components:**
- `ThymeleafFilter` - Servlet filter that routes requests to appropriate controllers
- `ThymeleafApplication` - Application initialization and controller mapping
- `AbstractThymeleafController` - Base class providing common template processing logic
- `ThymeleafController` - Interface defining the controller contract

**Package Structure:**
```
com.rslakra.thymeleaf.web
├── application/
│   └── ThymeleafApplication.java
├── controller/
│   ├── thymeleaf/
│   │   ├── ThymeleafController.java (interface)
│   │   └── AbstractThymeleafController.java (abstract base class)
│   ├── HomeController.java
│   ├── ProductListController.java
│   ├── ProductCommentsController.java
│   ├── OrderListController.java
│   ├── OrderDetailsController.java
│   ├── SubscribeController.java
│   └── UserProfileController.java
└── filter/
    └── ThymeleafFilter.java
```

### thymeleaf-templates

A Spring Boot application demonstrating:
- Spring Boot integration with Thymeleaf
- Multiple layout versions (v0, v1, v2, v3)
- Sidebar navigation with dark mode
- Profile dropdown functionality
- Responsive design

## Dependencies

### Common Dependencies
- **Spring Boot**: 3.5.7
- **Java**: 21
- **Thymeleaf**: 3.1.3.RELEASE (for thymeleaf-tomcat)
- **OGNL**: 3.3.5 (required by Thymeleaf Standard Dialect)

### thymeleaf-tomcat Specific
- Uses raw Thymeleaf (not Spring Boot's Thymeleaf starter)
- WAR packaging for deployment to servlet containers
- Cargo Maven plugin for embedded Tomcat 10.x

## Build and Run

### thymeleaf-tomcat

```bash
cd thymeleaf-tomcat

# Build the project
./buildMaven.sh

# Run with embedded Tomcat
./runMaven.sh

# Access the application
# http://localhost:8080/
```

### thymeleaf-templates

```bash
cd thymeleaf-templates

# Build the project
./buildMaven.sh

# Run the Spring Boot application
./runMaven.sh
# or
mvn spring-boot:run

# Access the application
# http://localhost:8080/thymeleaf-templates
```

## Configuration

### File Encoding
The `file.encoding` property is defined in `pom.xml` and reused throughout:
- Cargo container system properties
- Servlet URI encoding
- Tomcat connector URI encoding

### Static Resources
Static resources (CSS, images, JavaScript) are served from:
- `src/main/webapp/css/`
- `src/main/webapp/images/`
- `src/main/webapp/js/`

The `ThymeleafFilter` automatically excludes these paths from template processing.

## Recent Updates

### Controller Refactoring
- **Abstract Base Class**: Introduced `AbstractThymeleafController` to centralize common logic
- **Constructor Injection**: All dependencies injected via constructor
- **Method Overloading**: Public `process()` method is final; protected `handleTemplate()` method for implementation
- **Package Organization**: Core Thymeleaf classes moved to `thymeleaf` subpackage

### Spring Boot Upgrade
- Upgraded to Spring Boot 3.5.7
- Updated Thymeleaf API usage for 3.1+ compatibility
- Migrated from `ServletContextTemplateResolver` to `WebApplicationTemplateResolver`
- Updated to use `JakartaServletWebApplication` and `JakartaServletWebExchange`

### Dependencies
- Added explicit OGNL dependency (required by Thymeleaf)
- Replaced Spring Boot Thymeleaf starter with direct Thymeleaf dependency for raw usage
- Upgraded maven-resources-plugin to 3.3.1 to resolve warnings

## References

- [Spring Thymeleaf Fragments](https://www.baeldung.com/spring-thymeleaf-fragments)
- [Spring Web Modules - Thymeleaf](https://github.com/eugenp/tutorials/tree/master/spring-web-modules/spring-thymeleaf)
- [Spring Boot Thymeleaf Example: CRUD Application](https://www.bezkoder.com/spring-boot-thymeleaf-example/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)

## Additional Resources

### Pagination & Sorting
- [Thymeleaf Pagination example](https://www.bezkoder.com/thymeleaf-pagination/)
- [Thymeleaf Pagination and Sorting example](https://www.bezkoder.com/thymeleaf-pagination-and-sorting-example/)

### File Upload
- [Spring Boot File upload example with Multipart File](https://www.bezkoder.com/spring-boot-file-upload/)

### Exception Handling
- [Spring Boot @ControllerAdvice & @ExceptionHandler example](https://www.bezkoder.com/spring-boot-controlleradvice-exceptionhandler/)
- [@RestControllerAdvice example in Spring Boot](https://www.bezkoder.com/spring-boot-restcontrolleradvice/)

### REST API Examples
- [Spring Boot + MySQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/)
- [Spring Boot + PostgreSQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-postgresql-example/)
- [Spring Boot + H2: CRUD Rest API example](https://www.bezkoder.com/spring-boot-jpa-h2-example/)

### Security
- [Spring Boot + Spring Security JWT Authentication & Authorization](https://www.bezkoder.com/spring-boot-jwt-authentication/)

### Fullstack Examples
- [Vue + Spring Boot example](https://www.bezkoder.com/spring-boot-vue-js-crud-example/)
- [React + Spring Boot + MySQL example](https://www.bezkoder.com/react-spring-boot-crud/)
- [Angular + Spring Boot examples](https://www.bezkoder.com/angular-spring-boot-crud/)


### Don't Remove these
For more detail, please visit:
> [Spring Boot Thymeleaf example: CRUD Application](https://www.bezkoder.com/spring-boot-thymeleaf-example/)

More Practice:
> [Thymeleaf Pagination example](https://www.bezkoder.com/thymeleaf-pagination/)

> [Thymeleaf Pagination and Sorting example](https://www.bezkoder.com/thymeleaf-pagination-and-sorting-example/)

> [Spring Boot File upload example with Multipart File](https://www.bezkoder.com/spring-boot-file-upload/)

> [Spring Boot Pagination & Filter example | Spring JPA, Pageable](https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/)

> [Spring Data JPA Sort/Order by multiple Columns | Spring Boot](https://www.bezkoder.com/spring-data-sort-multiple-columns/)

> [Spring Boot Repository Unit Test with @DataJpaTest](https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/)

> [Deploy Spring Boot App on AWS – Elastic Beanstalk](https://www.bezkoder.com/deploy-spring-boot-aws-eb/)

Exception Handling:
> [Spring Boot @ControllerAdvice & @ExceptionHandler example](https://www.bezkoder.com/spring-boot-controlleradvice-exceptionhandler/)

> [@RestControllerAdvice example in Spring Boot](https://www.bezkoder.com/spring-boot-restcontrolleradvice/)

Rest API:
> [Spring Boot + MySQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/)

> [Spring Boot + PostgreSQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-postgresql-example/)

> [Spring Boot + SQL Server: CRUD Rest API example](https://www.bezkoder.com/spring-boot-sql-server/)

> [Spring Boot + H2: CRUD Rest API example](https://www.bezkoder.com/spring-boot-jpa-h2-example/)

> [Spring Boot + MongoDB: CRUD Rest API example](https://www.bezkoder.com/spring-boot-mongodb-crud/)

> [Spring Boot + Oracle: CRUD Rest API example](https://www.bezkoder.com/spring-boot-hibernate-oracle/)

> [Spring Boot + Cassandra: CRUD Rest API example](https://www.bezkoder.com/spring-boot-cassandra-crud/)

Security:
> [Spring Boot + Spring Security JWT Authentication & Authorization](https://www.bezkoder.com/spring-boot-jwt-authentication/)

Fullstack:
> [Vue + Spring Boot example](https://www.bezkoder.com/spring-boot-vue-js-crud-example/)

> [Angular 8 + Spring Boot example](https://www.bezkoder.com/angular-spring-boot-crud/)

> [Angular 10 + Spring Boot example](https://www.bezkoder.com/angular-10-spring-boot-crud/)

> [Angular 11 + Spring Boot example](https://www.bezkoder.com/angular-11-spring-boot-crud/)

> [Angular 12 + Spring Boot example](https://www.bezkoder.com/angular-12-spring-boot-crud/)

> [Angular 13 + Spring Boot example](https://www.bezkoder.com/spring-boot-angular-13-crud/)

> [Angular 14 + Spring Boot example](https://www.bezkoder.com/spring-boot-angular-14-crud/)

> [React + Spring Boot + MySQL example](https://www.bezkoder.com/react-spring-boot-crud/)

> [React + Spring Boot + PostgreSQL example](https://www.bezkoder.com/spring-boot-react-postgresql/)

Run both Back-end & Front-end in one place:
> [Integrate Angular with Spring Boot Rest API](https://www.bezkoder.com/integrate-angular-spring-boot/)

> [Integrate React.js with Spring Boot Rest API](https://www.bezkoder.com/integrate-reactjs-spring-boot/)

> [Integrate Vue.js with Spring Boot Rest API](https://www.bezkoder.com/integrate-vue-spring-boot/)

## Run Spring Boot Application
```
mvn spring-boot:run
```

[Why HtmlUnit Integration](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server-htmlunit)
----