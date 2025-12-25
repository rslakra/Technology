package com.rslakra.thymeleaf.web.filter;

import com.rslakra.thymeleaf.persistence.entities.User;
import com.rslakra.thymeleaf.web.application.ThymeleafApplication;
import com.rslakra.thymeleaf.web.controller.thymeleaf.ThymeleafController;
import org.thymeleaf.ITemplateEngine;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            // Get the request path without context path (similar to ThymeleafApplication.getRequestPath)
            String requestURI = request.getRequestURI();
            final String contextPath = request.getContextPath();
            
            // Remove context path if present
            if (requestURI.startsWith(contextPath)) {
                requestURI = requestURI.substring(contextPath.length());
            }
            
            // Remove any path parameters (e.g., ;jsessionid=...)
            final int fragmentIndex = requestURI.indexOf(';');
            if (fragmentIndex != -1) {
                requestURI = requestURI.substring(0, fragmentIndex);
            }
            
            // This prevents triggering engine executions for resource URLs
            if (requestURI.startsWith("/css") ||
                    requestURI.startsWith("/images") ||
                    requestURI.startsWith("/favicon") ||
                    requestURI.startsWith("/js") ||
                    requestURI.endsWith(".css") ||
                    requestURI.endsWith(".js") ||
                    requestURI.endsWith(".png") ||
                    requestURI.endsWith(".jpg") ||
                    requestURI.endsWith(".jpeg") ||
                    requestURI.endsWith(".gif") ||
                    requestURI.endsWith(".ico") ||
                    requestURI.endsWith(".svg") ||
                    requestURI.endsWith(".woff") ||
                    requestURI.endsWith(".woff2") ||
                    requestURI.endsWith(".ttf") ||
                    requestURI.endsWith(".eot")) {
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
            controller.process(request, response);
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
