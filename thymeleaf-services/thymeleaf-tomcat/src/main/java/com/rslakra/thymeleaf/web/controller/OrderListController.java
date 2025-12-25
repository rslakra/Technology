package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Order;
import com.rslakra.thymeleaf.service.OrderService;
import com.rslakra.thymeleaf.web.controller.thymeleaf.AbstractThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;

import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OrderListController extends AbstractThymeleafController {

    public OrderListController(
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

        final OrderService orderService = new OrderService();
        final List<Order> allOrders = orderService.findAll();

        webContext.setVariable("orders", allOrders);
        this.templateEngine.process("order/list", webContext, servletResponse.getWriter());
    }

}
