package com.rslakra.thymeleaf.web.controller;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements ThymeleafController {

    public HomeController() {
        super();
    }

    public void process(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
        throws Exception {

        WebContext ctx = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale());
        ctx.setVariable("today", Calendar.getInstance());

        templateEngine.process("home", ctx, servletResponse.getWriter());
    }
}
