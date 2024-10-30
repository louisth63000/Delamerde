package com.example.restservice.Model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String state;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publicationDate;
    
    private String zone;
    private boolean isHandDelivery;

    @ElementCollection
    private List<String> keywords =new ArrayList<>();
    //private String keyword;

    
    public Annonce() {}
    public Annonce(String title, String description, String state, String zone, boolean isHandDelivery, List<String> keywords) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.zone = zone;
        this.isHandDelivery = isHandDelivery;
        this.keywords=keywords;
       
    }
    public Annonce(String title, String description, String state, String zone, boolean isHandDelivery) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.zone = zone;
        this.isHandDelivery = isHandDelivery;
        
        
    }
    public Annonce(String title, String description, String state, LocalDateTime publicationDate, String zone, boolean isHandDelivery) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.zone = zone;
        this.isHandDelivery = isHandDelivery;
        this.publicationDate=publicationDate;
        
    }

    
}