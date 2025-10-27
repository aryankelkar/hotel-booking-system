package com.hotel.repository;

import com.hotel.model.Booking;
import java.util.List;


public interface BookingRepository {
    
    List<Booking> findAll();
    
    Booking findById(int id);
    
    int save(Booking booking);
    
    boolean deleteById(int id);
    
    List<Booking> findByRoomId(int roomId);
}