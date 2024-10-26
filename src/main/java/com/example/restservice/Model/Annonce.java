package com.example.restservice.Model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private LocalDate publicationDate;
    private String zone;
    private boolean isHandDelivery;

    /*@ElementCollection
    private List<String> keywords =new ArrayList<>();*/
    private String keyword;

    
    public Annonce() {}



    
}