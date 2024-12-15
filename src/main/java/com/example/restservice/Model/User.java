package com.example.restservice.Model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Collection;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    // Relation un-à-un avec Panier
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "panier_id", referencedColumnName = "id")
    @JsonManagedReference
    private Card card;
    private boolean hasNotification;
    

    @PrePersist
    public void prePersist() {
        if (hasNotification == false) {
            hasNotification = true;
        }
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Annonce> annonces = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return null; 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }
    public User(){};

    public User(Long id, String name){
        this.id=id;
        this.username=name;
    }
   

    
}
