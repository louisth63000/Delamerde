package com.example.restservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.Model.CustomUserDetails;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Service.SearchService;

@RestController
@RequestMapping("/searche")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/user", produces ={MediaType.APPLICATION_JSON_VALUE,"application/x-yaml"} )
    public ResponseEntity<Page<Search>> getSearchesByUser(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        Page<Search> searchPage = searchService.getSearchesByUser(currentUser, page, size);

        return ResponseEntity.ok(searchPage);
    }
}
