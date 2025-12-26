package com.rslakra.thymeleafsidebarsversion.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration to handle static resources and version paths
 * 
 * By using specific resource handler patterns (not /**), we ensure that bare version 
 * paths like /v0, /v1, etc. are handled by controllers rather than being treated as 
 * static resources.
 *
 * @author Rohtash Lakra
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.versions:v0,v1,v2,v3,v4}")
    private String versionsProperty;

    /**
     * Suppress Chrome DevTools warning by handling the well-known endpoint
     * This is a harmless request from Chrome DevTools that can be safely ignored
     */
    @org.springframework.web.bind.annotation.RestController
    @RequestMapping("/.well-known")
    public static class WellKnownController {
        
        @RequestMapping(value = "/appspecific/com.chrome.devtools.json", method = RequestMethod.GET)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public void chromeDevTools() {
            // Return 404 silently - Chrome DevTools will handle it gracefully
        }
    }

    /**
     * Configure static resource handling with a resolver that excludes bare version paths
     * This ensures that paths like /v0, /v1, etc. are handled by controllers
     * while still serving static resources like /v0/css/..., /v0/js/..., etc.
     *
     * @param registry the resource handler registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Parse versions from property (comma-separated)
        String[] versionArray = versionsProperty.split(",");
        
        // Handle versioned static resources with specific sub-paths
        // Use patterns that require at least one path segment after the version
        // This ensures /v0, /v1, etc. are NOT matched, but /v0/css/... is matched
        for (String version : versionArray) {
            String trimmedVersion = version.trim();
            if (trimmedVersion.isEmpty()) {
                continue; // Skip empty versions
            }
            registry.addResourceHandler("/" + trimmedVersion + "/css/**", 
                                       "/" + trimmedVersion + "/js/**",
                                       "/" + trimmedVersion + "/images/**",
                                       "/" + trimmedVersion + "/fonts/**")
                    .addResourceLocations("classpath:/static/" + trimmedVersion + "/css/",
                                         "classpath:/static/" + trimmedVersion + "/js/",
                                         "classpath:/static/" + trimmedVersion + "/images/",
                                         "classpath:/static/" + trimmedVersion + "/fonts/",
                                         "classpath:/static/" + trimmedVersion + "/",
                                         "classpath:/static/", 
                                         "classpath:/public/", 
                                         "classpath:/resources/", 
                                         "classpath:/META-INF/resources/");
        }
        
        // Handle root-level static resources (css, js, images, fonts, etc.)
        // This ensures /css/styles.css maps to classpath:/static/css/styles.css
        registry.addResourceHandler("/css/**", "/js/**", "/images/**", "/fonts/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", 
                                     "classpath:/static/images/", "classpath:/static/fonts/",
                                     "classpath:/static/", "classpath:/public/", 
                                     "classpath:/resources/", "classpath:/META-INF/resources/");
        
        // Handle files with extensions at root level
        registry.addResourceHandler("/*.css", "/*.js", "/*.png", "/*.jpg", "/*.jpeg", 
                                   "/*.gif", "/*.svg", "/*.ico", "/*.woff", "/*.woff2", 
                                   "/*.ttf", "/*.eot")
                .addResourceLocations("classpath:/static/", "classpath:/public/", 
                                     "classpath:/resources/", "classpath:/META-INF/resources/");
    }
}

