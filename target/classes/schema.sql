-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS hotel_db;

-- Use the database
USE hotel_db;

-- Drop tables if they exist to avoid conflicts
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS rooms;

-- Create rooms table
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Available'
);

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    contact VARCHAR(50) NOT NULL,
    phone_number VARCHAR(10) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    room_id INT NOT NULL,
    negotiated_price DOUBLE,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- Insert sample rooms with Indian room types and INR prices (3 rooms per floor)
INSERT INTO rooms (room_number, type, price, status) VALUES
('101', 'Non AC Room', 1500.00, 'Available'),
('102', 'Non AC Room', 1500.00, 'Available'),
('103', 'Non AC Room', 1500.00, 'Available'),
('201', 'AC Room', 2500.00, 'Available'),
('202', 'AC Room', 2500.00, 'Available'),
('203', 'AC Room', 2500.00, 'Available'),
('301', 'Luxury Room', 4500.00, 'Available'),
('302', 'Luxury Room', 4500.00, 'Available'),
('303', 'Luxury Room', 4500.00, 'Available');