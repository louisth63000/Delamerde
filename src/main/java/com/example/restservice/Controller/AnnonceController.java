package com.example.restservice.Controller;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Service.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

//import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Controller
@RequestMapping("/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    
    @GetMapping
    public Object getAllAnnonces(HttpServletRequest request, Model model) {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        
        if (request.getHeader("Accept") != null && request.getHeader("Accept").contains(MediaType.APPLICATION_JSON_VALUE)) {
            return annonces; 
        }

        model.addAttribute("annonces", annonces);
        return "annonces"; 
    }
    
    @PostMapping
    public String createAnnonce(@RequestBody Annonce annonce) {
        annonceService.createAnnonce(annonce);
        return "redirect:/annonces"; 
    }
    /*@PostMapping
    public ResponseEntity<String> createAnnonce(@RequestBody Annonce annonce) {
        annonceService.createAnnonce(annonce);
        return ResponseEntity.ok("Annonce créée avec succès !");
    }*/
    
   /* @GetMapping("/search")
    public List<Annonce> searchAnnonces(@RequestParam List<String> keywords) {
        return annonceService.searchAnnoncesByKeywords(keywords);
    }*/
}


   


