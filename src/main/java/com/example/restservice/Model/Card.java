package com.example.restservice.Model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec l'utilisateur
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Liste des annonces dans le panier
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Lot> lots = new ArrayList<>();

    // Getter pour obtenir la liste des lots
    public List<Lot> getLots() {
        return lots;
    }

    // Setter pour définir la liste des lots (si nécessaire)
    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    // Getter et setter pour l'utilisateur
    public User getUser() {
        return user;
    }
}
