package com.hotel.repository;

import com.hotel.model.Payment;
import java.util.List;

public interface PaymentRepository {
    List<Payment> findAll();
    Payment findById(int id);
    List<Payment> findByBookingId(int bookingId);
    int save(Payment payment);
    boolean delete(int id);
}