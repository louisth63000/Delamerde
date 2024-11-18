package com.example.restservice.Controller;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Service.AnnonceService;
import com.example.restservice.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/annonces")
public class AnnonceViewController {

    @Autowired
    private AnnonceService annonceService;

    @GetMapping
    public String afficherToutesLesAnnonces(Model model) {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        model.addAttribute("annonces", annonces);
        return "annonces"; // Vue Thymeleaf
    }

    @GetMapping("/mesannonces")
    @Transactional
    public String afficherMesAnnonces(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        List<Annonce> annonces = annonceService.findAnnoncesByUser(user);
        user.setAnnonces(annonces);
        model.addAttribute("annonces", annonces);
        model.addAttribute("user", user);
        return "mesannonces";
    }
    @GetMapping("/search")
    public String searchAnnonces(
        @RequestParam(required = false) String[] zone,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) List<String> keywords,
        @RequestParam(required = false) String date,
        Model model) {
        List<Annonce> annonces = annonceService.searchAnnonces(zone, state, keywords,date);
        model.addAttribute("annonces", annonces);
        return "searchAnnonce"; 
    }
}
