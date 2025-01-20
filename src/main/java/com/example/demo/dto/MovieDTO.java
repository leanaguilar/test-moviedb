package com.example.demo.dto;

import java.time.LocalDate;

public class MovieDTO {

    private Long id;
    private String title;
    private LocalDate releaseDate;
    private String posterUrl;
    private String overview;
    private String genres;
    private Double rating;
    private Integer runtime;
    private String language;
	

    // Constructor, getters, and setters

    public MovieDTO(Long id, String title, LocalDate releaseDate, String posterUrl, String overview, String genres, Double rating, Integer runtime, String language) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.genres = genres;
        this.rating = rating;
        this.runtime = runtime;
        this.language = language;
    }


	
	public MovieDTO(Long id, String title, LocalDate releaseDate, String posterUrl, Double rating) {
		
		    this.id = id;
	        this.title = title;
	        this.releaseDate = releaseDate;
	        this.posterUrl = posterUrl;
	        this.rating = rating;
	       
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getOverview() {
		return overview;
	}



	public void setOverview(String overview) {
		this.overview = overview;
	}



	public String getGenres() {
		return genres;
	}



	public void setGenres(String genres) {
		this.genres = genres;
	}



	public Double getRating() {
		return rating;
	}



	public void setRating(Double rating) {
		this.rating = rating;
	}



	public Integer getRuntime() {
		return runtime;
	}



	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}

    
}