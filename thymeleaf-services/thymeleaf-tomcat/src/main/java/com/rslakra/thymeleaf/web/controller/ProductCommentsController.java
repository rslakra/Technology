package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Product;
import com.rslakra.thymeleaf.service.ProductService;
import com.rslakra.thymeleaf.web.controller.thymeleaf.AbstractThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductCommentsController extends AbstractThymeleafController {

    public ProductCommentsController(
            final IServletWebApplication webApplication,
            final ServletContext servletContext,
            final ITemplateEngine templateEngine) {
        super(webApplication, servletContext, templateEngine);
    }

    @Override
    protected void handleTemplate(
            final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse,
            final WebContext webContext) throws Exception {

        final Long prodId = Long.valueOf(servletRequest.getParameter("prodId"));

        final ProductService productService = new ProductService();
        final Product product = productService.findById(prodId);

        webContext.setVariable("prod", product);
        this.templateEngine.process("product/comments", webContext, servletResponse.getWriter());
    }

}
