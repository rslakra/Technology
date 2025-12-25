package com.rslakra.thymeleaf.web.controller.thymeleaf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ThymeleafController {
    
    /**
     * @param servletRequest
     * @param servletResponse
     * @throws Exception
     */
    public void process(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception;
    
}
