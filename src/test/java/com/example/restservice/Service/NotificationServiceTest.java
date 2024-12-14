package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.NotificationRepository;
import com.example.restservice.specifications.NotificationSpecification;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNotification() {
        User user = new User();
        user.setId(1L);

        Annonce annonce = new Annonce();
        annonce.setId(1L);

        Notification notification = new Notification(user, annonce, 1, "Test message");

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification createdNotification = notificationService.createNotification(notification, user, annonce);

        assertThat(createdNotification).isNotNull();
        assertThat(createdNotification.getUser()).isEqualTo(user);
        assertThat(createdNotification.getAnnonce()).isEqualTo(annonce);
        assertThat(createdNotification.getMessage()).isEqualTo("Test message");
    }

    @Test
    void testGetNotificationsByUser() {
        User user = new User();
        user.setId(1L);

        Notification notification1 = new Notification(user, new Annonce(), 1, "Message 1");
        Notification notification2 = new Notification(user, new Annonce(), 1, "Message 2");

        Specification<Notification> spec = NotificationSpecification.getAllSearchByUser(user);
        when(notificationRepository.findAll(any(Specification.class))).thenReturn(List.of(notification1, notification2));

        List<Notification> notifications = notificationService.getNotificationsByUser(user);

        assertThat(notifications).isNotNull();
        assertThat(notifications).hasSize(2);
        assertThat(notifications).containsExactlyInAnyOrder(notification1, notification2);
    }

    @Test
    void testFindNotificationById() {
        Notification notification = new Notification();
        notification.setId(1L);

        when(notificationRepository.findById(any(Long.class))).thenReturn(Optional.of(notification));

        Notification foundNotification = notificationService.findNotificationById(1L);

        assertThat(foundNotification).isNotNull();
        assertThat(foundNotification.getId()).isEqualTo(1L);
    }

    @Test
    void testChangeStatus() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setStatus(0);

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notification.setStatus(1);
        notificationService.changestatus(notification);

        assertThat(notification.getStatus()).isEqualTo(1);
    }
}