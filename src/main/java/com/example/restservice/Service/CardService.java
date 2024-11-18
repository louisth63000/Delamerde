package com.example.restservice.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Card;
import com.example.restservice.Model.Lot;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.LotRepository;
import com.example.restservice.Repository.CardRepository;
import com.example.restservice.Repository.UserRepository;
import com.example.restservice.Repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private LotRepository lotRepository;

    public Card getPanierByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCard();
    }


    public void removeAnnonceFromCard(Long userId, Long annonceId) {
        // Récupérer le panier de l'utilisateur
        Card card = cardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));

        // Récupérer l'annonce par son ID
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Vérifier s'il existe un lot pour cet utilisateur avec des annonces du même utilisateur
        Lot lot = card.getLots().stream()
                .filter(l -> l.getAnnonces().stream()
                        .anyMatch(a -> a.getUser().equals(annonce.getUser())))  // Vérifier si l'annonce appartient au même utilisateur
                .findFirst()
                .orElseGet(() -> {
                    // Si aucun lot n'existe, créer un nouveau lot et l'affecter à l'utilisateur
                    Lot newLot = new Lot();
                    newLot.setCard(card);
                    newLot.setUser(annonce.getUser());  // Affecter l'utilisateur au lot
                    lotRepository.save(newLot);
                    card.getLots().add(newLot);
                    return newLot;
                });

        // Ajouter l'annonce au lot trouvé ou nouvellement créé
        lot.getAnnonces().add(annonce);
        lotRepository.save(lot);
        cardRepository.save(card);
    }
}


    /*public Panier ajouterLotAuPanier(Long userId, List<Long> annonceIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Panier panier = user.getPanier();
        if (panier == null) {
            panier = new Panier();
            panier.setUser(user);
            user.setPanier(panier);
        }

        // Créer un nouveau lot et ajouter les annonces
        Lot lot = new Lot();
        lot.setPanier(panier);

        for (Long annonceId : annonceIds) {
            Annonce annonce = annonceRepository.findById(annonceId).orElseThrow(() -> new RuntimeException("Annonce not found"));
            lot.getAnnonces().add(annonce);
        }

        panier.getLots().add(lot);
        lotRepository.save(lot);
        return panierRepository.save(panier);
    }*/

    /*public Panier ajouterAnnonceAuPanier(Long userId, Long annonceId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Panier panier = user.getPanier();
        if (panier == null) {
            panier = new Panier();
            panier.setUser(user);
            user.setPanier(panier);
        }

        // Créer un nouveau lot et ajouter l'annonce
        Lot lot = new Lot();
        lot.setPanier(panier);

        // Récupérer et ajouter l'annonce au lot
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce not found"));
        lot.getAnnonces().add(annonce);

        // Ajouter le lot au panier
        panier.getLots().add(lot);

        // Sauvegarder le lot et le panier
        lotRepository.save(lot);
        return panierRepository.save(panier);
    }*/

    /*public void ajouterAnnonceDansPanier(Long userId, Long annonceId) {
        // Récupérer le panier de l'utilisateur
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));

        // Récupérer l'annonce par son ID
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Vérifier s'il existe un lot pour cet utilisateur avec des annonces du même utilisateur
        Lot lot = panier.getLots().stream()
                .filter(l -> l.getAnnonces().stream()
                        .anyMatch(a -> a.getUser().equals(annonce.getUser())))  // Vérifier si l'annonce appartient au même utilisateur
                .findFirst()
                .orElseGet(() -> {
                    // Si aucun lot n'existe, créer un nouveau lot
                    Lot newLot = new Lot();
                    newLot.setPanier(panier);
                    lotRepository.save(newLot);
                    panier.getLots().add(newLot);
                    return newLot;
                });

        // Ajouter l'annonce au lot trouvé ou nouvellement créé
        lot.getAnnonces().add(annonce);
        lotRepository.save(lot);
        panierRepository.save(panier);
    }*/