# Spring Boot Thymeleaf Sidebars Application

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
- **Thymeleaf Layout Dialect**: 3.2.1
- **Bootstrap**: 5.3.3
- **Font Awesome**: 5.8.1
- **H2 Database**: In-memory database
- **Spring Data JPA**: For database operations
- **Maven**: Build tool

## Project Structure

```
src/main/
├── java/com/rslakra/thymeleafsidebars/
│   ├── admin/          # Admin controller and service
│   ├── framework/      # AbstractWebController, utilities
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
    │       │   ├── layouts/    # Layout templates
    │   │   ├── default.html  # Main layout with sidebar (used by most views)
    │   │   └── layout.html  # Simple layout without sidebar (used by /v1 views only)
    │   ├── views/      # View templates
    │   │   ├── admin/  # Admin pages
    │   │   ├── setting/# Settings pages
    │   │   ├── task/   # Task pages
    │   │   └── tutorial/# Tutorial pages
    └── static/
        ├── css/        # Stylesheets (core, sidebar, form, setting, modal, table)
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

# Web and API prefixes
webPrefix = /api
apiPrefix = /rest

# H2 Database
spring.datasource.url = jdbc:h2:file:~/Downloads/H2DB/ThymeleafSidebars;AUTO_SERVER=TRUE;
spring.h2.console.enabled = true
spring.h2.console.path = /h2

# JPA
spring.jpa.hibernate.ddl-auto = update
spring.jpa.show-sql = true
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
  - `/settings#account` - Account settings
  - `/settings#password` - Password settings
  - `/settings#security` - Security settings
  - `/settings#application` - Application settings
  - `/settings#notification` - Notification settings

## Sidebar Features

- **Collapse/Expand**: Click the hamburger menu to toggle sidebar
- **State Persistence**: Sidebar state (collapsed/expanded) is saved in localStorage
- **Active Menu Highlighting**: Current page is highlighted in the sidebar
- **Profile Menu**: Click the user icon in top navbar to access:
  - Profile (`/settings`)
  - Change Password (`/settings#password`)
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

### Layout Structure

The project uses **Thymeleaf Layout Dialect** with two layout files:

#### 1. `layouts/default.html` (Main Layout - With Sidebar)
- **Used by**: Most views (tasks, tutorials, settings, admin, reports, etc.)
- **Features**:
  - Full sidebar navigation with collapse/expand
  - Top navbar with hamburger menu
  - Breadcrumb navigation
  - Bootstrap 5.3.3
  - All CSS and JavaScript files loaded
- **Usage**: Views use `layout:decorate="~{layouts/default}"` and provide content via `layout:fragment="contents"`

#### 2. `layouts/layout.html` (Simple Layout - Without Sidebar)
- **Used by**: Only `/v1` views (v1/index.html, v1/about-us.html, v1/contact-us.html)
- **Features**:
  - Simple Bootstrap 4 navbar
  - No sidebar
  - Basic layout for legacy/alternative views
- **Usage**: Views use `layout:decorate="~{layouts/layout}"` and provide content via `layout:fragment="contents"`

#### View Organization
- **Main views** (`/views/task/`, `/views/tutorial/`, `/views/setting/`, etc.) → Use `layouts/default.html`
- **V1 views** (`/views/v1/`) → Use `layouts/layout.html`
- **Legacy task views** (`/views/task/task.html`, `/views/task/listTasks-param.html`) → Use old fragment approach with `task/layout.html` (not recommended for new development)

### Fragment Structure

The project uses a simplified fragment structure with merged components:
- **`sidebar.html`**: Contains the complete sidebar structure (profile, navigation menu, and footer with settings/version)
- **`menu-navbar.html`**: Contains the top navigation bar (hamburger menu and user profile dropdown)
- **`header.html`**: Combines the navbar and breadcrumb bar for consistent page headers

### Bootstrap Version

