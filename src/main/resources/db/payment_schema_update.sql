-- Add payment-related columns to bookings table
ALTER TABLE bookings 
ADD COLUMN payment_status VARCHAR(50) DEFAULT 'Awaiting Payment',
ADD COLUMN payment_method VARCHAR(50) DEFAULT NULL;

-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    amount DOUBLE NOT NULL,
    transaction_details VARCHAR(255),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);