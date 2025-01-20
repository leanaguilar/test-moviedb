CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Ensures auto-generation of ID
    title VARCHAR(255),
    release_date DATE,
    poster_url VARCHAR(255),
    overview TEXT,
    genres VARCHAR(255),
    rating DOUBLE,
    runtime INT,
    language VARCHAR(10)
);