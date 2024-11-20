package com.example.restservice.Controller;

import com.example.restservice.DTO.UserRegistrationDTO;
import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import org.springframework.ui.Model;
import com.example.restservice.Service.AnnonceService;
import com.example.restservice.Service.NotificationService;
import com.example.restservice.Service.SearchService;

import jakarta.servlet.http.HttpServletRequest;

import com.example.restservice.Repository.AnnonceRepository;
import com.example.restservice.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private AnnonceRepository annonceRepository;

    
    @GetMapping("/{id}")
    public Object getAnnonces(@PathVariable Long id, Model model,Authentication authentication) {
        Annonce annonce = annonceService.findAnnonceById(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  
        }

        model.addAttribute("annonce", annonce);


        return ResponseEntity.ok(annonce);
    }

    
    

    // Récupérer toutes les annonces
    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }
    
    // Récupérer les annonces de l'utilisateur connecté
    @GetMapping("/mesannonces")
    @Transactional
    public ResponseEntity<Object> getMyAnnonces(HttpServletRequest request,Model model,Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
        }
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<Annonce> annonces = annonceService.findAnnoncesByUser(user);
        return ResponseEntity.ok(annonces);
        
    }

    // Créer une annonce
    @PostMapping
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce, Authentication authentication) {
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

        Annonce createdAnnonce= annonceService.createAnnonce(annonce, user);
        
        users.forEach(u -> {
            Notification notif=new Notification();
            notif.setStatus(1);
            notif.setMessage("Vous avez un produit qui correspond à vos recherche");
            if (u.getId() != user.getId() && u.isHasNotification())
            {
                notificationService.createNotification(notif, u, annonce);
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnnonce);
    }
    @DeleteMapping("search/{id}")
    public ResponseEntity<String> deleteSearch(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non");
        }

        // Récupérer l'utilisateur connecté
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        Search search=searchService.getSearch(id);
        if (search == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non");
        // Vérifier si l'utilisateur connecté est bien le créateur de l'annonce
        if (!search.getUser().getId().equals(currentUser.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this annonce");
        }
        searchService.deleteSearch(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Supprimer");
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnonce(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Annonce annonce = annonceService.findAnnonceById(id);
        if (annonce == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Annonce not found");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        if (!annonce.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this annonce");
        }

        annonceService.deleteAnnonce(id);
        return ResponseEntity.ok("Annonce deleted successfully");
    }
    
    
    @PostMapping("/search")
    public ResponseEntity<String> saveSearch(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) List<String> zone,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false) String date,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();

        // Traitez les données reçues
        System.out.println("State: " + state);
        System.out.println("Zone: " + zone);
        System.out.println("Keywords: " + keywords);
        System.out.println("Date: " + date);

        Search search= new Search(currentUser, zone, keywords, state, date);
        searchService.saveSearch(search, currentUser);

        // Retournez une réponse appropriée
        return ResponseEntity.status(200).body("Enregistrement Réussi");
    }
   

    

    // Recherche des annonces
    @GetMapping("/search")
    public ResponseEntity<Object> searchAnnonces(
            @RequestParam(required = false) String[] zone,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false) String date,
            Model model,
            HttpServletRequest request) {
        List<Annonce> annonces = annonceService.searchAnnonces(zone, state, keywords, date);
        
        return ResponseEntity.ok(annonces);
        
        
    }
    
    // Récupérer les mots-clés
    @GetMapping("/keywords")
    public ResponseEntity<Set<String>> getKeywords() {
        List<Annonce> annonces = annonceRepository.findAll();
        Set<String> keywords = annonces.stream()
                .flatMap(annonce -> annonce.getKeywords().stream())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(keywords);
    }
}
