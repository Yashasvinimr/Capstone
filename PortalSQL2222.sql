-- Create database
CREATE DATABASE club_portal2;

-- Use the created database
USE club_portal2;

-- Create tables
-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(10) NOT NULL,
    year INT NOT NULL,
    role ENUM('MEMBER', 'COORDINATOR', 'ADMIN') DEFAULT 'MEMBER',
    password VARCHAR(255) NOT NULL
);

-- Clubs table
CREATE TABLE clubs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    coordinator_id INT NOT NULL,
    FOREIGN KEY (coordinator_id) REFERENCES users(id)
);

-- Events table
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    club_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    venue VARCHAR(100),
    date DATE,
    time TIME,
    reg_fee DECIMAL(10, 2),
    is_published BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (club_id) REFERENCES clubs(id)
);

-- Registrations table
CREATE TABLE registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample data
INSERT INTO users (name, email, phone, year, role, password) VALUES
('Admin User', 'admin@clubportal.com', '9876543210', 2025, 'ADMIN', 'admin123'),
('John Doe', 'jd21@bmsce.ac.in', '9876543210', 2025, 'MEMBER', 'pass123'),
('Jane Smith', 'js21@bmsce.ac.in', '9876543211', 2025, 'COORDINATOR', 'pass123');

INSERT INTO clubs (name, description, coordinator_id) VALUES
('Tech Club', 'A club for technology enthusiasts', 3),
('Music Club', 'A club for music lovers', 3);

INSERT INTO events (club_id, name, description, venue, date, time, reg_fee, is_published) VALUES
(1, 'Tech Talk', 'A talk on latest technologies', 'Auditorium', '2025-04-15', '10:00:00', 100, TRUE),
(2, 'Music Concert', 'An event to showcase musical talents', 'Music Hall', '2025-04-20', '18:00:00', 50, FALSE);

INSERT INTO registrations (event_id, user_id) VALUES
(1, 2), (2, 2);
