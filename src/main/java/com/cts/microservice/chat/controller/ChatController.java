package com.cts.microservice.chat.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.cts.microservice.chat.model.ChatTemplate;
import com.cts.microservice.chat.model.OutputMessage;


@Controller
public class ChatController {
	
	private Logger LOG= Logger.getLogger(this.getClass());
	
    protected static ArrayList<String> userList = new ArrayList<String>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/queue/specific-user")
    public String sendSpecific(@Payload ChatTemplate msg) throws Exception {
            String time = LocalDateTime.now().toString();
            OutputMessage out = new OutputMessage(msg.getSender(), msg.getContent(), time, false);
//            System.out.println("receiver " + msg.getReceiver());
            LOG.debug("receiver "+msg.getReceiver());
            simpMessagingTemplate.convertAndSendToUser(msg.getReceiver(), "/app/user/queue/specific-user", out);
            return "Received";
    }

    public void addUser(String user) {
        userList.add(user);
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public static void removeUser(String user) {
        userList.remove(user);
    }
    @MessageMapping("/getUserlist")  // For send, append app/
    @SendTo("/topic/getUser") // For Subscribe
    public Object[] getUser(@Payload String username) {
        if(!userList.contains(username)) {
            userList.add(username);
        }
//        System.out.println("Adding user in list: "+username);
        LOG.debug("Adding user in list: "+username);
        return userList.toArray();
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatTemplate sendMessage(@Payload ChatTemplate chatMessage) {
//        System.out.println("receiver /sendMessage" + chatMessage.getContent());
    	LOG.debug("receiver /sendMessage "+chatMessage.getContent());
        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatTemplate addUser(@Payload ChatTemplate chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        System.out.println(headerAccessor.getSubscriptionId()+"####"+chatMessage.getSender());
        LOG.debug(headerAccessor.getSubscriptionId()+"####"+chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/postStatus")
    @SendTo("/topic/status")
    public boolean check(@Payload boolean status) {
//        System.out.println("Received status post");
    	LOG.debug("Received status post");
        return true;
    }
    
}


