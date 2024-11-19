package com.example.restservice.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.SearchRepository;
import com.example.restservice.specifications.SearchSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SearchService  {
    
    @Autowired
    private SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository){
        this.searchRepository=searchRepository;
    }

    public Search saveSearch(Search search,User user) {
        search.setUser(user);  
        return searchRepository.save(search);
    }
    public Search getSearch(long id) {
        Optional<Search> optionalSearch = searchRepository.findById(id);
        if (optionalSearch.isPresent()) {
            return optionalSearch.get();
        } 
            return null;
    }
   public Page<Search> getSearchesByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Search> spec = SearchSpecification.getAllSearchByUser(user);
        return searchRepository.findAll(spec, pageable);
    }
    public List<Search> getSearchesByAnnonce(Annonce annonce) {
        
        Specification<Search> spec = SearchSpecification.getAllSearchByAnnonce(annonce);
        return searchRepository.findAll(spec);
    }
    public void deleteSearch(long id) {
        
        searchRepository.deleteById(id);
    }
}
