package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling room-related HTTP requests
 * Maps URLs to methods and returns appropriate views
 */
@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Display all rooms
     * @param model Model to add attributes
     * @return View name
     */
    @GetMapping
    public String getAllRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

    /**
     * Display form to add a new room
     * @param model Model to add attributes
     * @return View name
     */
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-room";
    }

    /**
     * Process form submission to add a new room
     * @param room Room object from form
     * @return Redirect URL
     */
    @PostMapping("/add")
    public String addRoom(@ModelAttribute Room room) {
        roomService.addRoom(room);
        return "redirect:/rooms";
    }

    /**
     * Display available rooms
     * @param model Model to add attributes
     * @return View name
     */
    @GetMapping("/available")
    public String getAvailableRooms(Model model) {
        model.addAttribute("rooms", roomService.getAvailableRooms());
        return "rooms";
    }
}