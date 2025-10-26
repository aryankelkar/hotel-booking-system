package com.hotel.service;

import com.hotel.model.Booking;
import java.util.List;

/**
 * Service interface for hotel booking operations
 */
public interface BookingService {
    
    /**
     * Get all bookings
     */
    List<Booking> getAllBookings();
    
    /**
     * Get a booking by its ID
     */
    Booking getBookingById(int id);
    
    /**
     * Create a new booking
     */
    int createBooking(Booking booking);
    
    /**
     * Update an existing booking
     */
    boolean updateBooking(Booking booking);
    
    /**
     * Check out a booking (delete booking and update room status)
     */
    boolean checkoutBooking(int bookingId);
    
    /**
     * Get all bookings for a specific room
     */
    List<Booking> getBookingsByRoomId(int roomId);
    
    /**
     * Check in a booking (update status to "Checked In")
     */
    boolean checkinBooking(int bookingId);
    
    /**
     * Update booking status to "Checked Out" without deleting the booking
     */
    boolean updateBookingStatusToCheckedOut(int bookingId);
}