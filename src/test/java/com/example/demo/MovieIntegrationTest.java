package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {

	}

	@AfterEach
	void tearDown() {
		
	}

	@Test
	public void testGetPopularMoviesIntegration() throws Exception {

		// Perform the API request to fetch the popular movies and check the first one
		mockMvc.perform(get("/movies/popular?page=0&size=5&api_key=12345")).andExpect(status().isOk()) // Ensure status
																										// is OK (200)
				.andExpect(jsonPath("$[0].title").value("The Shawshank Redemption")) // First movie in the list should
																						// have the highest rating
				.andExpect(jsonPath("$[0].rating").value(9.3));
	}

	@Test
	public void testMovieSearchEndpoint() throws Exception {
		// Perform GET request for movie search
		mockMvc.perform(get("/movies/search?query=The&sort_by=releaseDate&filter=7.0&page=0&size=10&api_key=12345"))
				.andExpect(status().isOk()); // Check if the response status is 200 OK
	}

	@Test
	public void testMovieDetailEndpoint() throws Exception {
		// Perform GET request to movie details endpoint
		mockMvc.perform(get("/movies/9?api_key=12345")).andExpect(status().isOk()); // Check for 200 OK response
	}

}
