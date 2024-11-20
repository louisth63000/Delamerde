package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Service.MessageService;
import com.example.restservice.Service.NotificationService;
import com.example.restservice.Service.SearchService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private MessageService messageService; 

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model, Authentication authentication  ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
            

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        
        Page<Search> searchPage = searchService.getSearchesByUser(currentUser, 1, 10);
        List<Notification> notifs = notificationService.getNotificationsByUser(currentUser);
        List<User> users = messageService.findUsersCommunicatedWith(currentUser);


        model.addAttribute("users", users);
        model.addAttribute("notifications", notifs);
        model.addAttribute("searchs", searchPage.getContent());
        model.addAttribute("user", currentUser);
    


        return "index"; 
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }
}
