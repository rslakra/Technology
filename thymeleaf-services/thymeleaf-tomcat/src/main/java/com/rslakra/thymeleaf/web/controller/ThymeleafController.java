package com.rslakra.thymeleaf.web.controller;

import org.thymeleaf.ITemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ThymeleafController {
    
    /**
     * @param servletRequest
     * @param servletResponse
     * @param servletContext
     * @param templateEngine
     * @throws Exception
     */
    public void process(HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext servletContext, ITemplateEngine templateEngine) throws Exception;
    
}
