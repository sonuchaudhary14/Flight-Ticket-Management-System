create database flight_management;
use flight_management;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS payments;

-- Create admin table
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create staff table
CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    position VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'INACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create flights table
CREATE TABLE flights (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL,
    available_seats INT NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create payments table
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100),
    payment_date DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
);

-- Modify tickets table to add payment status
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    flight_number VARCHAR(20) NOT NULL,
    booking_datetime DATETIME NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);

-- Insert default admin (password: admin123)
INSERT INTO admin (email, password) 
VALUES ('admin@system.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

-- Insert sample flights
INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price, capacity, available_seats)
VALUES 
('FL001', 'New York', 'London', '2024-03-20 10:00:00', '2024-03-20 22:00:00', 799.99, 200, 200),
('FL002', 'London', 'Paris', '2024-03-21 08:00:00', '2024-03-21 09:30:00', 199.99, 150, 150),
('FL003', 'Paris', 'Dubai', '2024-03-22 14:00:00', '2024-03-22 23:00:00', 599.99, 180, 180);
