package com.example.restservice.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "lot_annonces",
            joinColumns = @JoinColumn(name = "lot_id"),
            inverseJoinColumns = @JoinColumn(name = "annonce_id")
    )
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
}

