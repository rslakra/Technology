package com.rslakra.thymeleafsidebar.framework.controller;

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
     * Add app version to all models
     */
    @ModelAttribute
    public void addAppVersion(Model model) {
        model.addAttribute("appVersion", appVersion);
    }
}
