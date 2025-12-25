package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Product;
import com.rslakra.thymeleaf.service.ProductService;
import com.rslakra.thymeleaf.web.controller.thymeleaf.AbstractThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;

import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductListController extends AbstractThymeleafController {

    public ProductListController(
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

        final ProductService productService = new ProductService();
        final List<Product> allProducts = productService.findAll();

        webContext.setVariable("prods", allProducts);
        this.templateEngine.process("product/list", webContext, servletResponse.getWriter());
    }

}
