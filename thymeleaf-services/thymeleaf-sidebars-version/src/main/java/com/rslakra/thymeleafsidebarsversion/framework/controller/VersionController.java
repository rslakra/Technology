package com.rslakra.thymeleafsidebarsversion.framework.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for version selection page
 *
 * @author Rohtash Lakra
 */
@Controller
@RequestMapping("/")
public class VersionController {

    @Value("${app.versions:v0,v1,v2}")
    private String versionsProperty;

    /**
     * Display version selection page - handles root path "/"
     *
     * @param model
     * @return
     */
    @GetMapping({"", "/"})
    public String versionSelector(Model model) {
        // Parse versions from property (comma-separated)
        String[] versionArray = versionsProperty.split(",");
        List<String> versions = new ArrayList<>();
        
        // Trim whitespace from each version and filter empty strings
        for (String version : versionArray) {
            String trimmed = version.trim();
            if (!trimmed.isEmpty()) {
                versions.add(trimmed);
            }
        }
        
        // Sort versions
        Collections.sort(versions);
        
        model.addAttribute("versions", versions);
        return "index";
    }
}

