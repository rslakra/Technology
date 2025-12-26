package com.rslakra.thymeleafsidebarsversion.framework.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Rohtash Lakra
 * @created 1/27/23 11:29 AM
 */
public abstract class AbstractController {

    @Value("${app.version:v0.0.1}")
    private String appVersion;

    /**
     * Add app version and current version path to all models
     * 
     * @param model the model
     * @param request the HTTP request
     */
    @ModelAttribute
    public void addAppVersion(Model model, HttpServletRequest request) {
        model.addAttribute("appVersion", appVersion);
        
        // Extract version from request path (e.g., /v0/, /v1/, /admin/v0, /tasks/v1, etc.)
        String requestPath = request.getRequestURI();
        String version = "v0"; // default version
        
        // Extract version from path patterns like /v0/, /v0/index, /admin/v0, /tasks/v1, etc.
        if (requestPath != null && requestPath.length() > 1) {
            String[] pathParts = requestPath.split("/");
            for (String part : pathParts) {
                if (part.matches("^v\\d+$")) {
                    version = part;
                    break;
                }
            }
        }
        
        model.addAttribute("version", version);
    }
}
