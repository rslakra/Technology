package com.rslakra.thymeleaf.web.application;

import com.rslakra.thymeleaf.web.controller.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ThymeleafApplication {
    
    private TemplateEngine templateEngine;
    private Map<String, ThymeleafController> controllersByEndpoint;
    
    /**
     * @param servletContext
     */
    public ThymeleafApplication(final ServletContext servletContext) {
        super();
        ServletContextTemplateResolver templateResolver = getServletContextTemplateResolver(servletContext);
        
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        
        this.controllersByEndpoint = new HashMap<String, ThymeleafController>();
        this.controllersByEndpoint.put("/", new HomeController());
        this.controllersByEndpoint.put("/product/list", new ProductListController());
        this.controllersByEndpoint.put("/product/comments", new ProductCommentsController());
        this.controllersByEndpoint.put("/order/list", new OrderListController());
        this.controllersByEndpoint.put("/order/details", new OrderDetailsController());
        this.controllersByEndpoint.put("/subscribe", new SubscribeController());
        this.controllersByEndpoint.put("/userprofile", new UserProfileController());
        
    }
    
    /**
     * @param servletContext
     * @return
     */
    private static ServletContextTemplateResolver getServletContextTemplateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        
        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        
        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable(true);
        return templateResolver;
    }
    
    /**
     * @param request
     * @return
     */
    private static String getRequestPath(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        final String contextPath = request.getContextPath();
        
        final int fragmentIndex = requestURI.indexOf(';');
        if (fragmentIndex != -1) {
            requestURI = requestURI.substring(0, fragmentIndex);
        }
        
        if (requestURI.startsWith(contextPath)) {
            return requestURI.substring(contextPath.length());
        }
        return requestURI;
    }
    
    /**
     * @param request
     * @return
     */
    public ThymeleafController resolveControllerForRequest(final HttpServletRequest request) {
        final String path = getRequestPath(request);
        return this.controllersByEndpoint.get(path);
    }
    
    /**
     * @return
     */
    public ITemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }
    
}
