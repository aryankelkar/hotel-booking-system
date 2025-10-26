package com.hotel.repository;

import com.hotel.model.Booking;
import java.util.List;

/**
 * Repository interface for database operations on bookings
 */
public interface BookingRepository {
    
    /**
     * Find all bookings
     */
    List<Booking> findAll();
    
    /**
     * Find a booking by its ID
     */
    Booking findById(int id);
    
    /**
     * Save a booking
     */
    int save(Booking booking);
    
    /**
     * Delete a booking
     */
    boolean deleteById(int id);
    
    /**
     * Find all bookings for a specific room
     */
    List<Booking> findByRoomId(int roomId);
}