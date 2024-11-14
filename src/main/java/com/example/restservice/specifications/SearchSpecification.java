package com.example.restservice.specifications;



import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;



public class SearchSpecification {

    public static Specification<Search> getAllSearchByUser(User user) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("user"), user);
    }
    
    public static Specification<Search> getAllSearchByAnnonce(Annonce annonce) {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("state"), annonce.getState());
            Predicate predicate2 = cb.isMember(annonce.getZone(), root.get("zone"));
    
            List<String> keywords = annonce.getKeywords();
            List<Predicate> keywordPredicates = keywords.stream()
                .map((String keyword) -> cb.isTrue(
                    cb.function("LOWER", String.class, cb.literal(keyword))
                    .in(cb.function("LOWER", String.class, root.join("keywords")))
                ))
                .collect(Collectors.toList());
    
            // Combine all predicates using the and operator
            Predicate finalPredicate = cb.and(predicate, predicate2);
            for (Predicate keywordPredicate : keywordPredicates) {
                finalPredicate = cb.and(finalPredicate, keywordPredicate);
            }
    
            return finalPredicate;
        };
    }
    
    
}
