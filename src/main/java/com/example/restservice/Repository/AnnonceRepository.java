package com.example.restservice.Repository;

import com.example.restservice.Model.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    
    List<Annonce> findByZone(String zone);
    
   /*  @Query("SELECT a FROM Annonce a JOIN a.keywords k WHERE k IN :keywords")
    List<Annonce> findByKeywords(@Param("keywords") List<String> keywords);*/
}
