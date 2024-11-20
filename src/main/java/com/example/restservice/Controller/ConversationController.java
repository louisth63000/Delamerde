package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Message;
import com.example.restservice.Model.User;
import com.example.restservice.Service.CustomUserDetailsService;
import com.example.restservice.Service.MessageService;




@Controller
@RequestMapping("/conversations")
public class ConversationController {
    
    @Autowired
    private  MessageService messageservice;

    @Autowired
    private  CustomUserDetailsService userservice;

    @GetMapping("/{id}")
    public Object getAllAnnonces(@PathVariable Long id,Model model,Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        User user2 = userservice.getUserByid( id);

        if (user2 == null || user2.getId() == user.getId())
        {
            return "redirect:/";  
        }

        List<Message> messages = messageservice.findMessagesBetweenUsers(user,user2);
        // List<Annonce> annonces = annonceService.getAllAnnonces();


        // if (request.getHeader("Accept") != null && request.getHeader("Accept").contains(MediaType.APPLICATION_JSON_VALUE)) {
        //     return annonces; 
        // }
        model.addAttribute("messages",messages);
        model.addAttribute("id", id);


        return "conversation"; 
    }
    
    @PostMapping("/messages/create")
    public String sendMessage(@RequestParam String id,@RequestParam String message,Authentication authentication, RedirectAttributes redirectAttributes) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  
        }

        Long conversationId = Long.parseLong(id);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        User user2 = userservice.getUserByid( conversationId);

        if (user2 == null || user2.getId() == user.getId())
        {
            return "redirect:/";  
        }

        Message message_Message= new Message();

        message_Message.setMessage(message);

        messageservice.createMessage(message_Message,user, user2);

        // Traitez l'envoi du message ici
        // Par exemple, enregistrez le message dans la base de données ou envoyez-le par email

        // Ajoutez un message de confirmation à la vue
        redirectAttributes.addFlashAttribute("confirmation", "Message sent successfully!");

        return "redirect:/conversations/" + id;
    }


}
