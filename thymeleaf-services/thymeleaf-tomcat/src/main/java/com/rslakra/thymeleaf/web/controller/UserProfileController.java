package com.rslakra.thymeleaf.web.controller;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserProfileController implements ThymeleafController {

    public UserProfileController() {
        super();
    }

    public void process(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
        throws Exception {

        final WebContext ctx = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale());
        templateEngine.process("userprofile", ctx, servletResponse.getWriter());

    }

}
