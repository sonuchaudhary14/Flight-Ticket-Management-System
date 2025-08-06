create database flight_management;
use flight_management;

CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO admin (email, password, full_name) 	
VALUES ('admin@system.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'System Administrator');
select * from admin;

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    position VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL,
    status ENUM('ACTIVE', 'PENDING', 'INACTIVE') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
select * from staff;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
select * from users;

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
select* from flights;

-- Create tickets table
CREATE TABLE tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    flight_number VARCHAR(20) NOT NULL,
    booking_datetime DATETIME NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'BOOKED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);
ALTER TABLE tickets 
ADD COLUMN payment_status VARCHAR(20) DEFAULT 'PENDING' AFTER status;

select * from tickets;
ALTER TABLE tickets 
ADD COLUMN payment_method VARCHAR(50) DEFAULT NULL 
AFTER payment_status;

CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    ticket_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PAID',
    transaction_id VARCHAR(50) NOT NULL DEFAULT (CONCAT('TXN', UNIX_TIMESTAMP())),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
);


-- Insert flight data with status field
INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price, capacity, available_seats, status) VALUES  
('FL005', 'Kathmandu', 'Pokhara', '2025-03-25 09:00:00', '2025-03-25 09:40:00', 99.99, 180, 180, 'SCHEDULED'),  
('FL006', 'Kathmandu', 'Bharatpur', '2025-03-27 12:00:00', '2025-03-27 12:25:00', 79.99, 150, 150, 'SCHEDULED'),  
('FL007', 'Biratnagar', 'Kathmandu', '2025-04-05 14:00:00', '2025-04-05 15:00:00', 129.99, 200, 200, 'SCHEDULED'),  
('FL008', 'Pokhara', 'Kathmandu', '2025-04-10 16:00:00', '2025-04-10 16:40:00', 99.99, 180, 180, 'SCHEDULED'),  
('FL009', 'Bharatpur', 'Kathmandu', '2025-04-15 08:00:00', '2025-04-15 08:25:00', 79.99, 150, 150, 'SCHEDULED'),  
('FL010', 'Kathmandu', 'Lukla', '2025-04-20 07:00:00', '2025-04-20 07:45:00', 149.99, 100, 100, 'SCHEDULED'),  
('FL011', 'Lukla', 'Kathmandu', '2025-04-22 10:00:00', '2025-04-22 10:45:00', 149.99, 100, 100, 'SCHEDULED'),  
('FL012', 'Kathmandu', 'Janakpur', '2025-05-02 11:00:00', '2025-05-02 11:30:00', 89.99, 160, 160, 'SCHEDULED'),  
('FL013', 'Janakpur', 'Kathmandu', '2025-05-05 15:00:00', '2025-05-05 15:30:00', 89.99, 160, 160, 'SCHEDULED'),  
('FL014', 'Kathmandu', 'Biratnagar', '2025-05-10 09:30:00', '2025-05-10 10:30:00', 129.99, 200, 200, 'SCHEDULED'),  
('FL015', 'Biratnagar', 'Kathmandu', '2025-05-15 13:00:00', '2025-05-15 14:00:00', 129.99, 200, 200, 'SCHEDULED'),  
('FL016', 'Pokhara', 'Bharatpur', '2025-05-18 17:00:00', '2025-05-18 17:25:00', 59.99, 120, 120, 'SCHEDULED'),  
('FL017', 'Kathmandu', 'Pokhara', '2025-06-01 06:30:00', '2025-06-01 07:10:00', 99.99, 180, 180, 'SCHEDULED'),  
('FL018', 'Kathmandu', 'Bharatpur', '2025-06-05 12:00:00', '2025-06-05 12:25:00', 79.99, 150, 150, 'SCHEDULED'),  
('FL019', 'Pokhara', 'Kathmandu', '2025-06-10 10:00:00', '2025-06-10 10:40:00', 99.99, 180, 180, 'SCHEDULED'),  
('FL020', 'Lukla', 'Kathmandu', '2025-06-15 09:30:00', '2025-06-15 10:15:00', 149.99, 100, 100, 'SCHEDULED');