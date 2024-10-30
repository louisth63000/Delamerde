package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.restservice.specifications.AnnonceSpecification;
import com.example.restservice.Model.Annonce;
import com.example.restservice.Repository.AnnonceRepository;
import java.time.LocalDateTime;

import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

@DataJpaTest
public class AnnonceServiceTest {
    @Autowired
    private AnnonceRepository annonceRepository;
    
@Test
void testSearchState(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", "Occitanie",true, List.of("lourd", "leger"));

    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasEtat("NEUF"));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchAll(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", "Occitanie",true, List.of("lourd", "leger"));
    annonceRepository.save(annonce1);
    String[] selectedZones = {"Pays de la Loire","Occitanie"};
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasEtat("NEUF")).and(AnnonceSpecification.hasZone(selectedZones)).and(AnnonceSpecification.hasAllKeywords(List.of("lourd")));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchKeywordsWhoDontBelong(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", "Occitanie",true, List.of("lourd", "leger"));
    annonceRepository.save(annonce1);
    String[] selectedZones = {"Pays de la Loire","Occitanie"};
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasEtat("NEUF")).and(AnnonceSpecification.hasZone(selectedZones)).and(AnnonceSpecification.hasAllKeywords(List.of("lourd","leger","fort")));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.isEmpty()).isTrue();
}
@Test
void testSearchKeywordsAlone(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", "Occitanie",true, List.of("lourd", "leger"));
    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasAllKeywords(List.of("lourd","leger")));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchZoneAlone(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF","Occitanie",true, List.of("lourd", "leger"));
    annonceRepository.save(annonce1);
    String[] selectedZones = {"Occitanie"};
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasZone(selectedZones));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchZoneWhoDontBelong(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", "Occitanie",true, List.of("lourd", "leger"));
    annonceRepository.save(annonce1);
    String[] selectedZones = {"Pays de la Loire","Normandie"};
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.hasZone(selectedZones)).and(AnnonceSpecification.hasEtat("NEUF"));

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.isEmpty()).isTrue();
}
@Test
void testSearchLast5DaysWhoDontBelong(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", LocalDateTime.now().minusDays(5).minusMinutes(1),"Occitanie", true);
    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.publishedInLast5Days());

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.isEmpty()).isTrue();
}
@Test
void testSearchLast5Days(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", LocalDateTime.now().minusDays(4).minusMinutes(59),"Occitanie", true);
    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.publishedInLast5Days());

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchLastHour(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", LocalDateTime.now().minusMinutes(59),"Occitanie", true);
    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.publishedInLastHours());

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.contains(annonce1)).isTrue();
}
@Test
void testSearchLastHourWhoDontBelong(){
    Annonce annonce1 = new Annonce("title3", "description3", "NEUF", LocalDateTime.now().minusMinutes(60),"Occitanie", true);
    annonceRepository.save(annonce1);
    Specification<Annonce> spec = Specification.where(AnnonceSpecification.publishedInLastHours());

    List<Annonce> results = annonceRepository.findAll(spec);
    assertThat(results.isEmpty()).isTrue();
}
}
















/*@Mock
    private AnnonceRepository annonceRepository;

    private AnnonceService annonceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        annonceService = new AnnonceService(annonceRepository);
    } */
/*
    @Test
void testSearchAnnoncesByZone() {
    
    List<Annonce> annonces = List.of(new Annonce("title1", "description1", "NEUF", "zone1", true));
    when(annonceRepository.findAll(any(Specification.class))).thenReturn(annonces);

    
    List<Annonce> result = annonceService.searchAnnonces(null, "zone1", null);

  
    assertEquals(1, result.size());
    assertEquals("zone1", result.get(0).getZone());
}
@Test
void testSearchAnnoncesByZoneFalse() {
    // Création d'une annonce avec une zone spécifique
    List<Annonce> annonces = List.of(new Annonce("title1", "description1", "NEUF", "zone1", true));
    when(annonceRepository.findAll(any(Specification.class))).thenReturn(annonces);

    // Effectuer une recherche avec une zone qui ne correspond pas
    List<Annonce> result = annonceService.searchAnnonces("zone2", null, null); // La zone recherchée ne correspond pas

    // Vérifiez que la liste est vide
    assertTrue(result.isEmpty());
}
@Test
void testSearchAnnoncesByState() {
 
    List<Annonce> annonces = List.of(new Annonce("title2", "description2", "NEUF", "zone2", true));

    when(annonceRepository.findAll(any(Specification.class))).thenReturn(annonces);

   
    List<Annonce> result = annonceService.searchAnnonces("NEUF", null, null);

    
    assertEquals(1, result.size());
    assertEquals("NEUF", result.get(0).getState());
}
@Test
void testSearchAnnoncesByKeywords() {
    
    List<Annonce> annonces = List.of(new Annonce("title3", "description3", "NEUF", "zone3",true, List.of("lourd", "leger")));
    when(annonceRepository.findAll(any(Specification.class))).thenReturn(annonces);

    
    List<Annonce> result = annonceService.searchAnnonces(null, null, List.of("lourd"));

    
    assertEquals(1, result.size());
    assertTrue(result.get(0).getKeywords().contains("lourd"));
}
@Test
void testSearchAnnoncesByKeywordsWhoDontBelong() {
    
    List<Annonce> annonces = List.of(new Annonce("title3", "description3", "NEUF", "zone3",true, List.of("lourd", "leger")));
    when(annonceRepository.findAll(any(Specification.class))).thenReturn(annonces);

    
    List<Annonce> result = annonceService.searchAnnonces("zone", "NEUF", List.of("faible"));

    
    assertEquals(1, result.size());
    assertTrue(result.isEmpty());
} */