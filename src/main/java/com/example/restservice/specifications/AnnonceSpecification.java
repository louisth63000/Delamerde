package com.example.restservice.specifications;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import com.example.restservice.Model.Annonce;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


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
    

    public static Specification<Annonce> hasZone(String[] zones) {
        return (root, query, cb) -> {
            if (zones == null || zones.length == 0) {
                return cb.conjunction(); 
            } else {
                
                Predicate[] predicates = new Predicate[zones.length];
                for (int i = 0; i < zones.length; i++) {
                    predicates[i] = cb.equal(cb.lower(root.get("zone")), zones[i].toLowerCase());
                }
                
                return cb.or(predicates);
            }
        };
    }
    

    public static Specification<Annonce> hasEtat(String state) {
        return (root, query, cb) -> {
            if (state == null || state=="") {
                return cb.conjunction(); 
            } else {
                return cb.equal(cb.lower(root.get("state")), state.toLowerCase()); 
            }
        };
    }
    public static Specification<Annonce> publishedInLastHours() {
        return (root, query, cb) -> {
            LocalDateTime OneHourAgo = LocalDateTime.now().minusHours(1);
            return cb.greaterThanOrEqualTo(root.get("publicationDate"), OneHourAgo);
        };
    }
    public static Specification<Annonce> publishedInLast5Days() {
        return (root, query, cb) -> {
            LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
            return cb.greaterThanOrEqualTo(root.get("publicationDate"), fiveDaysAgo);
        };
    }
    public static Specification<Annonce> publishedInLast30Days() {
        return (root, query, cb) -> {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            return cb.greaterThanOrEqualTo(root.get("publicationDate"), thirtyDaysAgo);
        };
    }

}
