package com.cts.microservice.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.cts.microservice.chat.model.ChatTemplate;
import com.cts.microservice.chat.model.ChatTemplate.MessageType;

@Component
public class EventListner {
	
	@Autowired
    private SimpMessageSendingOperations messagingTemplate;
	
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }
	
	@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        ChatController.userList.remove(username);

        if(username != null) {
        	System.out.println("User Disconnected : " + username);

            ChatTemplate chat = new ChatTemplate();
            chat.setType(MessageType.LEAVE);
            chat.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chat);
        }
    }

}
