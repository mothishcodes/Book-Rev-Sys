CREATE DATABASE book_reviews;
USE book_reviews;
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    rating INT,
    review TEXT
);
