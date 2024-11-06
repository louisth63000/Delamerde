package com.example.restservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.restservice.Repository.UserRepository;

import scala.util.Random;

import com.example.restservice.DTO.UserRegistrationDTO;
import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.User;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user); 
    }


    public void registerUser(UserRegistrationDTO userDTO, PasswordEncoder passwordEncoder) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
       // user.setId(new Random().nextLong());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); 
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
    }
}
