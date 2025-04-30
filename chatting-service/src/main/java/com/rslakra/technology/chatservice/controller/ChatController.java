package com.rslakra.technology.chatservice.controller;

import com.rslakra.technology.chatservice.model.ChatMessage;
import com.rslakra.technology.chatservice.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.lang.String.format;

/**
 * The ChatController class handles WebSocket message mappings for chat room functionality.
 * It provides endpoints for sending messages and adding users to chat rooms.
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    /**
     * Provides an abstraction for sending messages over WebSocket channels.
     * This component facilitates broadcasting messages to specific destinations
     * and is injected into the class to enable real-time communication.
     */
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    /**
     * Handles sending a chat message to a specific room.
     * The message is broadcast to all subscribers of the designated chat room channel.
     *
     * @param roomId      the unique identifier of the chat room to which the message belongs
     * @param chatMessage the chat message payload containing the details such as type, content, and sender
     */
    @MessageMapping("/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        LOGGER.debug("+sendMessage({}, {})", roomId, chatMessage);
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
        LOGGER.debug("-sendMessage()");
    }

    /**
     * Adds a user to a chat room and notifies the room about the new participant.
     * If the user was previously in another room, a leave message is sent to that room.
     *
     * @param roomId         the unique identifier of the chat room to which the user is being added
     * @param chatMessage    the chat message payload containing the sender's name and other details
     * @param headerAccessor provides access to session attributes for user session tracking
     */
    @MessageMapping("/{roomId}/addUser")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        LOGGER.debug("+addUser({}, {})", roomId, chatMessage);
        String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        LOGGER.debug("currentRoomId={}", currentRoomId);
        if (currentRoomId != null) {
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setType(MessageType.LEAVE);
            leaveMessage.setSender(chatMessage.getSender());
            LOGGER.debug("leaveMessage={}", leaveMessage);
            messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
        }
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
        LOGGER.debug("-addUser()");
    }
}
