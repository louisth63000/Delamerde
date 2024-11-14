package com.example.restservice.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "search")
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  
    private User user;

    
    @ElementCollection
    private List<String> zone =new ArrayList<>();

    @ElementCollection
    private List<String> keywords =new ArrayList<>();

    private String state;

   
    private String date;
    
    public Search() {
    }

    // Constructeur avec tous les paramètres
    public Search(User user, List<String> zone, List<String> keywords, String state, String date) {
        this.user = user;
        this.zone = zone;
        this.keywords = keywords;
        this.state = state;
        this.date = date;
    }

}
