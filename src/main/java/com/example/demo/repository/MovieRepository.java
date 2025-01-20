package com.example.demo.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    
	 // Search for movies by title and rating greater than or equal to minRating
    List<Movie> findByTitleContainingAndRatingGreaterThanEqual(String query, Double minRating, Pageable pageable);

    // Search for movies by title
    List<Movie> findByTitleContaining(String query, Pageable pageable);
	
	// Fetch movies by rating in descending order with pagination
	Page<Movie> findAllByOrderByRatingDesc(Pageable pageable);
}