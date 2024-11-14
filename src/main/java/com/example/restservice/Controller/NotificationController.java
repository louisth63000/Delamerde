package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @PutMapping("/{id}/status")
    public ResponseEntity<String> index(@PathVariable Long id, Model model,Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Notification notification= notificationService.findNotificationById(id);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();


        if (notification == null || !notification.getUser().getId().equals(currentUser.getId()) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to change this notification");
        }
        
        notification.setStatus(0);
        notificationService.changestatus(notification);
        return ResponseEntity.ok("La notification a changé ");
    }
    

}
