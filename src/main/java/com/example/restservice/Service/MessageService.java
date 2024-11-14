package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Message;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.MessageRepository;


@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    public Message createMessage(Message message,User from,User to) {
        message.setFrom(from);  
        message.setTo(to); 
        return messageRepository.save(message);
    }

}
