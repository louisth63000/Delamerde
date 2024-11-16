package com.example.restservice.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.restservice.Model.Message;
import com.example.restservice.Model.User;
import jakarta.persistence.criteria.Predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class MessageSpecification {

    public static Specification<Message> hasUsers(User user1, User user2) {
        return (root, query, cb) -> {
            Predicate fromUser1ToUser2 = cb.and(
                cb.equal(root.get("from"), user1),
                cb.equal(root.get("to"), user2)
            );
            Predicate fromUser2ToUser1 = cb.and(
                cb.equal(root.get("from"), user2),
                cb.equal(root.get("to"), user1)
            );
            return cb.or(fromUser1ToUser2, fromUser2ToUser1);
        };
    }
}
