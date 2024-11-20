package com.example.restservice.Controller;
/*
import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Service.AnnonceService;
import com.example.restservice.Model.User;
import com.example.restservice.Service.NotificationService;
import com.example.restservice.Service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/annonces")
public class AnnonceViewController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SearchService searchService;

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

    @GetMapping("/{id}")
    public Object getAnnonces(@PathVariable Long id, Model model,Authentication authentication) {
        Annonce annonce = annonceService.findAnnonceById(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  
        }

        model.addAttribute("annonce", annonce);


        return "annonce"; 
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
    @PostMapping
    public Object createAnnonce(@RequestBody Annonce annonce, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        List<Search> searchList =searchService.getSearchesByAnnonce(annonce);
            List<User> users = searchList.stream()
            .map(search -> search.getUser())
            .distinct()
            .collect(Collectors.toList());

        
            System.out.println("Oui");
            System.out.println(users);

            annonceService.createAnnonce(annonce, user);
            
            users.forEach(u -> {
                Notification notif=new Notification();
                notif.setStatus(1);
                notif.setMessage("Vous avez un produit qui correspond à vos recherche");
                if (u.getId() != user.getId() && u.isHasNotification())
                {
                    notificationService.createNotification(notif, u, annonce);
                }
            });

            return "redirect:/annonces";
        }
}
*/