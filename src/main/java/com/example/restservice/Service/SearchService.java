package com.example.restservice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.SearchRepository;
import com.example.restservice.specifications.SearchSpecification;

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
    public List<Search> getSearchesByUser(User user) {
        
        Specification<Search> spec = SearchSpecification.getAllSearchByUser(user);
        return searchRepository.findAll(spec);
    }
}
