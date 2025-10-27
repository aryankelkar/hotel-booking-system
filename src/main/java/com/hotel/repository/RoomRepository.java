package com.hotel.repository;

import com.hotel.model.Room;
import java.util.List;


public interface RoomRepository {
    
    List<Room> findAll();
    
    Room findById(int id);
    
    int save(Room room);
    
    boolean update(Room room);
    
    boolean updateStatus(int roomId, String status);
    
    List<Room> findAvailableRooms();
}