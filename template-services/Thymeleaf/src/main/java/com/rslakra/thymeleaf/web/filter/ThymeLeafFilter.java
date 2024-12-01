package com.rslakra.thymeleaf.web.filter;

import com.rslakra.thymeleaf.persistence.entities.User;
import com.rslakra.thymeleaf.web.application.ThymeleafApplication;
import com.rslakra.thymeleaf.web.controller.ThymeleafController;
import org.thymeleaf.ITemplateEngine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThymeleafFilter implements Filter {
    
    private ServletContext servletContext;
    private ThymeleafApplication application;
    
    public ThymeleafFilter() {
        super();
    }
    
    /**
     * @param request
     */
    private static void addUserToSession(final HttpServletRequest request) {
        // Simulate a real user session by adding a user object
        request.getSession(true)
                .setAttribute("user", new User("Rohtash", "Lakra", "Indian", null));
    }
    
    /**
     * @param filterConfig
     * @throws ServletException
     */
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.application = new ThymeleafApplication(this.servletContext);
    }
    
    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        addUserToSession((HttpServletRequest) request);
        if (!process((HttpServletRequest) request, (HttpServletResponse) response)) {
            chain.doFilter(request, response);
        }
    }
    
    public void destroy() {
        // nothing to do
    }
    
    
    /**
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private boolean process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        
        try {
            // This prevents triggering engine executions for resource URLs
            if (request.getRequestURI().startsWith("/css") ||
                    request.getRequestURI().startsWith("/images") ||
                    request.getRequestURI().startsWith("/favicon")) {
                return false;
            }
            
            /*
             * Query controller/URL mapping and obtain the controller
             * that will process the request. If no controller is available,
             * return false and let other filters/servlets process the request.
             */
            ThymeleafController controller = this.application.resolveControllerForRequest(request);
            if (controller == null) {
                return false;
            }
            
            /*
             * Obtain the TemplateEngine instance.
             */
            ITemplateEngine templateEngine = this.application.getTemplateEngine();
            
            /*
             * Write the response headers
             */
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            
            /*
             * Execute the controller and process view template,
             * writing the results to the response writer.
             */
            controller.process(request, response, this.servletContext, templateEngine);
            return true;
        } catch (Exception ex) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException ignored) {
                // Just ignore this
            }
            throw new ServletException(ex);
        }
    }
}
