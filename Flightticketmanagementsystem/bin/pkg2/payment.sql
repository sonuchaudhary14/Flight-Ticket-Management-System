CREATE TABLE IF NOT EXISTS payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    ticket_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PAID',
    transaction_id VARCHAR(50) NOT NULL DEFAULT CONCAT('TXN', UNIX_TIMESTAMP()),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
); 