package com.example.restservice.Service;

import com.example.restservice.Model.Annonce;
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
    public Annonce createAnnonce(Annonce annonce) {
        return annonceRepository.save(annonce);
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
        if(date == "1hour"){
            spec =spec.and(AnnonceSpecification.publishedInLastHours());
        }
        if(date == "5days"){
            spec =spec.and(AnnonceSpecification.publishedInLast5Days());
        }
        if(date == "5days"){
            spec =spec.and(AnnonceSpecification.publishedInLast30Days());
        }
        return annonceRepository.findAll(spec);             
        //return annonceRepository.searchAnnonces(zone,state,keywords);
    }

}

