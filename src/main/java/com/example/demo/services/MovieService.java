package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MovieDTO;
import com.example.demo.model.Movie;
import com.example.demo.model.MovieNotFoundException;
import com.example.demo.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDTO> getPopularMovies(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Movie> moviePage = movieRepository.findAllByOrderByRatingDesc(pageRequest);

     // Map the movie entities to MovieDTOs
        return moviePage.stream()
                .map(movie -> new MovieDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseDate(),
                        movie.getPosterUrl(),
                        movie.getRating() // Closing parenthesis for the movie.getRating() method
                )) // Closing parenthesis for the map function
                .collect(Collectors.toList()); // Closing parenthesis for collect
    }
    
    public List<MovieDTO> searchMovies(String query, String sortBy, Double minRating, int page, int size) {
        // Default sort by "rating" if sortBy is null or invalid
        Sort sort = getValidSort(sortBy);

        // Create Pageable object with sorting and pagination options
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            // Search movies by title and apply filter for rating if provided
            List<Movie> movies;
            if (minRating != null && minRating >= 0) {
                movies = movieRepository.findByTitleContainingAndRatingGreaterThanEqual(query, minRating, pageable);
            } else {
                movies = movieRepository.findByTitleContaining(query, pageable);
            }

            // Map the movie entities to MovieDTOs
            return movies.stream()
                    .map(movie -> new MovieDTO(
                            movie.getId(),
                            movie.getTitle(),
                            movie.getReleaseDate(),
                            movie.getPosterUrl(),
                            movie.getRating()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error (you can add a logger here)
            throw new RuntimeException("Error while searching for movies: " + e.getMessage());
        }
    }

    private Sort getValidSort(String sortBy) {
        if (sortBy == null || !isValidSortField(sortBy)) {
            return Sort.by(Sort.Order.desc("rating")); // Default to sorting by rating
        }
        return Sort.by(Sort.Order.desc(sortBy));
    }

    private boolean isValidSortField(String field) {
        return "rating".equals(field) || "releaseDate".equals(field);
    }
    
    // Method to fetch detailed movie information by ID
    public MovieDTO getMovieDetails(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);

        // If the movie is not found, throw an exception
        if (movieOptional.isEmpty()) {
            throw new MovieNotFoundException("Movie with ID " + id + " not found.");
        }

        Movie movie = movieOptional.get();

        // Map the Movie entity to MovieDTO with detailed information
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getOverview(),
                movie.getGenres(),  
                movie.getRating(),
                movie.getRuntime(),
                movie.getLanguage()
        );
    }
    
    public void setMovieRepository(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}

    