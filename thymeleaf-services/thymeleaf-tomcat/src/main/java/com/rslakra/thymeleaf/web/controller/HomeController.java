package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.web.controller.thymeleaf.AbstractThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;

import java.util.Calendar;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HomeController extends AbstractThymeleafController {

    public HomeController(
            final IServletWebApplication webApplication,
            final ServletContext servletContext,
            final ITemplateEngine templateEngine) {
        super(webApplication, servletContext, templateEngine);
    }

    @Override
    protected void handleTemplate(
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse,
            final WebContext webContext) throws Exception {

        webContext.setVariable("today", Calendar.getInstance());
        this.templateEngine.process("home", webContext, servletResponse.getWriter());
    }
}
