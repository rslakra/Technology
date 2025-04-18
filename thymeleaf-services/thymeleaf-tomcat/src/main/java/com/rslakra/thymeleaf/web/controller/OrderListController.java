package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Order;
import com.rslakra.thymeleaf.service.OrderService;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderListController implements ThymeleafController {

    public OrderListController() {
        super();
    }

    public void process(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
        throws Exception {

        final OrderService orderService = new OrderService();
        final List<Order> allOrders = orderService.findAll();

        final WebContext ctx = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale());
        ctx.setVariable("orders", allOrders);

        templateEngine.process("order/list", ctx, servletResponse.getWriter());

    }

}
