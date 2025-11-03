package com.hotel.repository;

import com.hotel.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Payment> getPaymentRowMapper() {
        return (rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setPaymentId(rs.getInt("payment_id"));
            payment.setBookingId(rs.getInt("booking_id"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setPaymentStatus(rs.getString("payment_status"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setTransactionDetails(rs.getString("transaction_details"));
            payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
            return payment;
        };
    }

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, getPaymentRowMapper());
    }

    @Override
    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, getPaymentRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Payment> findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        return jdbcTemplate.query(sql, getPaymentRowMapper(), bookingId);
    }

    @Override
    public int save(Payment payment) {
        if (payment.getPaymentId() > 0) {
            String sql = "UPDATE payments SET booking_id = ?, payment_method = ?, payment_status = ?, amount = ?, transaction_details = ? WHERE payment_id = ?";
            
            jdbcTemplate.update(sql,
                payment.getBookingId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getAmount(),
                payment.getTransactionDetails(),
                payment.getPaymentId()
            );
            
            return payment.getPaymentId();
        } else {
            String sql = "INSERT INTO payments (booking_id, payment_method, payment_status, amount, transaction_details, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, payment.getBookingId());
                ps.setString(2, payment.getPaymentMethod());
                ps.setString(3, payment.getPaymentStatus());
                ps.setDouble(4, payment.getAmount());
                ps.setString(5, payment.getTransactionDetails());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);
            
            return keyHolder.getKey().intValue();
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}