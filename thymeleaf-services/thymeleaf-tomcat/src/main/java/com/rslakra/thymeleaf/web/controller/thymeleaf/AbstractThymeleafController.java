package com.rslakra.thymeleaf.web.controller.thymeleaf;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractThymeleafController implements ThymeleafController {
    
    protected final IServletWebApplication webApplication;
    protected final ServletContext servletContext;
    protected final ITemplateEngine templateEngine;
    
    /**
     * Constructor that accepts all dependencies.
     * 
     * @param webApplication The servlet web application instance
     * @param servletContext The servlet context
     * @param templateEngine The template engine instance
     */
    protected AbstractThymeleafController(
            final IServletWebApplication webApplication,
            final ServletContext servletContext,
            final ITemplateEngine templateEngine) {
        this.webApplication = webApplication;
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
    }
    
    /**
     * Process the request using the provided parameters.
     * This method handles the common setup and delegates to handleTemplate for specific logic.
     * This method is final to prevent overriding - concrete controllers should implement handleTemplate instead.
     * 
     * @param servletRequest
     * @param servletResponse
     * @throws Exception
     */
    @Override
    public final void process(
            final HttpServletRequest servletRequest, 
            final HttpServletResponse servletResponse) throws Exception {
        
        // Create WebContext using the webApplication from constructor
        // Cast to concrete type to access buildExchange method
        final IServletWebExchange webExchange = ((JakartaServletWebApplication) this.webApplication).buildExchange(servletRequest, servletResponse);
        final WebContext webContext = new WebContext(webExchange, servletRequest.getLocale());
        
        // Delegate to the abstract method for specific controller logic
        handleTemplate(servletRequest, servletResponse, webContext);
    }
    
    /**
     * Handle the template processing with the prepared WebContext.
     * Concrete controllers implement this method to add their specific logic.
     * 
     * @param servletRequest
     * @param servletResponse
     * @param webContext The WebContext already created with the webExchange
     * @throws Exception
     */
    protected abstract void handleTemplate(
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse,
            final WebContext webContext) throws Exception;
    
}
