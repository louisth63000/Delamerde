package com.example.restservice.Controller;

import com.example.restservice.DTO.*;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.User;
import com.example.restservice.config.JwtService;
import com.example.restservice.Service.CustomUserDetailsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Map;

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

    @Autowired
    private UserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder; 
        this.authenticationManager=authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            userService.registerUser(userDTO, passwordEncoder);
            
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Collections.singletonMap("error", "Username already exists"));
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Collections.singletonMap("error", "User registration failed: " + e.getMessage()));
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
            cookie.setHttpOnly(true); 
            cookie.setPath("/"); 
            response.addCookie(cookie); 

            return ResponseEntity.ok(new AuthResponse(token)); 
            
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Login failed: " + e.getMessage() + "\"}");
        }
    }

   

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, @CookieValue(value = "jwt", required = false) String jwtToken) {
        if (jwtToken != null) {
            // Supprimer le cookie
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("jwt", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); 
            response.addCookie(cookie);
        }
        return ResponseEntity.ok("Déconnexion réussie");
    }
}