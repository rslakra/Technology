package com.rslakra.technology.chatservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * A configuration class that sets up WebSocket messaging in the application.
 * This class extends AbstractWebSocketMessageBrokerConfigurer, enabling the
 * application to register WebSocket endpoints and configure the message broker.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    /**
     * Registers STOMP (Simple Text Oriented Message Protocol) endpoints to enable WebSocket communication.
     * This method allows the application to define endpoint URLs that clients can connect to for message exchange.
     *
     * @param registry the {@link StompEndpointRegistry} used to add and configure STOMP endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        LOGGER.debug("registerStompEndpoints({})", registry);
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Configures the message broker for WebSocket messaging in the application.
     * This method sets up application-level message destinations and enables a simple message broker.
     *
     * @param registry the {@link MessageBrokerRegistry} used to configure the message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        LOGGER.debug("+configureMessageBroker({})", registry);
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/channel");
        LOGGER.debug("-configureMessageBroker()");
    }
}
