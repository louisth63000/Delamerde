package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Card;
import com.example.restservice.Model.Lot;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.AnnonceRepository;
import com.example.restservice.Repository.CardRepository;
import com.example.restservice.Repository.LotRepository;
import com.example.restservice.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnnonceRepository annonceRepository;

    @Mock
    private LotRepository lotRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

 
    @Test
    void testAddAnnonce_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.addAnnonce(1L, 1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("User not found");
    }

    @Test
    void testRemoveAnnonce() {
        User user = new User();
        user.setId(1L);
        Card card = new Card();
        card.setUser(user);
        card.setLots(new ArrayList<>()); // Initialize the lots list
        Annonce annonce = new Annonce();
        annonce.setId(1L);
        Lot lot = new Lot();
        lot.setCard(card);
        lot.getAnnonces().add(annonce);
        card.getLots().add(lot);

        when(cardRepository.findByUserId(1L)).thenReturn(Optional.of(card));
        when(annonceRepository.findById(1L)).thenReturn(Optional.of(annonce));

        cardService.removeAnnonce(1L, 1L);

        verify(cardRepository).save(card);
        assertThat(lot.getAnnonces()).doesNotContain(annonce);
        assertThat(card.getLots()).doesNotContain(lot);
    }

}
