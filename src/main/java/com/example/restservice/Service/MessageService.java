package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        PageRequest pageRequest = PageRequest.of(0, 10,Sort.by(Sort.Direction.DESC,"id"));
        List<Message> messages = messageRepository.findByFromOrTo(user, user,pageRequest).getContent();
        return messages.stream()
                .flatMap(message -> List.of(message.getFrom(), message.getTo()).stream())
                .filter(u -> !u.getId().equals(user.getId()))
                .distinct()
                .collect(Collectors.toList());
    }
}
