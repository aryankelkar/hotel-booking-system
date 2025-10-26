package com.hotel.service;

import com.hotel.model.Room;
import java.util.List;

/**
 * Service interface for Room operations
 * Defines business logic methods for room management
 */
public interface RoomService {
    
    /**
     * Get all rooms
     * @return List of all rooms
     */
    List<Room> getAllRooms();
    
    /**
     * Get a room by its ID
     * @param id Room ID
     * @return Room object if found, null otherwise
     */
    Room getRoomById(int id);
    
    /**
     * Add a new room
     * @param room Room object to add
     * @return ID of the added room
     */
    int addRoom(Room room);
    
    /**
     * Update an existing room
     * @param room Room object with updated information
     * @return true if update was successful, false otherwise
     */
    boolean updateRoom(Room room);
    
    /**
     * Get all available rooms
     * @return List of available rooms
     */
    List<Room> getAvailableRooms();
    
    /**
     * Update room status
     * @param roomId Room ID
     * @param status New status ("Available" or "Booked")
     * @return true if update was successful, false otherwise
     */
    boolean updateRoomStatus(int roomId, String status);
}