package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Order;
import com.rslakra.thymeleaf.service.OrderService;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderDetailsController implements ThymeleafController {

    public OrderDetailsController() {
        super();
    }

    public void process(
            final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
        throws Exception {

        final Long orderId = Long.valueOf(servletRequest.getParameter("orderId"));

        final OrderService orderService = new OrderService();
        final Order order = orderService.findById(orderId);

        final WebContext ctx = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale());
        ctx.setVariable("order", order);

        templateEngine.process("order/details", ctx, servletResponse.getWriter());

    }

}
