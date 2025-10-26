package com.hotel.repository;

import com.hotel.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementation of RoomRepository using Spring JDBC
 * Handles all database operations for Room entity
 */
@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;
    
    // Row mapper for converting result set to Room object
    private final RowMapper<Room> roomRowMapper = (rs, rowNum) -> {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setType(rs.getString("type"));
        room.setPrice(rs.getDouble("price"));
        room.setStatus(rs.getString("status"));
        return room;
    };

    @Autowired
    public RoomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT * FROM rooms";
        return jdbcTemplate.query(sql, roomRowMapper);
    }

    @Override
    public Room findById(int id) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, roomRowMapper, id);
        } catch (Exception e) {
            return null; // Return null if room not found
        }
    }

    @Override
    public int save(Room room) {
        String sql = "INSERT INTO rooms (room_number, type, price, status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().intValue();
    }

    @Override
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number = ?, type = ?, price = ?, status = ? WHERE room_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, 
                room.getRoomNumber(), 
                room.getType(), 
                room.getPrice(), 
                room.getStatus(), 
                room.getRoomId());
        return rowsAffected > 0;
    }

    @Override
    public boolean updateStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, roomId);
        return rowsAffected > 0;
    }

    @Override
    public List<Room> findAvailableRooms() {
        String sql = "SELECT * FROM rooms WHERE status = 'Available'";
        return jdbcTemplate.query(sql, roomRowMapper);
    }
}