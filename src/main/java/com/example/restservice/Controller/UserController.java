package com.example.restservice.Controller;

//import com.example.restservice.Model.User;
import com.example.restservice.DTO.*;
import com.example.restservice.config.JwtService;
import com.example.restservice.Service.CustomUserDetailsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import com.example.restservice.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
/*import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;*/


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final CustomUserDetailsService userService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public UserController(CustomUserDetailsService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder; 
        this.authenticationManager=authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            userService.registerUser(userDTO, passwordEncoder); 
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
        }
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );
            String token = jwtService.generateToken(authentication.getName());

            // Ajouter le token en tant que cookie
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true); // Empêche l'accès via JavaScript
            cookie.setSecure(true); // Assurez-vous que votre application est en HTTPS
            cookie.setPath("/"); // Le cookie sera accessible sur tout le domaine
            response.addCookie(cookie); // Ajoute le cookie à la réponse

            return ResponseEntity.ok(new AuthResponse(token)); // Renvoyer une réponse (si nécessaire)
            
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Login failed: " + e.getMessage() + "\"}");
        }
    }

    

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}
