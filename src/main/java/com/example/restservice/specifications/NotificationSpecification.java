package com.example.restservice.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Notification;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class NotificationSpecification {

    public static Specification<Notification> getAllSearchByUser(User user) {
        return (root, query, criteriaBuilder) -> {
            // Create a subquery to select the IDs of the notifications for the user
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Notification> subRoot = subquery.from(Notification.class);
            subquery.select(subRoot.get("id"))
                    .where(criteriaBuilder.equal(subRoot.get("user"), user));
    
            // Add the subquery to the main query to limit the number of results to 10
            query.where(root.get("id").in(subquery));
            query.orderBy(criteriaBuilder.desc(root.get("id")));
    
            return criteriaBuilder.equal(root.get("user"), user);
        };
    }
    
}
