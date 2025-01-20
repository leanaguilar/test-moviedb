Movie API Service

This is a backend service that provides information about movies, including popular movies, movie details, and search functionality. It also includes an API key system to control access to the API endpoints.
Setup Instructions
Prerequisites

    Java 11 or higher
    Spring Boot
    Maven
    H2 Database (configured as an in-memory database for this project)

Clone the Repository

git clone https://github.com/yourusername/movie-api-service.git
cd movie-api-service

Configure the Application

    Update the application.properties file with the following configurations:

Example application.properties:

spring.application.name=demo
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.jdbc.datasource.init=DEBUG
logging.level.org.springframework.jdbc.core=DEBUG
logging.level.org.hibernate.tool.hbm2ddl=DEBUG

# Enable Hibernate SQL Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Database Configuration for in-memory DB
spring.datasource.url=jdbc:h2:mem:moviedb;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable H2 Console for debugging and development purposes
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Enable Hibernate to create the schema automatically
spring.jpa.hibernate.ddl-auto=update

Build and Run the Application

    Build the project using Maven:

mvn clean install

    Run the Spring Boot application:

mvn spring-boot:run

The application will start on http://localhost:8080.
API Key System

The API is protected by an API key system, where users need to pass the API key as a query parameter in every request. This is done by adding api_key to the URL like this:

/movies/popular?api_key=YOUR_API_KEY

API Key Validation

    If the API key is missing or invalid, the API will return the following error responses:
        Missing API key: 400 Bad Request with message "Error: API key is missing".
        Invalid API key: 403 Forbidden with message "Error: Invalid API key".
    The hardcoded valid API key is 12345. Any other key will be considered invalid.

Database Schema

The database schema includes a single table for storing movie information. The schema for the movie table is as follows:
Movies Table
Column	Data Type	Description
id	BIGINT	Unique identifier for the movie (Primary Key)
title	VARCHAR(255)	The title of the movie
release_date	DATE	The release date of the movie
poster_url	VARCHAR(255)	URL to the poster image
overview	TEXT	A short description or overview of the movie
genres	VARCHAR(255)	Comma-separated list of genres
rating	DOUBLE	The average rating of the movie
runtime	INT	The runtime of the movie in minutes
language	VARCHAR(10)	The language of the movie

Example SQL to create the table:

CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    release_date DATE,
    poster_url VARCHAR(255),
    overview TEXT,
    genres VARCHAR(255),
    rating DOUBLE,
    runtime INT,
    language VARCHAR(10)
);

Sample Data

You should populate the database with sample movie records for testing purposes. Here’s an example of inserting a sample record:

INSERT INTO movie (title, release_date, poster_url, overview, genres, rating, runtime, language)
VALUES 
("Inception", "2010-07-16", "https://example.com/poster.jpg", "A mind-bending thriller directed by Christopher Nolan.", "Action, Sci-Fi", 8.8, 148, "English");

How to Test the API

You can test the following endpoints using any API testing tool like Postman, Insomnia, or Curl.
1. Get Popular Movies

Retrieve the top 50 popular movies.

    URL: /movies/popular
    Method: GET
    Query Parameters:
        page: The page number (optional, default: 0)
        size: The number of movies per page (optional, default: 50)
        api_key: Your API key (required)

Example:

GET http://localhost:8080/movies/popular?page=0&size=50&api_key=12345

2. Search for Movies

Search for movies by title, with optional sorting and filtering.

    URL: /movies/search
    Method: GET
    Query Parameters:
        query: The search query (required)
        sort_by: The field to sort by (optional)
        filter: The rating filter (optional)
        page: The page number (optional, default: 0)
        size: The number of results per page (optional, default: 10)
        api_key: Your API key (required)

Example:

GET http://localhost:8080/movies/search?query=The&sort_by=releaseDate&filter=7.0&page=0&size=10&api_key=12345

3. Get Movie Details

Retrieve detailed information about a specific movie by ID.

    URL: /movies/{id}
    Method: GET
    Query Parameters:
        api_key: Your API key (required)

Example:

GET http://localhost:8080/movies/9?api_key=12345

Error Handling

    Missing API Key: 400 Bad Request with message "Error: API key is missing".
    Invalid API Key: 403 Forbidden with message "Error: Invalid API key".
    Movie Not Found: 404 Not Found with message "Movie not found".
    Internal Server Error: 500 Internal Server Error with a message describing the error.