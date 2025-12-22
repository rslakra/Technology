package com.rslakra.thymeleafsidebars.framework.controller.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Rohtash Lakra
 * @created 10/15/21 6:11 PM
 */
public abstract class AbstractWebController<T> implements WebController<T> {

    private String prefix;
    private Class<T> objectType;

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
