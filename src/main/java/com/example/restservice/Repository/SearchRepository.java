package com.example.restservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.restservice.Model.Search;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long>, JpaSpecificationExecutor<Search>{
    
}
