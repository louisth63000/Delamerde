package com.example.restservice.specifications;



import org.springframework.data.jpa.domain.Specification;


import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;



public class SearchSpecification {

    public static Specification<Search> getAllSearchByUser(User user) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("user"), user);
    }
    
}
