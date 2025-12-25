# Spring Boot Thymeleaf Templates

A Spring Boot application demonstrating multiple Thymeleaf layout versions with advanced UI features including sidebar navigation, dark mode, and responsive design.

## Overview

This application showcases different Thymeleaf layout implementations (v0, v1, v2) with progressively enhanced features. The v0 layout includes a fully functional sidebar with dark mode toggle, profile dropdown, and modern UI components.

## Features

### Layout Versions

- **v0 Layout**: Advanced layout with:
  - Collapsible sidebar navigation
  - Dark mode toggle
  - Profile dropdown menu
  - Responsive top navbar
  - Modern CSS with CSS variables
  - Persistent sidebar state (localStorage)
  - Smooth transitions and animations

- **v1 Layout**: Intermediate layout implementation
- **v2 Layout**: Basic layout with Bootstrap integration
- **Default Layout**: Standard Thymeleaf layout

### v0 Layout Features

#### Sidebar Navigation
- **Expandable/Collapsible**: Toggle sidebar width with smooth transitions
- **Persistent State**: Sidebar state saved to localStorage
- **Menu Items**: Left-aligned navigation links with icons
- **Bottom Section**: Logout and dark mode toggle at the bottom
- **Profile Header**: User profile photo and information at the top

#### Dark Mode
- **Toggle Switch**: Sun/moon icon toggle in sidebar
- **Persistent State**: Dark mode preference saved to localStorage
- **Full Theme Support**: All components styled for dark mode
- **Icon Visibility**: Icons change based on mode in both expanded and collapsed states

#### Profile Dropdown
- **User Icon**: Clickable profile icon in top navbar
- **Menu Items**:
  - Profile → `/v0/setting#account`
  - Change Password → `/v0/setting#password`
  - Logout → `/v0` (home screen)
- **Click Outside**: Dropdown closes when clicking outside

#### Settings Page
- **Tab Navigation**: Hash-based tab activation (`#account`, `#password`)
- **Profile Image**: Constrained circular profile photo
- **Bootstrap Tabs**: Integrated with Bootstrap tab component

## Project Structure

```
src/main/
├── java/com/rslakra/thymeleaftemplates/
│   ├── home/
│   │   └── controller/
│   │       ├── HomeController.java (default)
│   │       ├── HomeControllerV0.java
│   │       ├── HomeControllerV1.java
│   │       └── HomeControllerV2.java
│   └── ThymeleafTemplatesApplication.java
├── resources/
│   ├── application.properties
│   ├── static/
│   │   ├── css/
│   │   │   └── v0/
│   │   │       ├── bootstrap.min.css
│   │   │       ├── core-styles.css
│   │   │       ├── form-styles.css
│   │   │       ├── modal-styles.css
│   │   │       ├── sidebar-styles.css
│   │   │       ├── setting-styles.css
│   │   │       ├── styles.css
│   │   │       └── table-styles.css
│   │   ├── js/
│   │   │   └── v0/
│   │   │       └── navbar-actions.js (Sidebar class)
│   │   └── images/
│   │       └── profile.jpg
│   └── templates/
│       ├── layouts/
│       │   ├── default.html
│       │   ├── layout-v0.html
│       │   ├── layout-v1.html
│       │   └── layout-v2.html
│       ├── fragments/
│       │   └── v0/
│       │       ├── sidebar.html
│       │       └── top-navbar.html
│       ├── views/
│       │   ├── v0/
│       │   │   ├── home.html
│       │   │   ├── admin/
│       │   │   │   └── index.html
│       │   │   ├── report/
│       │   │   │   └── index.html
│       │   │   ├── setting/
│       │   │   │   └── index.html
│       │   │   └── task/
│       │   │       └── listTasks.html
│       │   ├── v1/
│       │   │   └── home.html
│       │   └── v2/
│       │       └── index.html
│       └── index.html
```

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Thymeleaf**: Managed by Spring Boot
- **Bootstrap**: 4.3.1 (v0 layout)
- **Font Awesome**: 5.8.1 (icons)
- **Boxicons**: 2.1.1 (sidebar icons)
- **jQuery**: 3.6.3
- **H2 Database**: In-memory database for development

## Build and Run

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Build the Project

```bash
cd thymeleaf-templates

# Build using Maven
./buildMaven.sh
# or
mvn clean package
```

### Run the Application

```bash
# Run using Maven
./runMaven.sh
# or
mvn spring-boot:run
```

### Access the Application

- **Default Home**: http://localhost:8080/thymeleaf-templates/
- **V0 Layout**: http://localhost:8080/thymeleaf-templates/v0
- **V1 Layout**: http://localhost:8080/thymeleaf-templates/v1
- **V2 Layout**: http://localhost:8080/thymeleaf-templates/v2
- **Settings Page**: http://localhost:8080/thymeleaf-templates/v0/setting
- **H2 Console**: http://localhost:8080/thymeleaf-templates/h2

## Configuration

### Application Properties

