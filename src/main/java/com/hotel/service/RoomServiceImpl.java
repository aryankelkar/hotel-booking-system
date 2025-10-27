package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(int id) {
        return roomRepository.findById(id);
    }

    @Override
    public int addRoom(Room room) {
        
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("Available");
        }
        return roomRepository.save(room);
    }

    @Override
    public boolean updateRoom(Room room) {
        return roomRepository.update(room);
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }

    @Override
    public boolean updateRoomStatus(int roomId, String status) {
        return roomRepository.updateStatus(roomId, status);
    }
}