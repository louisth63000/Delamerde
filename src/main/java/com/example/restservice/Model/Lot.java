package com.example.restservice.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Card_id")
    @JsonBackReference
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(name = "lot_annonces", joinColumns = @JoinColumn(name = "lot_id"), inverseJoinColumns = @JoinColumn(name = "annonce_id"))
    @JsonManagedReference
    private List<Annonce> annonces = new ArrayList<>();

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Annonce> getAnnonces() {
        return annonces;
    }

    public void setAnnonces(List<Annonce> annonces) {
        this.annonces = annonces;
    }

    public String getAnnonceTitles() {
        return annonces.stream()
                .map(Annonce::getTitle)
                .collect(Collectors.joining(", "));
    }
}
