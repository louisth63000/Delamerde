package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.example.restservice.Model.Message;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.MessageRepository;
import com.example.restservice.specifications.MessageSpecification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class MessageServiceTest {

    @Autowired
    private MessageRepository messageRepository;

    @MockBean
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageService = new MessageService(messageRepository);
    }

    @Test
    void testCreateMessage() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        Message message = new Message(user1, user2, "Hello!");

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message createdMessage = messageService.createMessage(message, user1, user2);

        assertThat(createdMessage).isNotNull();
        assertThat(createdMessage.getFrom()).isEqualTo(user1);
        assertThat(createdMessage.getTo()).isEqualTo(user2);
        assertThat(createdMessage.getMessage()).isEqualTo("Hello!");
    }

    @Test
    void testFindMessagesBetweenUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        Message message1 = new Message(user1, user2, "Hello!");
        Message message2 = new Message(user2, user1, "Hi!");

        Specification<Message> spec = MessageSpecification.hasUsers(user1, user2);
        when(messageRepository.findAll(any(Specification.class))).thenReturn(List.of(message1, message2));

        List<Message> messages = messageService.findMessagesBetweenUsers(user1, user2);

        assertThat(messages).hasSize(2);
        assertThat(messages).contains(message1, message2);
    }

    @Test
    void testFindUsersCommunicatedWith() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        User user3 = new User();
        user3.setUsername("user3");

        Message message1 = new Message(user1, user2, "Hello!");
        Message message2 = new Message(user1, user3, "Hi!");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(List.of(message1, message2), pageable, 2);

        when(messageRepository.findByFromOrTo(any(User.class), any(User.class), any(Pageable.class))).thenReturn(messagePage);

        List<User> users = messageService.findUsersCommunicatedWith(user1);

        assertThat(users).hasSize(2);
        assertThat(users).contains(user2, user3);
    }




}
