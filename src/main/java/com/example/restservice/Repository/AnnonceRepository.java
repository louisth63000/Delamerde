package com.example.restservice.Repository;

import com.example.restservice.Model.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long>, JpaSpecificationExecutor<Annonce> {

    
    
   /* @Query("SELECT a FROM Annonce a " +
       "WHERE (:zone IS NULL OR a.zone = :zone) " +
       "AND (:state IS NULL OR a.state = :state) " +
       "AND (:keywords IS NULL OR " +
       "  (SELECT COUNT(k) FROM a.keywords k WHERE k IN :keywords) = :#{#keywords.size()})")
    List<Annonce> searchAnnonces(
    @Param("zone") String zone,
    @Param("state") String state,
    @Param("keywords") List<String> keywords
    );
 */


}
