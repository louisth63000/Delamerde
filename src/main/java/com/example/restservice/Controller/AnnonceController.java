package com.example.restservice.Controller;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Service.AnnonceService;
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
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private AnnonceRepository annonceRepository;

    // Récupérer toutes les annonces
    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }

    // Récupérer les annonces de l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<List<Annonce>> getMyAnnonces(Authentication authentication) {
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

        Annonce createdAnnonce = annonceService.createAnnonce(annonce, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnnonce);
    }

    // Supprimer une annonce
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

    // Recherche des annonces
    @GetMapping("/search")
    public ResponseEntity<List<Annonce>> searchAnnonces(
            @RequestParam(required = false) String[] zone,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false) String date) {
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
