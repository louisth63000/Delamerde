package com.example.restservice.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public Annonce createAnnonce(Annonce annonce) {
        return annonceRepository.save(annonce);
    }

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }
    /*public List<Annonce> searchAnnoncesByKeywords(List<String> keywords) {
        return annonceRepository.findByKeywords(keywords);
    } */
}

