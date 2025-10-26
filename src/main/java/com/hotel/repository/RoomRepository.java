package com.hotel.repository;

import com.hotel.model.Room;
import java.util.List;

/**
 * Repository interface for Room operations
 * Defines methods for CRUD operations on rooms
 */
public interface RoomRepository {
    
    /**
     * Find all rooms in the database
     * @return List of all rooms
     */
    List<Room> findAll();
    
    /**
     * Find a room by its ID
     * @param id Room ID
     * @return Room object if found, null otherwise
     */
    Room findById(int id);
    
    /**
     * Save a new room to the database
     * @param room Room object to save
     * @return ID of the saved room
     */
    int save(Room room);
    
    /**
     * Update an existing room in the database
     * @param room Room object with updated information
     * @return true if update was successful, false otherwise
     */
    boolean update(Room room);
    
    /**
     * Update the status of a room
     * @param roomId Room ID
     * @param status New status ("Available" or "Booked")
     * @return true if update was successful, false otherwise
     */
    boolean updateStatus(int roomId, String status);
    
    /**
     * Find all available rooms
     * @return List of available rooms
     */
    List<Room> findAvailableRooms();
}