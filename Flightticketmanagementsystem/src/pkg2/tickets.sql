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
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(50) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
); 