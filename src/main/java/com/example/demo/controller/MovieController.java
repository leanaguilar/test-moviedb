package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MovieDTO;
import com.example.demo.model.MovieNotFoundException;
import com.example.demo.services.MovieService;

@RestController
public class MovieController {

	private static final String VALID_API_KEY = "12345"; // hardcoded API key

	@Autowired
	private MovieService movieService;

	@GetMapping("/movies/{id}")
	public ResponseEntity<?> getMovieDetails(@PathVariable Long id, @RequestParam("api_key") String apiKey) {

		try {
			// Validate API key
			ResponseEntity<String> apiKeyValidationResult = validateApiKey(apiKey);
			if (apiKeyValidationResult != null) {
				return apiKeyValidationResult; // Return error response if invalid or missing API key
			}
			MovieDTO movieDTO = movieService.getMovieDetails(id);
			return ResponseEntity.ok(movieDTO);

		} catch (MovieNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching movie details: " + e.getMessage());
		}
	}

	@GetMapping("/movies/popular")
	public ResponseEntity<?> getPopularMovies(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "50") int size, @RequestParam("api_key") String apiKey) {

		// Validate API key
		ResponseEntity<String> apiKeyValidationResult = validateApiKey(apiKey);
		if (apiKeyValidationResult != null) {
			return apiKeyValidationResult; // Return error response if invalid or missing API key
		}

		try {
			List<MovieDTO> popularMovies = movieService.getPopularMovies(page, size);
			return ResponseEntity.ok(popularMovies);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@GetMapping("/movies/search")
	public ResponseEntity<?> searchMovies(@RequestParam("query") String query,
			@RequestParam(value = "sort_by", required = false) String sortBy,
			@RequestParam(value = "filter", required = false) Double filter,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, @RequestParam("api_key") String apiKey) {
		try {

			// Validate API key
			ResponseEntity<String> apiKeyValidationResult = validateApiKey(apiKey);
			if (apiKeyValidationResult != null) {
				return apiKeyValidationResult; // Return error response if invalid or missing API key
			}
			List<MovieDTO> movies = movieService.searchMovies(query, sortBy, filter, page, size);
			return ResponseEntity.ok(movies);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters: " + e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal server error: " + e.getMessage());
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleExceptions(Exception e) {
		// Handle any uncaught exceptions globally
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
	}

	// Method to validate API key
	private ResponseEntity<String> validateApiKey(String apiKey) {

		if (apiKey == null || apiKey.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: API key is missing");
		}

		if (!isValidApiKey(apiKey)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Invalid API key");
		}

		return null; // No error, valid API key
	}

	// API Key Validation Method
	private boolean isValidApiKey(String apiKey) {
		if (apiKey == null || apiKey.trim().isEmpty()) {
			// API key is missing, return false
			return false;
		}
		return VALID_API_KEY.equals(apiKey); // Compare with the expected hardcoded key
	}
}
