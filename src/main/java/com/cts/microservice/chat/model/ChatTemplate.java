package com.cts.microservice.chat.model;

import java.time.LocalDateTime;

public class ChatTemplate {
	private MessageType type;
    private String content;
    private String sender;
    private LocalDateTime dateTime=LocalDateTime.now();;
    private String receiver;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING
    }

    public ChatTemplate() {
        // Empty
    }

    public ChatTemplate(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
