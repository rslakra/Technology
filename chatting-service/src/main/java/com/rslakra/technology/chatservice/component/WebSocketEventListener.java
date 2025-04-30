package com.rslakra.technology.chatservice.component;

import com.rslakra.technology.chatservice.model.ChatMessage;
import com.rslakra.technology.chatservice.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static java.lang.String.format;

/**
 * A listener for WebSocket events that handles connection and disconnection events.
 * The class processes WebSocket connection and disconnection events and
 * performs actions such as logging and broadcasting messages to subscribed clients.
 * <p>
 * It listens to the following events:
 * 1. SessionConnectedEvent - Triggered when a new WebSocket connection is established.
 * 2. SessionDisconnectEvent - Triggered when a WebSocket connection is terminated.
 * <p>
 * Methods:
 * - handleWebSocketConnectListener(SessionConnectedEvent event): Logs a message upon receiving a new connection.
 * - handleWebSocketDisconnectListener(SessionDisconnectEvent event): Handles user disconnection by logging the event,
 * retrieving session details (username and room ID), and sending a LEAVE message to the relevant channel.
 * <p>
 * Dependencies:
 * - SimpMessageSendingOperations: Used to send messages to specific destinations.
 * - Logger: Used for logging information and debug messages.
 * <p>
 * This class is annotated with @Component to enable component scanning, allowing Spring to detect and manage it.
 */
@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    /**
     * Provides operations for sending messages to specific WebSocket destinations.
     * <p>
     * This variable is an instance of {@code SimpMessageSendingOperations}, which is
     * used to facilitate the sending of messages to various WebSocket topics or queues.
     * It enables interaction with WebSocket clients by broadcasting or multicasting
     * messages to specific subscribers. Common use cases include notifying all users
     * in a specific chat room or sending alerts to various channels.
     * <p>
     * This dependency is automatically injected by Spring's dependency injection mechanism
     * through the {@code @Autowired} annotation.
     */
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Handles the WebSocket connection event when a new WebSocket session is established.
     * This method is triggered upon a successful connection to the WebSocket server.
     * It logs the connection event for debugging and informational purposes.
     *
     * @param event the event that contains details about the new WebSocket session connection
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        LOGGER.debug("handleWebSocketConnectListener({})", event);
        LOGGER.info("Received a new web socket connection.");
    }

    /**
     * Handles WebSocket disconnection events.
     * This method is triggered when a WebSocket session is terminated.
     * It logs the disconnection event, retrieves the session attributes such as
     * the username and room identifier, and broadcasts a leave message to the corresponding room
     * notifying other participants about the user's departure.
     *
     * @param event the event that contains details about the WebSocket session disconnection
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LOGGER.debug("+handleWebSocketDisconnectListener({})", event);
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");
        LOGGER.debug("username={}, roomId={}", username, roomId);
        if (username != null) {
            LOGGER.info("User Disconnected={}", username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);
            messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
        }
        LOGGER.debug("-handleWebSocketDisconnectListener()");
    }
}
