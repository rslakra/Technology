package com.rslakra.thymeleaf.web.controller;

import com.rslakra.thymeleaf.persistence.entities.Order;
import com.rslakra.thymeleaf.service.OrderService;
import com.rslakra.thymeleaf.web.controller.thymeleaf.AbstractThymeleafController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OrderDetailsController extends AbstractThymeleafController {

    public OrderDetailsController(
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

        final Long orderId = Long.valueOf(servletRequest.getParameter("orderId"));

        final OrderService orderService = new OrderService();
        final Order order = orderService.findById(orderId);

        webContext.setVariable("order", order);
        this.templateEngine.process("order/details", webContext, servletResponse.getWriter());
    }

}
