package com.example.restservice.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.User;
import com.example.restservice.specifications.AnnonceSpecification;
import com.example.restservice.Repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


//import org.springframework.data.jpa.domain.Specification;
import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public AnnonceService(AnnonceRepository annonceRepository){
        this.annonceRepository=annonceRepository;
    }
    public Annonce createAnnonce(Annonce annonce, User user) {
        annonce.setUser(user);  
        return annonceRepository.save(annonce);
    }
    public Annonce findAnnonceById(Long id) {
        return annonceRepository.findById(id).orElse(null);
    }

    public List<Annonce> findAnnoncesByUser(User user) {
        return annonceRepository.findByUser(user);
    }
    
    public void deleteAnnonce(Long id) {
        annonceRepository.deleteById(id);
    }
    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }
    public List<Annonce> searchAnnonces(String[] zone, String state, List<String> keywords, String date) {
        Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasZone(zone))
                                                   .and(AnnonceSpecification.hasEtat(state));

        if (keywords != null && !keywords.isEmpty()) {
            spec = spec.and(AnnonceSpecification.hasAllKeywords(keywords));
        }
        if("1hour".equals(date)){
            
            spec =spec.and(AnnonceSpecification.publishedInLastHours());
        }
        if("5days".equals(date)){
            spec =spec.and(AnnonceSpecification.publishedInLast5Days());
        }
        if("30days".equals(date)){
            spec =spec.and(AnnonceSpecification.publishedInLast30Days());
        }
        return annonceRepository.findAll(spec);             
        //return annonceRepository.searchAnnonces(zone,state,keywords);
    }

}

