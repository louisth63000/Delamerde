package com.example.restservice.Service;


    
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.example.restservice.Model.Annonce;
import com.example.restservice.Repository.AnnonceRepository;
import com.example.restservice.Repository.UserRepository;

import java.util.Optional;
import java.time.LocalDateTime;
import com.example.restservice.Model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;

public class UserAnnonceTest {
        @Mock
        private AnnonceRepository annonceRepository;
    
        @InjectMocks
        private AnnonceService annonceService;
    
        @Test
        void testDeleteAnnonceByUnauthorizedUser() {
           
            User owner = new User();
            owner.setId(1L);
            Annonce annonce = new Annonce();
            annonce.setId(1L);
            annonce.setUser(owner);
    
            
            User unauthorizedUser = new User();
            unauthorizedUser.setId(2L);
    
            
            when(annonceRepository.findById(1L)).thenReturn(Optional.of(annonce));
    
            
            assertThrows(AccessDeniedException.class, () -> {
                annonceService.deleteAnnonce(1L);
            });
        }
}
    