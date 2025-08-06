-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Staff table
CREATE TABLE IF NOT EXISTS staff (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    position VARCHAR(50) NOT NULL,
    department VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drop existing admin account if exists
DELETE FROM admin WHERE email = 'admin@system.com';

-- Insert default admin account with hashed password
-- Password: admin123
INSERT INTO admin (email, password, full_name) 
VALUES ('admin@system.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'System Administrator'); 