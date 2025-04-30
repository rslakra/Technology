package com.rslakra.technology.chatservice.model;

import lombok.Data;

/**
 * Represents a chat message exchanged within a chat application.
 * <p>
 * This class is used to encapsulate the data for a single chat message,
 * which can be transmitted between the server and clients in a WebSocket-based
 * communication setup. Each message contains a type indicating the nature
 * of the message, the actual content, and the sender's identifier.
 * <p>
 * The `type` field indicates the type of the chat message,
 * which could be either CHAT, JOIN, or LEAVE as defined by the `MessageType` enum.
 * <p>
 * The `content` field holds the textual content of the chat message.
 * <p>
 * The `sender` field contains the identifier (e.g., username) of the user that sent the message.
 */
@Data
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;

}
