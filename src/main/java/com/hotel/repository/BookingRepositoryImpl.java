package com.hotel.repository;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * JDBC implementation of BookingRepository
 */
@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RoomRepository roomRepository;
    
    @Autowired
    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate, RoomRepository roomRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomRepository = roomRepository;
    }

    private RowMapper<Booking> getBookingRowMapper() {
        return (rs, rowNum) -> {
            Booking booking = new Booking();
            booking.setBookingId(rs.getInt("booking_id"));
            booking.setCustomerName(rs.getString("customer_name"));
            booking.setContact(rs.getString("contact"));
            booking.setPhoneNumber(rs.getString("phone_number"));
            booking.setCheckIn(rs.getDate("check_in").toLocalDate());
            booking.setCheckOut(rs.getDate("check_out").toLocalDate());
            booking.setRoomId(rs.getInt("room_id"));
            
            Double negotiatedPrice = rs.getObject("negotiated_price") != null ? 
                rs.getDouble("negotiated_price") : null;
            booking.setNegotiatedPrice(negotiatedPrice);
            
            String status = rs.getString("status");
            booking.setStatus(status != null ? status : "Pending");
            
            Room room = roomRepository.findById(booking.getRoomId());
            booking.setRoom(room);
            
            return booking;
        };
    }

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM bookings";
        return jdbcTemplate.query(sql, getBookingRowMapper());
    }

    @Override
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, getBookingRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int save(Booking booking) {
        if (booking.getBookingId() > 0) {
            String sql = "UPDATE bookings SET customer_name = ?, contact = ?, phone_number = ?, check_in = ?, check_out = ?, room_id = ?, negotiated_price = ?, status = ? WHERE booking_id = ?";
            
            jdbcTemplate.update(sql,
                booking.getCustomerName(),
                booking.getContact(),
                booking.getPhoneNumber(),
                Date.valueOf(booking.getCheckIn()),
                Date.valueOf(booking.getCheckOut()),
                booking.getRoomId(),
                booking.getNegotiatedPrice(),
                booking.getStatus(),
                booking.getBookingId()
            );
            
            return booking.getBookingId();
        } else {
            String sql = "INSERT INTO bookings (customer_name, contact, phone_number, check_in, check_out, room_id, negotiated_price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, booking.getCustomerName());
                ps.setString(2, booking.getContact());
                ps.setString(3, booking.getPhoneNumber());
                ps.setDate(4, Date.valueOf(booking.getCheckIn()));
                ps.setDate(5, Date.valueOf(booking.getCheckOut()));
                ps.setInt(6, booking.getRoomId());
                
                if (booking.getNegotiatedPrice() != null) {
                    ps.setDouble(7, booking.getNegotiatedPrice());
                } else {
                    ps.setNull(7, java.sql.Types.DOUBLE);
                }
                
                ps.setString(8, booking.getStatus() != null ? booking.getStatus() : "Pending");
                
                return ps;
            }, keyHolder);
            
            return keyHolder.getKey().intValue();
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public List<Booking> findByRoomId(int roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ?";
        return jdbcTemplate.query(sql, getBookingRowMapper(), roomId);
    }
}