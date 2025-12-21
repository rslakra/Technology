package com.rslakra.springservices.thymeleaflayout.framework;

import jakarta.servlet.http.HttpServletRequest;

public enum RequestUtils {
    
    INSTANCE;
    
    /**
     * Returns true if it's a POST request otherwise false.
     *
     * @param servletRequest
     * @return
     */
    public static boolean isPostRequest(HttpServletRequest servletRequest) {
        return "post".equals(servletRequest.getMethod().toLowerCase());
    }
}