All views use **Bootstrap 5.3.3** (loaded in `layouts/default.html`). Views should use Bootstrap 5 syntax:
- `data-bs-dismiss` instead of `data-dismiss`
- `data-bs-toggle` instead of `data-toggle`
- `btn-close` instead of `close` button with `&times;`
- `me-*` and `ms-*` instead of `mr-*` and `ml-*` for margins
- `bootstrap.Modal` JavaScript API instead of `$().modal()`

### Code Style

The project uses Checkstyle for code quality. Configuration is pulled from:
```
https://raw.githubusercontent.com/rslakra/code-styles/master
```

### Hot Reload with Spring Boot DevTools

Spring Boot DevTools provides two types of reloading:

1. **Automatic Restart**: For Java class changes (controllers, services, etc.)
2. **LiveReload**: For static resources (HTML, CSS, JS) - requires browser connection

#### Setup Instructions

1. **Verify DevTools Dependency**

The `spring-boot-devtools` dependency should be in `pom.xml` with:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

2. **Run the Application**

```bash
mvn spring-boot:run
```

3. **Check DevTools is Active**

When the application starts, you should see in the console:
```
LiveReload server is running on port 35729
```

4. **Enable LiveReload in Browser**

**Option A: Install LiveReload Browser Extension**
- Chrome: [LiveReload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggjgedjdmgemjmlhcdh)
- Firefox: [LiveReload](https://addons.mozilla.org/en-US/firefox/addon/livereload/)
- Safari: [LiveReload](https://livereload.com/extensions/)

After installing:
1. Open your application in the browser (e.g., `http://localhost:8080`)
2. Click the LiveReload extension icon to connect
3. The icon should show "connected"

**Option B: Manual Browser Refresh**
- For static resources (HTML, CSS, JS), you'll need to manually refresh the browser
- Java class changes will still trigger automatic restart

#### What Gets Reloaded

**Automatic Restart (No Browser Action Needed)**
- ✅ Java classes (controllers, services, repositories, etc.)
- ✅ Configuration files (`application.properties`)

**LiveReload (Requires Browser Connection)**
- ✅ HTML templates (`templates/**/*.html`)
- ✅ CSS files (`static/css/**/*.css`)
- ✅ JavaScript files (`static/js/**/*.js`)
- ✅ Static images (`static/images/**`)

#### Troubleshooting

**Issue: Changes Not Reloading**

1. **Check DevTools is Running**
   - Look for "LiveReload server is running on port 35729" in console output

2. **Verify Browser Connection**
   - Check if LiveReload extension is connected (icon should be active)
   - Try disconnecting and reconnecting the extension

3. **Check File Paths**
   - Ensure you're editing files in `src/main/resources/`
   - Changes to files in `target/` won't trigger reload

4. **Clear Browser Cache**
   - Hard refresh: `Ctrl+Shift+R` (Windows/Linux) or `Cmd+Shift+R` (Mac)
   - Or disable cache in DevTools (Network tab → "Disable cache")

5. **Verify Configuration**
   - Check `application.properties` has:
     ```properties
     spring.devtools.restart.enabled = true
     spring.devtools.livereload.enabled = true
     spring.thymeleaf.cache = false
     spring.web.resources.cache.period = 0
     ```

6. **Check Maven Plugin Configuration**
   - Verify `pom.xml` has:
     ```xml
     <plugin>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
         <configuration>
             <excludeDevtools>false</excludeDevtools>
         </configuration>
     </plugin>
     ```

**Issue: Application Restarts Too Often**

If the application restarts on every file change:

1. **Adjust Exclusions**
   ```properties
   spring.devtools.restart.exclude = static/**,public/**,templates/**
   ```

2. **Increase Poll Interval**
   ```properties
   spring.devtools.restart.poll-interval = 2000
   ```

**Issue: LiveReload Port Conflict**

If port 35729 is already in use:

1. **Change LiveReload Port**
   ```properties
   spring.devtools.livereload.port = 35730
   ```

2. **Update Browser Extension**
   - Configure the extension to use the new port

#### Testing Hot Reload

**Test 1: HTML Template Change**
1. Edit `src/main/resources/templates/index.html`
2. Save the file
3. Browser should auto-refresh (if LiveReload connected) or manually refresh

**Test 2: CSS Change**
1. Edit `src/main/resources/static/css/core-styles.css`
2. Save the file
3. Browser should auto-refresh (if LiveReload connected) or manually refresh

**Test 3: JavaScript Change**
1. Edit `src/main/resources/static/js/navbar-actions.js`
2. Save the file
3. Browser should auto-refresh (if LiveReload connected) or manually refresh

**Test 4: Java Class Change**
1. Edit a controller (e.g., `TaskController.java`)
2. Save the file
3. Application should automatically restart (check console)

#### Alternative: Manual Refresh

If LiveReload doesn't work, you can:
1. Make your changes
2. Save the file
3. Manually refresh the browser (`F5` or `Ctrl+R` / `Cmd+R`)

Since `spring.thymeleaf.cache = false` and `spring.web.resources.cache.period = 0`, your changes will be visible immediately after refresh.

#### Notes

- DevTools only works in development mode (not in production)
- The `optional=true` flag ensures DevTools isn't included in production builds
- For best results, use an IDE with Spring Boot support (IntelliJ IDEA, Eclipse STS, VS Code with Spring Boot extension)

## CSS Files Documentation

All CSS files are loaded in `layouts/default.html` and are used throughout the application.

### CSS Files Overview

#### 1. `core-styles.css` (689 lines)
**Purpose**: Core/base styles for the entire application

**Key Classes/Styles**:
- CSS Variables (`:root`) - Colors, transitions, button styles
- Body and base typography styles
- `.box-block` - Main content container
- `.wrapper` - Main wrapper for sidebar layout
- `.container` - Content container
- Text alignment utilities (`.left-align`, `.center-align`, `.right-align`)
- Flex container utilities
- Breadcrumb styles
- Button styles (including Bootstrap button hover effects)
- Various utility classes

**Used In**: All pages using `layouts/default.html`

#### 2. `sidebar-styles.css` (481 lines)
**Purpose**: Sidebar navigation styles and layout

**Key Classes/Styles**:
- `.sidebar` - Sidebar container
- `.sidebar.active` - Collapsed sidebar state
- `.main_container` - Main content area
- `.hamburger` - Hamburger menu button
- `.profile_info` - User profile section
- `.sidebar-menu` - Navigation menu
- `.sidebar-version` - Version display at bottom
- Sidebar transitions and animations
- Responsive sidebar behavior

**Used In**: All pages using `layouts/default.html` (with sidebar)

#### 3. `form-styles.css` (368 lines)
**Purpose**: Form styling and form-related components

**Key Classes/Styles**:
- `.form-container` - Form container wrapper
- `.form-contents` - Form content area
- `.form-button` - Custom form button styling
- `.form-wrapper` - Form wrapper sections
- `.form-group` - Form group spacing
- `.radio`, `.checkbox` - Custom radio/checkbox styles
- `.radio-inline`, `.checkbox-inline` - Inline form controls
- Input field styles (all input types)
- Textarea and select styles
- `.lead-message` - Message textarea
- `.search-icon` - Search icon positioning
- `.grid-container` - Grid layout
- `.form-width-640px` - Fixed width form container

**Used In**: 
- All form pages (editTask.html, editTutorial.html)
- Contact form (contact-us.html)
- Sign-in/Sign-up pages
- Search forms (listTasks.html, listTutorials.html)

#### 4. `table-styles.css` (234 lines)
**Purpose**: Table styling and table-related components

**Key Classes/Styles**:
- `table` - Base table styles
- `.table-style` - Custom table container (legacy, still used in some places)
- Table header styles (`thead`, `th`)
- Table body styles (`tbody`, `td`)
- `.odd` - Alternating row colors
- `.number` - Right-aligned number cells
- Table border and spacing
- Responsive table styles

**Used In**:
- Task list (listTasks.html) - Uses Bootstrap table classes now
- Tutorial list (listTutorials.html) - Uses Bootstrap table classes
- Any page displaying tabular data

**Note**: While Bootstrap 5 table classes are now preferred, `.table-style` is kept for backward compatibility.

#### 5. `modal-styles.css` (244 lines)
**Purpose**: Modal dialog styling

**Key Classes/Styles**:
- `.modal` - Modal container
- `.modal-dialog` - Modal dialog wrapper
- `.modal-dialog-centered` - Centered modal
- `.modal-content` - Modal content area
- `.modal-header` - Modal header
- `.modal-body` - Modal body
- `.modal-footer` - Modal footer
- `.modal-backdrop` - Modal backdrop
- `.modal-open` - Body class when modal is open
- Tooltip styles (legacy, may be replaced by Bootstrap tooltips)

**Used In**: 
- All pages using delete confirmation modal
- Sign-in/Sign-up modals
- Contact form modals
- Any page using `fragments/modal-dialogs.html`

#### 6. `setting-styles.css` (41 lines)
**Purpose**: Settings page specific styles

**Key Classes/Styles**:
- `.shadow` - Box shadow utility
- `.nav-account-settings` - Settings navigation sidebar
- `.tab-content` - Tab content area
- `.form-group` - Form group spacing (Bootstrap compatible)
- `.nav-pills a.nav-link` - Settings tab navigation links
- `.img-circle` - Circular image container

**Used In**:
- Settings page (`views/setting/index.html`)

### CSS Loading Order

CSS files are loaded in `layouts/default.html` in the following order:

1. **Font Awesome 5.8.1** (CDN)
2. **Bootstrap 5.3.3** (CDN)
3. **core-styles.css** - Base styles (must load first)
4. **modal-styles.css** - Modal styles
5. **sidebar-styles.css** - Sidebar layout
6. **form-styles.css** - Form components
7. **table-styles.css** - Table components
8. **setting-styles.css** - Settings page

This order ensures:
- Base styles are available first
- Component styles can override base styles when needed
- Bootstrap classes are available for all custom styles

### CSS Variables

All CSS files use CSS custom properties (variables) defined in `core-styles.css`:

```css
:root {
    --body-color: #FFFFFF;
    --sidebar-color: #2e8fff;
    --primary-color: #0d61b4;
    --primary-color-light: #F6F5FF;
    --toggle-color: #DDDDDD;
    --text-color: #707070;
    --font-size: 16px;
    --block-color: #333;
    --button-border-radius: 8px;
    --button-background-color: #48b3c9;
    --button-hover-color: #0d61b4;
}
```

These variables ensure consistent theming across all CSS files.

### CSS Best Practices

1. **Use CSS Variables**: Always use CSS variables for colors, spacing, and other theme values
2. **Bootstrap First**: Prefer Bootstrap 5 classes when available, then add custom styles
3. **Component-Based**: Each CSS file focuses on a specific component/feature
4. **No Removal**: All styles are actively used - do not remove unused styles without verification
5. **Responsive**: All styles should be responsive and work on mobile devices

### CSS Migration Notes

- **Bootstrap 5**: The application uses Bootstrap 5.3.3. Some legacy Bootstrap 4 classes may still exist but should be migrated.
- **Font Awesome 5**: Uses Font Awesome 5.8.1 (classes: `fas`, `far`, `fab`). Do not use Font Awesome 6 syntax (`fa-solid`, `fa-regular`).
- **Table Styles**: While `.table-style` is still available, new code should use Bootstrap 5 table classes (`.table`, `.table-hover`, `.table-responsive`).

## References



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

[Why HtmlUnit Integration](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-server-htmlunit)


- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Thymeleaf Layout Dialect](https://github.com/ultraq/thymeleaf-layout-dialect)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)
- [Font Awesome Icons](https://fontawesome.com/icons)




## Inspiration

UI/UX inspired by:
- [Syncfusion Sidebar Menu](https://ej2.syncfusion.com/demos/sidebar/sidebar-menu/)
- [Syncfusion Responsive Panel](https://ej2.syncfusion.com/demos/sidebar/responsive-panel/)

---

## License

This project is part of the Technology Services collection.

# Author
- Rohtash Lakra

