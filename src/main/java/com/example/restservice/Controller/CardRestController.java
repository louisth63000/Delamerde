package com.example.restservice.Controller;

import com.example.restservice.Model.Card;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.User;
import com.example.restservice.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardRestController {

    @Autowired
    private CardService cardService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> getCard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Card card = cardService.getCardByUserId(user.getId());

        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(card);
    }

    @PostMapping(value = "/annonce/{annonceId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> addAnnonceToCard(@PathVariable Long annonceId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        cardService.addAnnonce(user.getId(), annonceId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/annonce/{annonceId}", produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/x-yaml" })
    public ResponseEntity<Object> removeAnnonceFromCard(@PathVariable Long annonceId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        cardService.removeAnnonce(user.getId(), annonceId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lot/{lotId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> removeLotFromCard(@PathVariable Long lotId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        cardService.removeLot(user.getId(), lotId);

        return ResponseEntity.ok().build();
    }
}
