package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of booking service operations
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(int id) {
        return bookingRepository.findById(id);
    }

    @Override
    public int createBooking(Booking booking) {
        int bookingId = bookingRepository.save(booking);
        roomService.updateRoomStatus(booking.getRoomId(), "Booked");
        return bookingId;
    }

    @Override
    public boolean checkoutBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            return false;
        }
        
        // Update room status to Available
        roomService.updateRoomStatus(booking.getRoomId(), "Available");
        
        // Delete the booking
        bookingRepository.deleteById(bookingId);
        return true;
    }

    @Override
    public List<Booking> getBookingsByRoomId(int roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
    
    @Override
    public boolean updateBooking(Booking booking) {
        return bookingRepository.save(booking) > 0;
    }
    
    @Override
    public boolean checkinBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            return false;
        }
        
        booking.setStatus("Checked In");
        return bookingRepository.save(booking) > 0;
    }
    
    @Override
    public boolean updateBookingStatusToCheckedOut(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            return false;
        }
        
        booking.setStatus("Checked Out");
        roomService.updateRoomStatus(booking.getRoomId(), "Available");
        return bookingRepository.save(booking) > 0;
    }
}