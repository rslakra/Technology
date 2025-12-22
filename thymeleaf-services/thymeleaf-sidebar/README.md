# Spring Boot Thymeleaf Sidebar Application

A modern Spring Boot 3.5.7 application with Thymeleaf template engine featuring a responsive sidebar navigation, breadcrumb navigation, and CRUD operations.

## Features

- **Spring Boot 3.5.7** with Java 21
- **Responsive Sidebar Navigation** with collapse/expand functionality
- **Breadcrumb Navigation** that dynamically updates based on current page and tabs
- **Version Display** in sidebar footer (from `app.version` property)
- **Settings Page** with tabbed interface (General, Change Password, Info, Social Links, Connections, Notifications)
- **CRUD Operations** for Tasks and Tutorials
- **H2 Database** integration with Spring Data JPA
- **Modern UI** with Bootstrap 5.3.3 and Font Awesome icons
- **Class-based JavaScript Architecture** using singleton pattern (Breadcrumb, Sidebar classes)

## Technology Stack

- **Spring Boot**: 3.5.7
- **Java**: 21
- **Thymeleaf**: 3.1.3.RELEASE
- **Bootstrap**: 5.3.3
- **Font Awesome**: 5.8.1
- **H2 Database**: In-memory database
- **Spring Data JPA**: For database operations
- **Maven**: Build tool

## Project Structure

```
src/main/
├── java/com/rslakra/thymeleafsidebar/
│   ├── admin/          # Admin controller and service
│   ├── framework/      # AbstractController, utilities
│   ├── home/           # Home controller and service
│   ├── report/         # Report controller and service
│   ├── setting/        # Settings controller and service
│   ├── task/           # Task CRUD operations
│   └── tutorial/       # Tutorial CRUD operations
└── resources/
    ├── templates/      # Thymeleaf templates
    │   ├── fragments/  # Reusable fragments
    │   │   ├── sidebar.html      # Complete sidebar (profile, navigation, footer)
    │   │   ├── menu-navbar.html  # Top navbar (hamburger + menu)
    │   │   ├── header.html       # Header (navbar + breadcrumb)
    │   │   ├── breadcrumb.html   # Breadcrumb navigation
    │   │   ├── profile.html      # User profile section
    │   │   └── head.html         # Page head (CSS, JS)
    │   ├── admin/      # Admin pages
    │   ├── setting/    # Settings pages
    │   ├── task/       # Task pages
    │   └── tutorial/   # Tutorial pages
    └── static/
        ├── css/        # Stylesheets (core, sidebar, form)
        └── js/         # JavaScript (breadcrumb, sidebar actions)
```

## Key Components

### Sidebar Navigation
- Collapsible sidebar with smooth animations
- State persistence using localStorage
- Active menu item highlighting
- Profile section with user information
- Settings and Admin links in footer
- Version display at bottom

### Breadcrumb Navigation
- Dynamic breadcrumb generation based on URL path
- Support for hash fragments (settings tabs)
- Automatic updates on navigation
- JavaScript-based with `Breadcrumb` singleton class

### JavaScript Architecture
- **Breadcrumb Class** (Singleton): Manages breadcrumb navigation with dynamic path generation
- **Sidebar Class** (Singleton): Handles sidebar state, hamburger toggle, active menu items, profile dropdown, settings tab activation, and early initialization to prevent FOUC (Flash of Unstyled Content)

## Build and Run

### Prerequisites
- Java 21
- Maven 3.6+

### Build the Application

Using the build script:
```bash
./buildMaven.sh
```

Or using Maven directly:
```bash
mvn clean install
```

The build script uses a shared `version.sh` file located at `../version.sh` to generate version numbers based on git commit count.

### Run the Application

Using the run script:
```bash
./runMaven.sh
```

Or using Maven directly:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Application version (displayed in sidebar)
app.version = v0.0.1.SNAPSHOT

# H2 Database
spring.datasource.url = jdbc:h2:file:~/Downloads/H2DB/ThymeleafSidebar;AUTO_SERVER=TRUE;
spring.h2.console.enabled = true
spring.h2.console.path = /h2

# JPA
spring.jpa.hibernate.ddl-auto = update
spring.jpa.show-sql = false
```

### Access H2 Console

Once the application is running, access the H2 console at:
```
http://localhost:8080/h2
```

## Pages and Routes

- `/` - Home/Dashboard
- `/tasks` - Task list and management
- `/tutorials` - Tutorial list and management
- `/reports` - Reports page
- `/admin` - Admin page
- `/settings` - Settings page with tabs:
  - `/settings#account-general` - General settings
  - `/settings#account-change-password` - Change password
  - `/settings#account-info` - Account info
  - `/settings#account-social-links` - Social links
  - `/settings#account-connections` - Account connections
  - `/settings#account-notifications` - Notifications

## Sidebar Features

- **Collapse/Expand**: Click the hamburger menu to toggle sidebar
- **State Persistence**: Sidebar state (collapsed/expanded) is saved in localStorage
- **Active Menu Highlighting**: Current page is highlighted in the sidebar
- **Profile Menu**: Click the user icon in top navbar to access:
  - Profile
  - Change Password
  - Logout
- **Version Display**: Application version is displayed at the bottom of the sidebar

## Browser Support

- Modern browsers (Chrome, Firefox, Safari, Edge)
- Responsive design for different screen sizes

## Development

### Version Management

The project uses a shared version script at `../version.sh` that generates version numbers based on git commit count:
- SNAPSHOT version: `0.0.{commit-count}-SNAPSHOT`
- Release version: `0.0.{commit-count}`

### Fragment Structure

The project uses a simplified fragment structure with merged components:
- **`sidebar.html`**: Contains the complete sidebar structure (profile, navigation menu, and footer with settings/version)
- **`menu-navbar.html`**: Contains the top navigation bar (hamburger menu and user profile dropdown)
- **`header.html`**: Combines the navbar and breadcrumb bar for consistent page headers

### Code Style

The project uses Checkstyle for code quality. Configuration is pulled from:
```
https://raw.githubusercontent.com/rslakra/code-styles/master
```

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)
- [Font Awesome Icons](https://fontawesome.com/icons)

## Inspiration

UI/UX inspired by:
- [Syncfusion Sidebar Menu](https://ej2.syncfusion.com/demos/sidebar/sidebar-menu/)
- [Syncfusion Responsive Panel](https://ej2.syncfusion.com/demos/sidebar/responsive-panel/)

---

## License

This project is part of the Technology Services collection.
