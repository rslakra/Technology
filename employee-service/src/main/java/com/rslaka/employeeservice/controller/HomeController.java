package com.rslaka.employeeservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Home controller to handle root path requests.
 * 
 * @author Rohtash Lakra
 */
@RestController
public class HomeController {

    /**
     * Handles root path and /employee-service path.
     * Returns API information.
     * 
     * @return API information
     */
    @GetMapping({"/", "/employee-service"})
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Employee Service");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "GET /employees", "Get all employees",
            "POST /employees", "Create a new employee"
        ));
        return ResponseEntity.ok(response);
    }
}

