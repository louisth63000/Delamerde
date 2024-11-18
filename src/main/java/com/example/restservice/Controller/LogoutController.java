package com.example.restservice.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            // Supprime l'authentification en cours
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        // Redirige vers la page d'accueil après déconnexion
        return "redirect:/login?logout"; 
    }
}
