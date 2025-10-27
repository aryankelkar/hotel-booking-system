package com.hotel.service;

import com.hotel.model.Room;
import java.util.List;


public interface RoomService {
    
    List<Room> getAllRooms();
    
    Room getRoomById(int id);
    
    int addRoom(Room room);
    
    boolean updateRoom(Room room);
    
    List<Room> getAvailableRooms();
   
    boolean updateRoomStatus(int roomId, String status);
}