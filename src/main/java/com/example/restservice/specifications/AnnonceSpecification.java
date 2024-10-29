package com.example.restservice.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import com.example.restservice.Model.Annonce;
import java.util.stream.Collectors;

public class AnnonceSpecification {

    public static Specification<Annonce> hasAllKeywords(List<String> keywords) {
        return (root, query, cb) -> {
            if (keywords == null || keywords.isEmpty()) {
                return cb.conjunction();
            }
    
            List<Predicate> predicates = keywords.stream()
                    .map(keyword -> cb.isTrue(
                        cb.function("LOWER", String.class, cb.literal(keyword))
                            .in(cb.function("LOWER", String.class, root.join("keywords")))
                    ))
                    .collect(Collectors.toList());
    
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    

    public static Specification<Annonce> hasZone(String zone) {
        return (root, query, cb) -> {
            if (zone == null) {
                return cb.conjunction(); 
            } else {
                return cb.equal(cb.lower(root.get("zone")), zone.toLowerCase()); 
            }
        };
    }

    public static Specification<Annonce> hasEtat(String state) {
        return (root, query, cb) -> {
            if (state == null) {
                return cb.conjunction(); 
            } else {
                return cb.equal(cb.lower(root.get("state")), state.toLowerCase()); 
            }
        };
    }
}
