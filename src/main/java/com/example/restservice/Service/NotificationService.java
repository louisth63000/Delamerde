package com.example.restservice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.NotificationRepository;
import com.example.restservice.specifications.NotificationSpecification;
import com.example.restservice.specifications.SearchSpecification;


@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }
    
    public Notification createNotification(Notification notification,User user,Annonce annonce) {
        notification.setUser(user);  
        notification.setAnnonce(annonce); 
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(User user) {
        
        Specification<Notification> spec = NotificationSpecification.getAllSearchByUser(user);
        return notificationRepository.findAll(spec);
    }
    public Notification findNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    public void changestatus(Notification notification)
    {
        notificationRepository.save(notification);
    }
    
}
