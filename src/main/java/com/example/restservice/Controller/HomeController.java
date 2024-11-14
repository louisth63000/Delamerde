package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Service.NotificationService;
import com.example.restservice.Service.SearchService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class HomeController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private NotificationService notificationService; 

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        
        List<Search> searchs = searchService.getSearchesByUser(currentUser);
        List<Notification> notifs = notificationService.getNotificationsByUser(currentUser);

        model.addAttribute("notifications",notifs);
        model.addAttribute("searchs",searchs);
        return "index"; 
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }
}
