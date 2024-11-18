package com.example.restservice.Model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec le panier
    @ManyToOne
    @JoinColumn(name = "Card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;  // Un lot est lié à un utilisateur

    // Liste des annonces dans le lot
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

    // Getter et setter pour la liste des annonces
    public List<Annonce> getAnnonces() {
        return annonces;
    }

    public void setAnnonces(List<Annonce> annonces) {
        this.annonces = annonces;
    }
}

