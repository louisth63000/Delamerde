package com.example.restservice.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.example.restservice.Model.Annonce;
import com.example.restservice.Model.Search;
import com.example.restservice.Model.User;
import com.example.restservice.Repository.SearchRepository;
import com.example.restservice.specifications.SearchSpecification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSearch() {
        User user = new User();
        user.setUsername("testuser");

        Search search = new Search(user, List.of("zone1"), List.of("keyword1"), "state", "date");

        when(searchRepository.save(any(Search.class))).thenReturn(search);

        Search savedSearch = searchService.saveSearch(search, user);

        assertThat(savedSearch).isNotNull();
        assertThat(savedSearch.getUser()).isEqualTo(user);
        assertThat(savedSearch.getZone()).contains("zone1");
        assertThat(savedSearch.getKeywords()).contains("keyword1");
        assertThat(savedSearch.getState()).isEqualTo("state");
        assertThat(savedSearch.getDate()).isEqualTo("date");
    }

    @Test
    void testGetSearch() {
        Search search = new Search();
        search.setId(1L);

        when(searchRepository.findById(any(Long.class))).thenReturn(Optional.of(search));

        Search foundSearch = searchService.getSearch(1L);

        assertThat(foundSearch).isNotNull();
        assertThat(foundSearch.getId()).isEqualTo(1L);
    }

    @Test
    void testGetSearchesByUser() {
        User user = new User();
        user.setUsername("testuser");

        Search search1 = new Search(user, List.of("zone1"), List.of("keyword1"), "state", "date");
        Search search2 = new Search(user, List.of("zone2"), List.of("keyword2"), "state", "date");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Search> searchPage = new PageImpl<>(List.of(search1, search2), pageable, 2);

        Specification<Search> spec = SearchSpecification.getAllSearchByUser(user);
        when(searchRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(searchPage);

        Page<Search> searches = searchService.getSearchesByUser(user, 0, 10);

        assertThat(searches.getContent()).hasSize(2);
        assertThat(searches.getContent()).contains(search1, search2);
    }

    @Test
    void testGetSearchesByAnnonce() {
        Annonce annonce = new Annonce();
        annonce.setTitle("Test Annonce");

        Search search1 = new Search(new User(), List.of("zone1"), List.of("keyword1"), "state", "date");
        Search search2 = new Search(new User(), List.of("zone2"), List.of("keyword2"), "state", "date");

        Specification<Search> spec = SearchSpecification.getAllSearchByAnnonce(annonce);
        when(searchRepository.findAll(any(Specification.class))).thenReturn(List.of(search1, search2));

        List<Search> searches = searchService.getSearchesByAnnonce(annonce);

        assertThat(searches).hasSize(2);
        assertThat(searches).contains(search1, search2);
    }

    @Test
    void testDeleteSearch() {
        doNothing().when(searchRepository).deleteById(any(Long.class));

        searchService.deleteSearch(1L);

        verify(searchRepository, times(1)).deleteById(1L);
    }
}
