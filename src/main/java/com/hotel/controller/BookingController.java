package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;
import com.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;


@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    @Autowired
    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    
    @GetMapping
    public String getAllBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }
    
    
    @GetMapping({"/book", "/book-room/{id}", "/book-room/id={id}"})
    public String showBookRoomForm(
            @RequestParam(required = false) Integer roomId,
            @PathVariable(required = false) Integer id,
            Model model) {
        
        
        Booking booking = new Booking();
        booking.setCheckIn(LocalDate.now());
        booking.setCheckOut(LocalDate.now().plusDays(1));
        
        
        if (roomId != null) {
            booking.setRoomId(roomId);
        } else if (id != null) {
            booking.setRoomId(id);
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("availableRooms", roomService.getAvailableRooms());
        return "book-room";
    }

    
    @PostMapping("/book")
    public String bookRoom(@ModelAttribute Booking booking) {
        bookingService.createBooking(booking);
        return "redirect:/bookings";
    }

    
    @GetMapping("/checkout/{id}")
    public String checkoutBooking(@PathVariable int id) {
        bookingService.updateBookingStatusToCheckedOut(id);
        return "redirect:/bookings";
    }
    
   
    @GetMapping("/checkin/{id}")
    public String checkinBooking(@PathVariable int id) {
        bookingService.checkinBooking(id);
        return "redirect:/bookings";
    }
    
    
    @GetMapping("/negotiate/{id}")
    public String showNegotiateForm(@PathVariable int id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/bookings";
        }
        model.addAttribute("booking", booking);
        return "negotiate-price";
    }
    
    
    @PostMapping("/negotiate/{id}")
    public String saveNegotiatedPrice(@PathVariable int id, @RequestParam Double negotiatedPrice) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            booking.setNegotiatedPrice(negotiatedPrice);
            bookingService.updateBooking(booking);
        }
        return "redirect:/bookings";
    }
}