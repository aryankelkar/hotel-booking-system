CREATE DATABASE IF NOT EXISTS hotel_db;


USE hotel_db;


-- Drop tables in dependency order to avoid FK issues
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS rooms;


CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Available'
);


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
    payment_status VARCHAR(20) DEFAULT 'Awaiting Payment',
    payment_method VARCHAR(20),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);


CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    transaction_details VARCHAR(255),
    payment_date TIMESTAMP NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);


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