Located in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.contextPath=/thymeleaf-templates

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:file:~/Downloads/H2DB/ThymeleafTemplates;AUTO_SERVER=TRUE;
```

### CSS Variables

The v0 layout uses CSS variables defined in `core-styles.css` and `sidebar-styles.css`:

```css
:root {
    --sidebar-color: #2e8fff;
    --body-color: #FFFFFF;
    --text-color: #333333;
    --spacing-sm: 8px;
    --spacing-md: 16px;
    --spacing-lg: 24px;
    --spacing-xl: 32px;
    /* ... more variables */
}
```

## JavaScript Architecture

### Sidebar Class

The `Sidebar` class (`navbar-actions.js`) manages:
- Sidebar expand/collapse state
- Dark mode toggle
- Profile dropdown functionality
- State persistence (localStorage)
- Navigation handling

**Key Methods:**
- `init()` - Initialize sidebar and attach event listeners
- `toggleSidebar()` - Toggle sidebar expanded/collapsed state
- `handleModeSwitchClick()` - Toggle dark mode
- `handleProfileIconClick()` - Toggle profile dropdown
- `handleMenuItemClick()` - Handle profile dropdown navigation
- `getBasePath()` - Extract application context path

## CSS Architecture

### Modular CSS Structure

- **core-styles.css**: Base styles and CSS variables
- **sidebar-styles.css**: Sidebar and top navbar styles
- **form-styles.css**: Form element styles
- **table-styles.css**: Table styles
- **modal-styles.css**: Modal dialog styles
- **setting-styles.css**: Settings page specific styles
- **styles.css**: Additional utility styles

### Dark Mode Support

Dark mode is implemented using:
- CSS variables for color values
- `body.dark` class selector
- JavaScript to toggle the class
- localStorage for persistence

## Recent Updates

### UI Improvements
- **Sidebar Refactoring**: Restructured HTML into distinct top, middle, and bottom sections
- **CSS Variables**: Replaced hardcoded values with CSS variables for better maintainability
- **Class Renaming**: Semantic class names (e.g., `profile-photo-container`, `profile-info`)
- **Alignment Fixes**: Improved menu item alignment and spacing
- **Dark Mode**: Full dark mode implementation with icon visibility controls

### JavaScript Enhancements
- **Singleton Pattern**: Sidebar class uses singleton pattern
- **State Management**: Improved state persistence and restoration
- **Navigation**: Context-aware navigation with base path handling
- **Event Handling**: Improved click handlers and event propagation

### Backend Updates
- **Controller Updates**: Fixed template paths and null safety checks
- **Model Attributes**: Ensured all required model attributes are present

## Development

### Adding New Layout Versions

1. Create a new layout file in `templates/layouts/` (e.g., `layout-v3.html`)
2. Create a corresponding controller (e.g., `HomeControllerV3.java`)
3. Add view templates in `templates/views/v3/`
4. Update navigation links to include the new version

### Customizing v0 Layout

#### Sidebar Menu Items
Edit `templates/fragments/v0/sidebar.html` to add/modify menu items.

#### Styling
- Modify CSS variables in `core-styles.css` for global changes
- Update `sidebar-styles.css` for sidebar-specific styling
- Use `!important` sparingly and only when necessary

#### JavaScript Behavior
Modify `navbar-actions.js` to change sidebar behavior, add new features, or customize navigation.

## Troubleshooting

### Sidebar Not Expanding/Collapsing
- Clear browser cache and localStorage
- Check browser console for JavaScript errors
- Verify `navbar-actions.js` is loaded correctly

### Dark Mode Not Working
- Check if `body.dark` class is being toggled
- Verify CSS variables are defined in `body.dark` selector
- Check localStorage for `darkMode` key

### Profile Dropdown Not Showing
- Verify `navbar-right-profile` class is present
- Check JavaScript console for errors
- Ensure event listeners are attached correctly

### Tab Navigation Not Working
- Verify Bootstrap JavaScript is loaded
- Check hash fragment in URL (e.g., `#account`, `#password`)
- Check browser console for JavaScript errors

## References

- [Spring Boot Thymeleaf Example: CRUD Application](https://www.bezkoder.com/spring-boot-thymeleaf-example/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Thymeleaf Layout Dialect](https://github.com/ultraq/thymeleaf-layout-dialect)
- [Bootstrap Documentation](https://getbootstrap.com/docs/4.3/)

## Additional Resources

### Pagination & Sorting
- [Thymeleaf Pagination example](https://www.bezkoder.com/thymeleaf-pagination/)
- [Thymeleaf Pagination and Sorting example](https://www.bezkoder.com/thymeleaf-pagination-and-sorting-example/)

### File Upload
- [Spring Boot File upload example with Multipart File](https://www.bezkoder.com/spring-boot-file-upload/)

### Exception Handling
- [Spring Boot @ControllerAdvice & @ExceptionHandler example](https://www.bezkoder.com/spring-boot-controlleradvice-exceptionhandler/)

### REST API Examples
- [Spring Boot + MySQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-jpa-crud-rest-api/)
- [Spring Boot + PostgreSQL: CRUD Rest API example](https://www.bezkoder.com/spring-boot-postgresql-example/)

### Security
- [Spring Boot + Spring Security JWT Authentication & Authorization](https://www.bezkoder.com/spring-boot-jwt-authentication/)


### Don't Remove These
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
