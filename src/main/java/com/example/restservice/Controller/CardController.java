package com.example.restservice.Controller;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Card;
import com.example.restservice.Model.User;
import com.example.restservice.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService cardService;


    @GetMapping
    public String showCardConnectedUser(Model model, RedirectAttributes redirectAttributes) {
        // Récupérer l'authentification de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est authentifié
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
        }

        // Obtenir le nom d'utilisateur (username) de l'utilisateur connecté
        String username = authentication.getName();


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // Vérifier si l'utilisateur a un panier et le récupérer
        Card card = cardService.getPanierByUserId(user.getId());

        // Si un panier est disponible, on affiche la page avec les détails du panier
        if (card != null) {
            model.addAttribute("panier", card);
            model.addAttribute("user_name", user.getUsername());
            return "panier";
            //redirectAttributes.addAttribute("userId", user.getId());
        } else {
            // Sinon, rediriger vers "/panier/{userId}"
            redirectAttributes.addAttribute("userId", user.getId());
            return "redirect:/panier/{userId}";
        }
    }
    @GetMapping("/{userId}")
    public String showCard(@PathVariable Long userId, Model model) {
        // Récupère le panier de l'utilisateur via le service
        Card card = cardService.getPanierByUserId(userId);

        // Ajoute le panier au modèle pour être accessible dans le template Thymeleaf
        model.addAttribute("panier", card);

        // Retourne le nom du template Thymeleaf à afficher
        return "panier";
    }

    /*@PostMapping("/ajouter-lot")
    public String ajouterLot(@PathVariable Long userId, @RequestBody Long annonceId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        panierService.ajouterAnnonceAuPanier(userId, annonceId);
        return "redirect:/panier/" + userId; // Redirige vers la page du panier de l'utilisateur
    }*/

    @PostMapping("/")
    public String AddAnnonceinCard(@RequestParam Long annonceId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Vérifier si l'utilisateur est authentifié
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // Ajouter l'annonce au panier de l'utilisateur
        cardService.removeAnnonceFromCard(user.getId(), annonceId);

        return "redirect:/panier"; // Redirige vers la page du panier
    }
}


