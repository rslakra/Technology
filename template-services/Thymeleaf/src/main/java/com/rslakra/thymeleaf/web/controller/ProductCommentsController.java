package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Product;
import com.rslakra.thymeleaf.service.ProductService;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductCommentsController implements ThymeleafController {

    public ProductCommentsController() {
        super();
    }

    public void process(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
        throws Exception {

        final Long prodId = Long.valueOf(servletRequest.getParameter("prodId"));

        final ProductService productService = new ProductService();
        final Product product = productService.findById(prodId);

        final WebContext ctx = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale());
        ctx.setVariable("prod", product);

        templateEngine.process("product/comments", ctx, servletResponse.getWriter());

    }

}
