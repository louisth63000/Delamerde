package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import com.example.restservice.Model.Message;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.MessageRepository;
import com.example.restservice.Service.MessageService;
import com.example.restservice.specifications.MessageSpecification;

@SpringBootTest
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testCreateMessage() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
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
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        Message message1 = new Message(user1, user2, "Hello!");
        Message message2 = new Message(user2, user1, "Hi!");

        Specification<Message> spec = MessageSpecification.hasUsers(user1, user2);
        when(messageRepository.findAll(any(Specification.class))).thenReturn(List.of(message1, message2));

        List<Message> messages = messageService.findMessagesBetweenUsers(user1, user2);

        assertThat(messages).isNotNull();
        assertThat(messages).hasSize(2);
        assertThat(messages).containsExactlyInAnyOrder(message1, message2);
    }
}
