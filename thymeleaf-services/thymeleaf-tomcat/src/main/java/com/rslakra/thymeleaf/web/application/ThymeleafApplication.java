package com.rslakra.thymeleaf.web.application;

import com.rslakra.thymeleaf.web.controller.HomeController;
import com.rslakra.thymeleaf.web.controller.OrderDetailsController;
import com.rslakra.thymeleaf.web.controller.OrderListController;
import com.rslakra.thymeleaf.web.controller.ProductCommentsController;
import com.rslakra.thymeleaf.web.controller.ProductListController;
import com.rslakra.thymeleaf.web.controller.SubscribeController;
import com.rslakra.thymeleaf.web.controller.UserProfileController;
import com.rslakra.thymeleaf.web.controller.thymeleaf.ThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.web.servlet.IServletWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ThymeleafApplication {
    
    private TemplateEngine templateEngine;
    private Map<String, ThymeleafController> controllersByEndpoint;
    private IServletWebApplication webApplication;
    
    /**
     * @param servletContext
     */
    public ThymeleafApplication(final ServletContext servletContext) {
        super();
        this.webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver templateResolver = getWebApplicationTemplateResolver();
        
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        
        this.controllersByEndpoint = new HashMap<String, ThymeleafController>();
        this.controllersByEndpoint.put("/", new HomeController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/product/list", new ProductListController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/product/comments", new ProductCommentsController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/order/list", new OrderListController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/order/details", new OrderDetailsController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/subscribe", new SubscribeController(this.webApplication, servletContext, this.templateEngine));
        this.controllersByEndpoint.put("/userprofile", new UserProfileController(this.webApplication, servletContext, this.templateEngine));
        
    }
    
    /**
     * @return
     */
    private WebApplicationTemplateResolver getWebApplicationTemplateResolver() {
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(this.webApplication);
        
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
     * @return
     */
    public IServletWebApplication getWebApplication() {
        return this.webApplication;
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
