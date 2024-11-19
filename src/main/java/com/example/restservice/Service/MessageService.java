package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Message;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.MessageRepository;
import com.example.restservice.specifications.MessageSpecification;
import com.example.restservice.specifications.NotificationSpecification;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<Message> findMessagesBetweenUsers(User user1, User user2) {

        Specification<Message> spec = MessageSpecification.hasUsers(user1, user2);

        return messageRepository.findAll(spec);
    }
  public List<User> findUsersCommunicatedWith(User user) {
        Specification<Message> spec = MessageSpecification.hasCommunicatedWith(user);
        List<Message> messages = messageRepository.findAll(spec);

        return messages.stream()
        .flatMap(message -> Stream.of(message.getFrom(), message.getTo()))
        .filter(u -> !u.equals(user))
        .distinct()
        .sorted((u1, u2) -> {
            Message latestMessage1 = messages.stream()
                    .filter(m -> m.getFrom().equals(u1) || m.getTo().equals(u1))
                    .max(Comparator.comparing(Message::getId))
                    .orElse(null);
            Message latestMessage2 = messages.stream()
                    .filter(m -> m.getFrom().equals(u2) || m.getTo().equals(u2))
                    .max(Comparator.comparing(Message::getId))
                    .orElse(null);
            return latestMessage2.getId().compareTo(latestMessage1.getId());
        })
        .limit(10)
        .collect(Collectors.toList());
    }
}
