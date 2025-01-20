package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.dto.MovieDTO;
import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.services.MovieService;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository; // Mocked repository

    @InjectMocks
    private MovieService movieService; // Service to test

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mock objects
    }

    @Test
    public void testGetMovieDetails() {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie 1", LocalDate.parse("2023-01-01"), "url1", 8.5); // Create a movie instance

        // Define the mock behavior for repository
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));

        // Act
        MovieDTO movieDTO = movieService.getMovieDetails(1L); // Call the service method

        // Assert
        assertNotNull(movieDTO); // Ensure movieDTO is not null
        assertEquals("Movie 1", movieDTO.getTitle()); // Validate title
        assertEquals(8.5, movieDTO.getRating(), 0.1); // Validate rating
    }

    @Test
    public void testGetPopularMovies() {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie 1", LocalDate.parse("2023-01-01"), "url1", 9.0);
        Movie movie2 = new Movie(2L, "Movie 2", LocalDate.parse("2022-01-01"), "url2", 8.5);
        List<Movie> mockMovies = List.of(movie1, movie2);
        Page<Movie> moviePage = new PageImpl<>(mockMovies, PageRequest.of(0, 10), mockMovies.size());

        // Mock the repository call
        when(movieRepository.findAllByOrderByRatingDesc(PageRequest.of(0, 10))).thenReturn(moviePage);

        // Act
        List<MovieDTO> movieDTOs = movieService.getPopularMovies(0, 10);

        // Assert
        assertNotNull(movieDTOs); // Ensure the list is not null
        assertEquals(2, movieDTOs.size()); // Validate the size of the returned list
        assertEquals("Movie 1", movieDTOs.get(0).getTitle()); // Validate the title of the first movie
        assertEquals("Movie 2", movieDTOs.get(1).getTitle()); // Validate the title of the second movie
        assertTrue(movieDTOs.get(0).getRating() > movieDTOs.get(1).getRating()); // Ensure movies are sorted by rating
    }
    
    @Test
    public void testSearchMoviesWithValidQuery() {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie 1", LocalDate.parse("2023-01-01"), "url1", 8.5);
        Movie movie2 = new Movie(2L, "Movie 2", LocalDate.parse("2022-01-01"), "url2", 7.5);
        List<Movie> mockMovies = List.of(movie1, movie2);
        String query = "Movie";
        Double minRating = 7.0;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").descending());

        // Mock repository behavior
        when(movieRepository.findByTitleContainingAndRatingGreaterThanEqual(query, minRating, pageable))
                .thenReturn(mockMovies);

        // Act
        List<MovieDTO> movieDTOs = movieService.searchMovies(query, "rating", minRating, 0, 10);

        // Assert
        assertNotNull(movieDTOs); // Ensure the list is not null
        assertEquals(2, movieDTOs.size()); // Validate the number of returned movies
        assertEquals("Movie 1", movieDTOs.get(0).getTitle()); // Validate title of the first movie
        assertEquals("Movie 2", movieDTOs.get(1).getTitle()); // Validate title of the second movie
        assertTrue(movieDTOs.get(0).getRating() > movieDTOs.get(1).getRating()); // Ensure sorting by rating
    }

    @Test
    public void testSearchMoviesWithNoResults() {
        // Arrange
        String query = "Nonexistent Movie";
        Double minRating = 7.0;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").descending());

        // Mock repository behavior to return no movies
        when(movieRepository.findByTitleContainingAndRatingGreaterThanEqual(query, minRating, pageable))
                .thenReturn(List.of());

        // Act
        List<MovieDTO> movieDTOs = movieService.searchMovies(query, "rating", minRating, 0, 10);

        // Assert
        assertNotNull(movieDTOs); // Ensure the list is not null
        assertTrue(movieDTOs.isEmpty()); // Ensure the list is empty
    }

    @Test
    public void testSearchMoviesWithInvalidRatingFilter() {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie 1", LocalDate.parse("2023-01-01"), "url1", 8.5);
        List<Movie> mockMovies = List.of(movie1);
        String query = "Movie";
        Double minRating = -1.0; // Invalid rating filter
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating").descending());

        // Mock repository behavior for invalid rating filter
        when(movieRepository.findByTitleContaining(query, pageable))
                .thenReturn(mockMovies);

        // Act
        List<MovieDTO> movieDTOs = movieService.searchMovies(query, "rating", minRating, 0, 10);

        // Assert
        assertNotNull(movieDTOs); // Ensure the list is not null
        assertEquals(1, movieDTOs.size()); // Validate that one movie is returned
        assertEquals("Movie 1", movieDTOs.get(0).getTitle()); // Validate title
    }
}
