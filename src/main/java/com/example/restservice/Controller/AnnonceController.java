package com.example.restservice.Controller;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Service.AnnonceService;
import com.example.restservice.Repository.AnnonceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;



@Controller
@RequestMapping("/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private AnnonceRepository annonceRepository;
    
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
    @GetMapping("/api/keywords")
    public ResponseEntity<Set<String>> getKeywords() {

        List<Annonce> annonces = annonceRepository.findAll();
        Set<String> keywords = annonces.stream()
                .flatMap(annonce -> annonce.getKeywords().stream()) 
                .collect(Collectors.toSet()); 
        return ResponseEntity.ok(keywords);
    }
}


   


