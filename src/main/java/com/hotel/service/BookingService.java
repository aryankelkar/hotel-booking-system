package com.hotel.service;

import com.hotel.model.Booking;
import java.util.List;


public interface BookingService {
   
    List<Booking> getAllBookings();
    
    Booking getBookingById(int id);
   
    int createBooking(Booking booking);
    
    boolean updateBooking(Booking booking);
    
    boolean checkoutBooking(int bookingId); 
    
    List<Booking> getBookingsByRoomId(int roomId); 
   
    boolean checkinBooking(int bookingId);
    
    boolean updateBookingStatusToCheckedOut(int bookingId);
}