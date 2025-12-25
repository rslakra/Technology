package com.rslakra.thymeleaftemplates.framework.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller to handle errors and route v0 errors to v0 error template.
 *
 * @author Rohtash Lakra
 * @created 1/31/23 4:58 PM
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles error requests and routes to appropriate error template based on the request path.
     *
     * @param request the HTTP request
     * @param model   the model
     * @return the error template name
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        // Also check the original request URI in case ERROR_REQUEST_URI is not set
        if (path == null) {
            path = request.getRequestURI();
        }

        // Check if the error is from a v0 path
        // The path should start with /v0 or contain /v0/ (accounting for context path)
        boolean isV0Path = path != null && (path.startsWith("/v0/") || path.equals("/v0") || 
                          path.contains("/v0/") || path.endsWith("/v0"));
        
        if (isV0Path) {
            // Add error attributes to model
            if (status != null) {
                model.addAttribute("status", status.toString());
                Integer statusCode = Integer.valueOf(status.toString());
                if (statusCode == HttpStatus.NOT_FOUND.value()) {
                    model.addAttribute("error", "Not Found");
                } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    model.addAttribute("error", "Internal Server Error");
                } else {
                    model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
                }
            }
            if (message != null) {
                model.addAttribute("message", message.toString());
            } else if (error != null) {
                model.addAttribute("message", ((Exception) error).getMessage());
            }

            return "views/v0/error";
        }

        // Default error handling for non-v0 paths
        if (status != null) {
            model.addAttribute("status", status.toString());
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Not Found");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Internal Server Error");
            } else {
                model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
            }
        }
        if (message != null) {
            model.addAttribute("message", message.toString());
        } else if (error != null) {
            model.addAttribute("message", ((Exception) error).getMessage());
        }

        return "error";
    }
}

