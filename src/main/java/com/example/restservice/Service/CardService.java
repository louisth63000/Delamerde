package com.example.restservice.Service;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Card;
import com.example.restservice.Model.Lot;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.AnnonceRepository;
import com.example.restservice.Repository.CardRepository;
import com.example.restservice.Repository.LotRepository;
import com.example.restservice.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Card getCardByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getCard();
    }

    public void addAnnonce(Long userId, Long annonceId) {

        Card card = getCardByUserId(userId);

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new EntityNotFoundException("Annonce non trouvée"));

        // Check if the annonce is already in the card
        boolean annonceExists = card.getLots().stream()
                .anyMatch(lot -> lot.getAnnonces().contains(annonce));

        if (!annonceExists) {
            Lot lot = card.getLots().stream()
                    .filter(l -> l.getAnnonces().stream()
                            .anyMatch(a -> a.getUser().equals(annonce.getUser())))
                    .findFirst()
                    .orElseGet(() -> {
                        Lot newLot = new Lot();
                        newLot.setCard(card);
                        newLot.setUser(annonce.getUser());
                        lotRepository.save(newLot);
                        card.getLots().add(newLot);
                        return newLot;
                    });

            lot.getAnnonces().add(annonce);
            lotRepository.save(lot);
            cardRepository.save(card);
        }
    }

    public void removeAnnonce(Long userId, Long annonceId) {
        Card card = cardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        Lot lot = card.getLots().stream()
                .filter(l -> l.getAnnonces().contains(annonce))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée dans le panier"));

        lot.getAnnonces().remove(annonce);
        lotRepository.save(lot);

        if (lot.getAnnonces().isEmpty()) {
            card.getLots().remove(lot);
            lotRepository.delete(lot);
        }

        cardRepository.save(card);
    }

    public void removeLot(Long userId, Long lotId) {
        Card card = cardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));

        if (card != null) {
            Lot lot = lotRepository.findById(lotId).orElse(null);
            if (lot != null && lot.getCard().getId().equals(card.getId())) {
                card.getLots().remove(lot);
                lotRepository.delete(lot);
                cardRepository.save(card);
            }
        }
    }
}
